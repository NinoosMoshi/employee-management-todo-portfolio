package com.ninos.apiResponse;

import lombok.Data;

@Data
public class EmployeeResponse {

    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String employeeCode;

    private String fileName;
    private String fileDownloadUri;
    private String fileType;
    private long size;


    public EmployeeResponse(String firstName, String lastName, String email, String phoneNumber, String employeeCode) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.employeeCode = employeeCode;
    }
}
