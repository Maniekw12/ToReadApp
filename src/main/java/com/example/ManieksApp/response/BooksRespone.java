package com.example.ManieksApp.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BooksRespone {
    private List<OneBookResponse> responses;

}
