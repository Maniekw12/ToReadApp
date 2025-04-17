package com.example.ManieksApp.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OneBookResponse {
    private Long id;
    private int priority;
    private String name;
    private String author;
    private int pages;
    private int readPages;
    private boolean read;
}
