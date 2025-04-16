package com.example.ManieksApp.controller;

import com.example.ManieksApp.request.CreateNewBook;
import com.example.ManieksApp.response.AllBooksRespone;
import com.example.ManieksApp.response.BaseResponse;
import com.example.ManieksApp.response.OneBookResponse;
import com.example.ManieksApp.service.BookAppService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:8000")
public class ManieksAppController {

    @Autowired
    BookAppService bookAppService;

    @GetMapping("/books")
    public AllBooksRespone getAllBooks(){
        return bookAppService.getAllBooksFromDataBase();
    }

    @GetMapping("/books/{id}")
    public OneBookResponse getOneBook(@PathVariable Long id){
        return bookAppService.getOneBook(id);
    }

    @DeleteMapping("/books/delete/{id}")
    public BaseResponse deleteOneBook(@PathVariable Long id){
        return new BaseResponse("Okk");
    }

    @PostMapping("/post")
    public BaseResponse postOneBook(@RequestBody CreateNewBook newBook){
        return bookAppService.addBook(newBook);
    }

    @PutMapping("/books/update")
    public BaseResponse updateOneBook(@RequestBody CreateNewBook newBook){
        return new BaseResponse("Okk");
    }
}
