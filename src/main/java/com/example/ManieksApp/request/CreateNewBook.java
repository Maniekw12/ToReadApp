package com.example.ManieksApp.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateNewBook {
    String name;
    String author;
    String genre;
    String description;
    double rating;
}
