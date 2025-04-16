package com.example.ManieksApp.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateNewBook {
    String name;
    String author;
    String genre;
    String description;
    double rating;
}
