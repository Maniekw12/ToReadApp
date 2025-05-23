package com.example.ManieksApp.repository;

import com.example.ManieksApp.entity.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BooksRepository extends JpaRepository<BookEntity, Long> {

    Optional<BookEntity> findById(Long id);

    Optional<BookEntity> findByNameAndAuthor(String name, String author);

    List<BookEntity> findAll();

    boolean existsByName(String name);

    boolean existsByAuthor(String author);

    List<BookEntity> findByPriorityGreaterThanEqual(int priority);

    Optional<BookEntity> findTopByReadFalseOrderByPriorityDesc();

    List<BookEntity> findByPriorityGreaterThan(int priority);

    List<BookEntity> findByReadFalseOrderByPriority();

}
