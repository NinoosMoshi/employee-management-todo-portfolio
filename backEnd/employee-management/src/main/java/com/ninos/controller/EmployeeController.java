package com.ninos.controller;

import com.ninos.apiResponse.EmployeeResponse;
import com.ninos.model.Employee;
import com.ninos.repository.EmployeeRepository;
import com.ninos.service.EmployeeService;
import com.ninos.utill.EmployeeGenerateCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.core.io.Resource;
import java.util.List;

@RestController
public class EmployeeController {

    private EmployeeRepository employeeRepository;
    private EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeRepository employeeRepository, EmployeeService employeeService) {
        this.employeeRepository = employeeRepository;
        this.employeeService = employeeService;
    }


    @GetMapping("/all")
    public List<Employee> getAllEmployee(){
        return employeeRepository.findAll();
    }



    @PostMapping("/addEmployee")
    public EmployeeResponse addEmployee(
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName,
            @RequestParam("email") String email,
            @RequestParam("phoneNumber") String phoneNumber,
            @RequestParam(value="file",required=false) MultipartFile file) {

        Employee dbFile = employeeService.saveEmployee(firstName,lastName,email,phoneNumber,file);
        String code = EmployeeGenerateCode.generateRandomCode();

        if(file != null){
            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/downloadFile/")
                    .path(dbFile.getId())
                    .toUriString();
            return new EmployeeResponse(dbFile.getFirstName(), dbFile.getLastName(),dbFile.getEmail(), dbFile.getPhoneNumber(),code,dbFile.getFileName(), fileDownloadUri,
                    file.getContentType(), file.getSize());
        }
        else {
            String fileDownloadUri = "https://robohash.org/"+ firstName;
            return new EmployeeResponse(dbFile.getFirstName(),dbFile.getLastName(),dbFile.getEmail(),dbFile.getPhoneNumber(), code, dbFile.getFileName(),fileDownloadUri, file.getContentType(),file.getSize());
        }

    }



    @GetMapping("/downloadFile/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileId) {
        // Load file from database
        Employee dbFile = employeeService.getFile(fileId);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(dbFile.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + dbFile.getFileName() + "\"")
                .body(new ByteArrayResource(dbFile.getData()));
    }



}
