package com.example.ManieksApp.controller;

import com.example.ManieksApp.repository.BooksRepository;
import com.example.ManieksApp.request.CreateNewBook;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ManieksAppControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BooksRepository booksRepository;

    @Test
    void shouldCreateNewBookSuccessfully() throws Exception {
        // given
        CreateNewBook request = CreateNewBook.builder()
                .name("Clean Code")
                .author("Robert C. Martin")
                .priority(1)
                .pages(500)
                .readPages(20)
                .build();

        //when
        //then
        mockMvc.perform(post("/newBook")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldCreateNewBookWithLackOfData() throws Exception {
        // given
        CreateNewBook request = CreateNewBook.builder()
                .name("Clean Code")
                .priority(1)
                .pages(500)
                .readPages(20)
                .build();

        //when
        //then
        mockMvc.perform(post("/newBook")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldGetAllBooksSuccessfully() throws Exception {
        //when
        //then
        mockMvc.perform(get("/books")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetBookByIdUnsuccessfully() throws Exception {
        // zakładamy, że książka o ID = 1 istnieje
        mockMvc.perform(get("/book/{id}", 1000L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateAndDeleteBookSuccessfully() throws Exception {
        // given
        CreateNewBook request = CreateNewBook.builder()
                .name("Clean Codje")
                .author("Robert C. Martin")
                .priority(1)
                .pages(500)
                .readPages(20)
                .build();

        // when
        mockMvc.perform(post("/newBook")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        MvcResult getResult = mockMvc.perform(get("/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseJson = getResult.getResponse().getContentAsString();
        Long id = extractIdFromResponse(responseJson); // np. {"message": "Book added successfully"}

        // then
        mockMvc.perform(delete("/books/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


    private Long extractIdFromResponse(String jsonResponse) throws Exception {
        JsonNode jsonNode = objectMapper.readTree(jsonResponse);
        JsonNode booksNode = jsonNode.path("responses");

        for (JsonNode book : booksNode) {
            if (book.path("name").asText().equals("Clean Codje") &&
                    book.path("author").asText().equals("Robert C. Martin")) {
                return book.path("id").asLong();
            }
        }

        return null;
    }



}
