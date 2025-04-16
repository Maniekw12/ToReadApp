package com.example.ManieksApp.service;

import com.example.ManieksApp.entity.BookEntity;
import com.example.ManieksApp.exceptions.NonExistingBook;
import com.example.ManieksApp.mapper.BookMapper;
import com.example.ManieksApp.repository.BooksRepository;
import com.example.ManieksApp.request.CreateNewBook;
import com.example.ManieksApp.response.AllBooksRespone;
import com.example.ManieksApp.response.BaseResponse;
import com.example.ManieksApp.response.OneBookResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.print.Book;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class BookAppService {
    @Autowired
    BooksRepository booksRepository;

    public BaseResponse addBook(CreateNewBook book) {
        BookEntity bookToAdd = BookMapper.mapToEntity(book);

        booksRepository.save(bookToAdd);
        return new BaseResponse("Book added successfully");
    }

    public OneBookResponse getOneBook(Long id) {
        ValidateBookExistence(id);
        Optional<BookEntity> entityOptional = booksRepository.findById(id);
        if (entityOptional.isPresent()) {
            BookEntity entity = entityOptional.get();
            return BookMapper.mapToOneBookResponse(entity);
        }
        return new OneBookResponse();
    }

    public AllBooksRespone getAllBooksFromDataBase() {
        ArrayList<BookEntity> allBooks = (ArrayList<BookEntity>) booksRepository.findAll();
        ArrayList<OneBookResponse> bookResponses = new ArrayList<>();

        for (BookEntity book : allBooks) {
            OneBookResponse bookResponse = BookMapper.mapToOneBookResponse(book);
            bookResponses.add(bookResponse);
        }

        return AllBooksRespone.builder().
                responses(bookResponses)
                .build();
    }

    private void ValidateBookExistence(Long id) {
        if (!booksRepository.existsById(id)) {
            throw new NonExistingBook("Book with id " + id + " does not exist");
        }
    }

}
