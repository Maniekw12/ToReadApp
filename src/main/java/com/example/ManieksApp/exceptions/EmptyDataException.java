package com.example.ManieksApp.exceptions;

import com.example.ManieksApp.util.ValidateDate;

public class EmptyDataException extends RuntimeException {
    public EmptyDataException(String message) {
        super(message);
    }
}
