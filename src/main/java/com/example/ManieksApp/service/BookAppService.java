package com.example.ManieksApp.service;

import com.example.ManieksApp.entity.BookEntity;
import com.example.ManieksApp.exceptions.DuplicatedIdException;
import com.example.ManieksApp.exceptions.NonExistingBook;
import com.example.ManieksApp.mapper.BookMapper;
import com.example.ManieksApp.repository.BooksRepository;
import com.example.ManieksApp.request.CreateNewBook;
import com.example.ManieksApp.request.UpdatePages;
import com.example.ManieksApp.response.BooksRespone;
import com.example.ManieksApp.response.BaseResponse;
import com.example.ManieksApp.response.OneBookResponse;
import com.example.ManieksApp.util.ValidateData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
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

    public BaseResponse updateBook(Long id, CreateNewBook newBook) {
        ValidateData.validateAuthor(newBook.getAuthor());
        ValidateData.validateName(newBook.getName());
        validateBookExistence(id);

        BookEntity existingBook = booksRepository.findById(id).get();
        boolean wasRead = existingBook.isRead();

        int maxPriority = booksRepository.findTopByReadFalseOrderByPriorityDesc()
                .map(BookEntity::getPriority)
                .orElse(0);

        BookMapper.mapToEntity(newBook, maxPriority, existingBook);
        boolean isNowRead = existingBook.isRead();

        if (wasRead && !isNowRead) {
            List<BookEntity> unread = booksRepository.findByReadFalseOrderByPriority();
            int desiredPriority = existingBook.getPriority();
            if (desiredPriority < 1) desiredPriority = 1;
            if (desiredPriority > unread.size()) desiredPriority = unread.size() + 1;

            unread.add(desiredPriority - 1, existingBook);
            reorderUnreadBooks(unread);

        } else if (!wasRead && isNowRead) {
            List<BookEntity> unread = booksRepository.findByReadFalseOrderByPriority();
            unread.remove(existingBook);
            reorderUnreadBooks(unread);
            existingBook.setPriority(Integer.MAX_VALUE);

        } else if (!isNowRead) {
            List<BookEntity> unread = booksRepository.findByReadFalseOrderByPriority();
            unread.remove(existingBook);
            int newPriority = existingBook.getPriority();
            if (newPriority < 1) newPriority = 1;
            if (newPriority > unread.size()) newPriority = unread.size() + 1;
            unread.add(newPriority - 1, existingBook);
            reorderUnreadBooks(unread);
        }

        booksRepository.save(existingBook);
        return new BaseResponse("Book successfully updated");
    }

    public BaseResponse updatePages(UpdatePages updatedPages) {
        validateBookExistence(updatedPages.getId());
        BookEntity BookToUpdate = booksRepository.findById(updatedPages.getId()).get();
        BookEntity BookAfterUpdatedPages = adjustBookPagesAndPriority(BookToUpdate, updatedPages.getReadPages());
        booksRepository.save(BookAfterUpdatedPages);
        return new BaseResponse("Book successfully updated");
    }

    private BookEntity adjustBookPagesAndPriority(BookEntity book, int updatedNumberOfPages) {
        updatedNumberOfPages = Math.max(0, Math.min(updatedNumberOfPages, book.getPages()));
        boolean wasRead = book.isRead();

        book.setReadPages(updatedNumberOfPages);

        if (updatedNumberOfPages >= book.getPages()) {
            if (!wasRead) {
                lowerPrioritiesAfterInsert(book.getPriority());
            }
            book.setRead(true);
            book.setPriority(Integer.MAX_VALUE);

        } else {
            if (wasRead) {
                int maxPriority = booksRepository.findTopByReadFalseOrderByPriorityDesc()
                        .map(BookEntity::getPriority).orElse(0);
                book.setPriority(maxPriority + 1);
            }
            book.setRead(false);
        }

        return book;
    }


    private void reorderUnreadBooks(List<BookEntity> books) {
        int priority = 1;
        for (BookEntity book : books) {
            book.setPriority(priority++);
        }
        booksRepository.saveAll(books);
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
