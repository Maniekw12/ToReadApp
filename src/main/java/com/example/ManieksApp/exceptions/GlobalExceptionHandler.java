package com.example.ManieksApp.exceptions;

import com.example.ManieksApp.response.BaseResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NonExistingBook.class)
    public ResponseEntity<BaseResponse> handleNonExistingSwiftCode(HttpServletRequest req, Exception e) throws Exception {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new BaseResponse(e.getMessage()));
    }

    @ExceptionHandler(DuplicatedIdException.class)
    public ResponseEntity<BaseResponse> handleConflict(HttpServletRequest req, Exception e) throws Exception {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new BaseResponse(e.getMessage()));
    }

    @ExceptionHandler(EmptyDataException.class)
    public ResponseEntity<BaseResponse> emptyData(HttpServletRequest req, Exception e) throws Exception {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseResponse(e.getMessage()));
    }
}
