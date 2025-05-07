package com.example.ManieksApp.mapper;

import com.example.ManieksApp.entity.BookEntity;
import com.example.ManieksApp.request.CreateNewBook;
import com.example.ManieksApp.response.OneBookResponse;
import org.junit.jupiter.api.Test;

import java.awt.print.Book;

import static org.junit.jupiter.api.Assertions.*;

public class MapperTest {

    @Test
    void ShouldMapToEntityWithNewBookCreateNewEntity() {
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
    void ShouldMapToEntityWithNewBookCreateNewEntityWhenRead() {
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

    @Test
    void shouldMapCreateNewBookToBookEntity_WithUnfinishedBookAndCorrectedPriority() {
        //given
        CreateNewBook dto = CreateNewBook.builder()
                .name("Unfinished Book")
                .author("Author C")
                .priority(10)
                .pages(250)
                .readPages(50)
                .build();

        //when
        //then
        BookEntity entity = BookMapper.mapToEntity(dto,5);
        assertEquals(6, entity.getPriority());
        assertFalse(entity.isRead());
    }

    @Test
    void shouldMapCreateNewBookToExistingEntityWithNegativePriority(){
        //given
        CreateNewBook dto = CreateNewBook.builder()
                .name("Updated Name")
                .author("New Author")
                .priority(-5)
                .pages(150)
                .readPages(-20)
                .build();
        BookEntity existingEntity = new BookEntity();
        existingEntity.setId(1L);
        //when
        BookEntity updated = BookMapper.mapToEntity(dto, 3, existingEntity);
        //then
        assertEquals("Updated Name", updated.getName());
        assertEquals("New Author", updated.getAuthor());
        assertEquals(1, updated.getPriority());
        assertEquals(0, updated.getReadPages());
        assertFalse(updated.isRead());
    }

    @Test
    void shouldMapBookEntityToOneBookResponse(){
        //given
        BookEntity entity = BookEntity.builder()
                .id(123L)
                .name("Mapped Book")
                .author("Author D")
                .priority(4)
                .pages(220)
                .read(true)
                .readPages(220)
                .build();
        //when
        OneBookResponse response = BookMapper.mapToOneBookResponse(entity);

        //then
        assertEquals(123L, response.getId());
        assertEquals("Mapped Book", response.getName());
        assertEquals("Author D", response.getAuthor());
        assertEquals(4, response.getPriority());
        assertEquals(220, response.getPages());
        assertEquals(220, response.getReadPages());
        assertTrue(response.isRead());
    }

    @Test
    void shouldMapOneBookResponseToBookEntity(){
        OneBookResponse response = OneBookResponse.builder()
                .id(999L)
                .name("Response Book")
                .author("Author E")
                .priority(3)
                .pages(180)
                .read(false)
                .readPages(100)
                .build();

        BookEntity entity = BookMapper.mapToEntity(response);

        assertEquals(999L, entity.getId());
        assertEquals("Response Book", entity.getName());
        assertEquals("Author E", entity.getAuthor());
        assertEquals(3, entity.getPriority());
        assertEquals(180, entity.getPages());
        assertEquals(100, entity.getReadPages());
        assertFalse(entity.isRead());
    }

    @Test
    void shouldCorrectNegativeReadPagesToZero(){
        //given
        CreateNewBook dto = CreateNewBook.builder()
                .name("Ne;gative ReadPages")
                .author("Author X")
                .priority(2)
                .pages(100)
                .readPages(-10)
                .build();
        //when
        BookEntity entity = BookMapper.mapToEntity(dto,3);
        //then
        assertEquals(0, entity.getReadPages());
        assertFalse(entity.isRead());
    }

    @Test
    void shouldSetPriorityToOneIfLessThanOne() {
        CreateNewBook dto = CreateNewBook.builder()
                .name("Low Priority Book")
                .author("Author Y")
                .priority(0) // invalid priority
                .pages(120)
                .readPages(30)
                .build();

        BookEntity entity = BookMapper.mapToEntity(dto, 10);

        assertEquals(1, entity.getPriority());
    }

    @Test
    void shouldCapReadPagesToPagesWhenExceedingTotal() {
        // given
        CreateNewBook dto = CreateNewBook.builder()
                .name("Too Much Reading")
                .author("Reader")
                .priority(3)
                .pages(100)
                .readPages(200) // too much
                .build();

        // when
        BookEntity result = BookMapper.mapToEntity(dto, 10);

        // then
        assertEquals(100, result.getReadPages());
        assertTrue(result.isRead());
        assertEquals(Integer.MAX_VALUE, result.getPriority());
    }

    @Test
    void shouldSetReadTrueWhenReadPagesEqualPagesExactly() {
        // given
        CreateNewBook dto = CreateNewBook.builder()
                .name("Exact Read")
                .author("Precise Reader")
                .priority(2)
                .pages(200)
                .readPages(200)
                .build();

        // when
        BookEntity result = BookMapper.mapToEntity(dto, 10);

        // then
        assertTrue(result.isRead());
        assertEquals(200, result.getReadPages());
        assertEquals(Integer.MAX_VALUE, result.getPriority());
    }

}
