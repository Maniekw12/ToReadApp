package com.example.ManieksApp.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OneBookResponse {
    String name;
    String author;
    String genre;
    String description;
    double rating;
}
