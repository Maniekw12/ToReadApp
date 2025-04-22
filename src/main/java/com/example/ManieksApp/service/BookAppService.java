package com.example.ManieksApp.service;

import com.example.ManieksApp.entity.BookEntity;
import com.example.ManieksApp.exceptions.DuplicatedIdException;
import com.example.ManieksApp.exceptions.NonExistingBook;
import com.example.ManieksApp.mapper.BookMapper;
import com.example.ManieksApp.repository.BooksRepository;
import com.example.ManieksApp.request.CreateNewBook;
import com.example.ManieksApp.response.BooksRespone;
import com.example.ManieksApp.response.BaseResponse;
import com.example.ManieksApp.response.OneBookResponse;
import com.example.ManieksApp.util.ValidateData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookAppService {
    @Autowired
    BooksRepository booksRepository;

    public BaseResponse addBook(CreateNewBook newBook) {
        ValidateData.validateAuthor(newBook.getAuthor());
        ValidateData.validateName(newBook.getName());
        validateNameAndAuthor(newBook);

        int maxPriority = 0;
        Optional<BookEntity> maxPriorityEntity = booksRepository.findTopByReadFalseOrderByPriorityDesc();
        if(maxPriorityEntity.isPresent()) {
            maxPriority = maxPriorityEntity.get().getPriority();
        }

        BookEntity bookToAdd = BookMapper.mapToEntity(newBook,maxPriority);

        increasePrioritiesAfterInsert(bookToAdd.getPriority());
        booksRepository.save(bookToAdd);

        return new BaseResponse("Book added successfully");
    }

    public OneBookResponse getOneBook(Long id) {
        validateBookExistence(id);
        Optional<BookEntity> entityOptional = booksRepository.findById(id);
        if (entityOptional.isPresent()) {
            BookEntity entity = entityOptional.get();
            return BookMapper.mapToOneBookResponse(entity);
        }
        return new OneBookResponse();
    }

    public BooksRespone getAllBooksFromDataBase() {
        ArrayList<BookEntity> allBooks = (ArrayList<BookEntity>) booksRepository.findAll();
        ArrayList<OneBookResponse> bookResponses = new ArrayList<>();

        for (BookEntity book : allBooks) {
            OneBookResponse bookResponse = BookMapper.mapToOneBookResponse(book);
            bookResponses.add(bookResponse);
        }

        return BooksRespone.builder().
                responses(bookResponses)
                .build();
    }

    public BooksRespone getBooksByReadStatus(String readStatus) {
        ArrayList<BookEntity> allBooks = (ArrayList<BookEntity>) booksRepository.findAll();
        ArrayList<OneBookResponse> booksByStatus = new ArrayList<>();

        switch (readStatus) {
            case "y":
                booksByStatus = (ArrayList<OneBookResponse>) allBooks.stream()
                        .map(x -> BookMapper.mapToOneBookResponse(x))
                        .filter(x -> x.isRead())
                        .collect(Collectors.toList());
                break;

            case "n":
                booksByStatus = (ArrayList<OneBookResponse>) allBooks.stream()
                        .map(x -> BookMapper.mapToOneBookResponse(x))
                        .filter(x -> !x.isRead())
                        .collect(Collectors.toList());
                break;
        }

        booksByStatus.sort(Comparator.comparingInt(OneBookResponse::getPriority));

        return BooksRespone.builder().
                responses(booksByStatus)
                .build();
    }

    public BaseResponse removeBook(Long id) {
        validateBookExistence(id);

        BookEntity bookToRemove = booksRepository.findById(id).get();
        int bookToRemovePriority = bookToRemove.getPriority();

        booksRepository.deleteById(id);

        lowerPrioritiesAfterInsert(bookToRemovePriority);

        return new BaseResponse("Book removed successfully");
    }

    //TODO change the logic to creating new sorted list and then inserting element between some of them
    public BaseResponse updateBook(Long id, CreateNewBook newBook) {
        ValidateData.validateAuthor(newBook.getAuthor());
        ValidateData.validateName(newBook.getName());
        validateBookExistence(id);

        return new BaseResponse("Book successfully updated");
    }

    private void rebuildUnreadBookPriorities() {
        // Get all unread books ordered by their current priority
        List<BookEntity> unreadBooks = booksRepository.findByReadFalseOrderByPriority();

        // Reassign priorities sequentially starting from 1
        int priority = 1;
        for (BookEntity book : unreadBooks) {
            book.setPriority(priority++);
        }

        // Save all updated books
        booksRepository.saveAll(unreadBooks);
    }

    private void lowerPrioritiesAfterInsert(int priority) {
        List<BookEntity> booksWithGreaterPriority = booksRepository.findByPriorityGreaterThan(priority);

        booksWithGreaterPriority = booksWithGreaterPriority.stream()
                .filter(x -> !x.isRead())
                .collect(Collectors.toList());

        for (BookEntity book : booksWithGreaterPriority) {
            book.setPriority(book.getPriority() - 1);
        }

        booksRepository.saveAll(booksWithGreaterPriority);
    }

    private void increasePrioritiesAfterInsert(int priority) {
        List<BookEntity> booksWithGreaterPriority = booksRepository.findByPriorityGreaterThanEqual(priority);

        booksWithGreaterPriority = booksWithGreaterPriority.stream()
                .filter(x -> !x.isRead())
                .collect(Collectors.toList());

        for (BookEntity book : booksWithGreaterPriority) {
            book.setPriority(book.getPriority() + 1);
        }

        booksRepository.saveAll(booksWithGreaterPriority);
    }

    private void validateBookExistence(Long id) {
        if (!booksRepository.existsById(id)) {
            throw new NonExistingBook("Book with id " + id + " does not exist");
        }
    }

    private void validateNameAndAuthor(CreateNewBook newBook) {
        if(booksRepository.existsByName(newBook.getName().trim()) && booksRepository.existsByAuthor(newBook.getAuthor().trim())) {
            throw new DuplicatedIdException("Book with name " + newBook.getName().trim() + " and author "+ newBook.getAuthor().trim() + "already exists");
        }
    }

}
