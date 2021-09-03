package com.ninos.service;

import com.ninos.exception.FileStorageException;
import com.ninos.exception.MyFileNotFoundException;
import com.ninos.model.Employee;
import com.ninos.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class EmployeeService {

    private EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Employee saveEmployee(String firstName,String lastName, String email, String phoneNumber, MultipartFile file) {

        if(file != null){
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            try {
                // Check if the file's name contains invalid characters
                if(fileName.contains("..")) {
                    throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
                }
                Employee employee = new Employee(firstName, lastName, email, phoneNumber,fileName, file.getContentType(), file.getBytes());
                return employeeRepository.save(employee);

            } catch (IOException ex) {
                throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
            }
        }

        Employee employee = new Employee(firstName, lastName, email, phoneNumber);
        return employeeRepository.save(employee);

         }

    public Employee getFile(String fileId) {
        return employeeRepository.findById(fileId)
                .orElseThrow(() -> new MyFileNotFoundException("File not found with id " + fileId));
    }


}




