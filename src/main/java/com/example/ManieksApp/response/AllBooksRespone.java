package com.example.ManieksApp.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AllBooksRespone {
    private List<OneBookResponse> responses;

}
