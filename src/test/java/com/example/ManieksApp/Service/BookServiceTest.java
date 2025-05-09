package com.example.ManieksApp.Service;

import com.example.ManieksApp.entity.BookEntity;
import com.example.ManieksApp.mapper.BookMapper;
import com.example.ManieksApp.repository.BooksRepository;
import com.example.ManieksApp.request.CreateNewBook;
import com.example.ManieksApp.response.BaseResponse;
import com.example.ManieksApp.service.BookAppService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    @InjectMocks
    private BookAppService bookAppService;

    @Mock
    private BooksRepository booksRepository;

    @Test
    public void shouldRemoveBookSuccessfully() {
        // given
        Long bookId = 1L;
        BookEntity bookEntity = BookEntity.builder()
                .id(bookId)
                .name("Clean Code")
                .author("Robert C. Martin")
                .priority(1)
                .pages(500)
                .readPages(20)
                .read(false)
                .build();

        when(booksRepository.existsById(bookId)).thenReturn(true);
        when(booksRepository.findById(bookId)).thenReturn(Optional.of(bookEntity));

        // when
        BaseResponse response = bookAppService.removeBook(bookId);

        // then
        Assertions.assertNotNull(response);
        verify(booksRepository).deleteById(bookId);
    }


}
