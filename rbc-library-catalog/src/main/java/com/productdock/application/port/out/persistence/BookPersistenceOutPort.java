package com.productdock.application.port.out.persistence;

import com.productdock.domain.Book;

import java.util.Optional;

public interface BookPersistenceOutPort {

    Optional<Book> findById(Long bookId);

    Optional<Book> findByTitleAndAuthor(String title, String author);

    Book save(Book book);

    void deleteById(Long bookId);
}
