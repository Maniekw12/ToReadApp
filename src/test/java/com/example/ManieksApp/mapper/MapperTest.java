package com.example.ManieksApp.mapper;

import com.example.ManieksApp.entity.BookEntity;
import com.example.ManieksApp.request.CreateNewBook;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class MapperTest {

    @Test
    void mapToEntityWithNewBookShouldCreateNewEntity() {
        // given
        CreateNewBook newBook = CreateNewBook.builder()
                .name("Wiedźmin")
                .author("Andrzej Sapkowski")
                .pages(320)
                .readPages(100)
                .priority(2)
                .build();
        int maxPriority = 5;

        // when
        BookEntity result = BookMapper.mapToEntity(newBook, maxPriority);

        // then
        assertEquals("Wiedźmin", result.getName());
        assertEquals("Andrzej Sapkowski", result.getAuthor());
        assertEquals(320, result.getPages());
        assertEquals(100, result.getReadPages());
        assertEquals(2, result.getPriority());
        assertFalse(result.isRead());
    }

    @Test
    void mapToEntityWithNewBookShouldCreateNewEntityWhenRead() {
        // given
        CreateNewBook newBook = CreateNewBook.builder()
                .name("Wiedźmin")
                .author("Andrzej Sapkowski")
                .pages(320)
                .readPages(1000)
                .priority(2)
                .build();
        int maxPriority = 5;

        // when
        BookEntity result = BookMapper.mapToEntity(newBook, maxPriority);

        // then
        assertEquals("Wiedźmin", result.getName());
        assertEquals("Andrzej Sapkowski", result.getAuthor());
        assertEquals(320, result.getPages());
        assertEquals(320, result.getReadPages());
        assertEquals(2, result.getPriority());
        assertFalse(result.isRead());
    }

}
