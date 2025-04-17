package com.example.ManieksApp.util;

import com.example.ManieksApp.exceptions.EmptyDataException;

public class ValidateDate {
    public static void validateName(String Name) {
        if(Name == null || Name.isEmpty()){
            throw new EmptyDataException("Name cannot be null or empty");
        }
    }

    public static void validateAuthor(String Name) {
        if(Name == null || Name.isEmpty()){
            throw new EmptyDataException("Author cannot be null or empty");
        }
    }

}
