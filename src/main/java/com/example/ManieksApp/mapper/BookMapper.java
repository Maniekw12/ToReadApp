package com.example.ManieksApp.mapper;

import com.example.ManieksApp.entity.BookEntity;
import com.example.ManieksApp.request.CreateNewBook;
import com.example.ManieksApp.response.AllBooksRespone;
import com.example.ManieksApp.response.OneBookResponse;

import javax.swing.text.html.parser.Entity;
import java.awt.print.Book;
import java.util.List;

public class BookMapper {

    public static BookEntity mapToEntity(CreateNewBook NewBook) {
        return BookEntity.builder()
                .name(NewBook.getName())
                .genre(NewBook.getGenre())
                .description(NewBook.getDescription())
                .author(NewBook.getAuthor())
                .rating(NewBook.getRating())
                .build();
    }

    public static OneBookResponse mapToOneBookResponse(BookEntity bookEntity) {
        return OneBookResponse.builder()
                .id(bookEntity.getId())
                .author(bookEntity.getAuthor())
                .genre(bookEntity.getGenre())
                .description(bookEntity.getDescription())
                .rating(bookEntity.getRating())
                .name(bookEntity.getName())
                .build();
    }


}
