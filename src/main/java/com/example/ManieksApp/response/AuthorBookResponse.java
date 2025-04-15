package com.example.ManieksApp.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class AuthorBookResponse {
    private String author;
    private List<OneBookResponse> responses;
}
