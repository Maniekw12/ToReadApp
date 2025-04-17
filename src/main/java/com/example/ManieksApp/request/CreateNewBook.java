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
    private int priority;
    private String name;
    private String author;
    private int pages;
    private int readPages;
    //private boolean read;
}
