package com.ninos.utill;

import org.apache.commons.lang3.RandomStringUtils;

public class EmployeeGenerateCode {

    public static String generateRandomCode(){
        return RandomStringUtils.randomAlphabetic(8);
    }

}
