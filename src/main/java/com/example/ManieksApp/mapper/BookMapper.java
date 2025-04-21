package com.example.ManieksApp.mapper;

import com.example.ManieksApp.entity.BookEntity;
import com.example.ManieksApp.repository.BooksRepository;
import com.example.ManieksApp.request.CreateNewBook;
import com.example.ManieksApp.response.OneBookResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

public class BookMapper {

    public static BookEntity mapToEntity(CreateNewBook NewBook, int maxPriority) {
        boolean isRead = false;
        int readPages = NewBook.getReadPages();
        int priority = NewBook.getPriority();

        if(priority > maxPriority) {
            priority = maxPriority+1;
        }

        if(readPages >= NewBook.getPages()) {
            isRead = true;
            priority = Integer.MAX_VALUE;
            readPages = NewBook.getPages();
        }
        else if(readPages < 0) {
            readPages = 0;
        }

        return BookEntity.builder()
                .name(NewBook.getName())
                .author(NewBook.getAuthor())
                .priority(priority)
                .pages(NewBook.getPages())
                .read(isRead)
                .readPages(readPages)
                .build();
    }

    public static OneBookResponse mapToOneBookResponse(BookEntity bookEntity) {
        return OneBookResponse.builder()
                .id(bookEntity.getId())
                .author(bookEntity.getAuthor())
                .priority(bookEntity.getPriority())
                .pages(bookEntity.getPages())
                .name(bookEntity.getName())
                .read(bookEntity.isRead())
                .readPages(bookEntity.getReadPages())
                .build();
    }

    public static BookEntity mapToEntity(OneBookResponse NewBook) {
        return BookEntity.builder()
                .author(NewBook.getAuthor())
                .priority(NewBook.getPriority())
                .pages(NewBook.getPages())
                .name(NewBook.getName())
                .read(NewBook.isRead())
                .readPages(NewBook.getReadPages())
                .id(NewBook.getId())
                .build();
    };

}
