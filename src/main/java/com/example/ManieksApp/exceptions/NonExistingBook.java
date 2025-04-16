package com.example.ManieksApp.exceptions;

public class NonExistingBook extends RuntimeException {
    public NonExistingBook(String message) {
        super(message);
    }
}
