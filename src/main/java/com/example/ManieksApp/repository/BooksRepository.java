package com.example.ManieksApp.repository;

import com.example.ManieksApp.entity.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BooksRepository extends JpaRepository<BookEntity, Long> {


}
