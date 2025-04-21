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

        int maxPriority = booksRepository.findTopByOrderByPriorityDesc().getPriority();
        BookEntity bookToAdd = BookMapper.mapToEntity(newBook,maxPriority);
        ArrayList<BookEntity> booksWithGreaterPriority = (ArrayList<BookEntity>) booksRepository.findByPriorityGreaterThanEqual(bookToAdd.getPriority());

        //TODO make it a method
        for (BookEntity book : booksWithGreaterPriority) {
            book.setPriority(book.getPriority() + 1);
        }

        booksRepository.save(bookToAdd);
        booksRepository.saveAll(booksWithGreaterPriority);
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
        boolean isRead = bookToRemove.isRead();

        booksRepository.deleteById(id);
        if(!isRead){
            ArrayList<BookEntity> booksWithGreaterPriority = (ArrayList<BookEntity>) booksRepository.findByPriorityGreaterThanEqual(bookToRemovePriority);
            //TODO make it a method
            for (BookEntity book : booksWithGreaterPriority) {
                book.setPriority(book.getPriority() - 1);
            }
            booksRepository.saveAll(booksWithGreaterPriority);
        }

        return new BaseResponse("Book removed successfully");
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
