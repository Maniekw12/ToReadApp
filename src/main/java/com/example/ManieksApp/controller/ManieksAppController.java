package com.example.ManieksApp.controller;

import com.example.ManieksApp.request.CreateNewBook;
import com.example.ManieksApp.response.BaseResponse;
import org.springframework.web.bind.annotation.*;

@RestController
public class ManieksAppController {

    @GetMapping("/books")
    public BaseResponse getAllBooks(){
        return new BaseResponse("Okk");
    }

    @GetMapping("/books/{id}")
    public BaseResponse getOneBook(@PathVariable Long id){
        return new BaseResponse("Okk");
    }

    @DeleteMapping("/books/delete/{id}")
    public BaseResponse deleteOneBook(@PathVariable Long id){
        return new BaseResponse("Okk");
    }

    @PostMapping("/books/post")
    public BaseResponse postOneBook(@RequestBody CreateNewBook newBook){
        return new BaseResponse("Okk");
    }

    @PutMapping("/books/update")
    public BaseResponse updateOneBook(@RequestBody CreateNewBook newBook){
        return new BaseResponse("Okk");
    }
}
