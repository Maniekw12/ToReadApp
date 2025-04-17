package com.example.ManieksApp.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@Entity
@Table(name = "item",indexes = {
        @Index(name = "idx_name", columnList = "name"),
        @Index(name = "idx_author", columnList = "author")
})
@NoArgsConstructor
@AllArgsConstructor
public class BookEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int priority;
    private String name;
    private String author;
    private int pages;
    private int readPages;
    private boolean read;
}
