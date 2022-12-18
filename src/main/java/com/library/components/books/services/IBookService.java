package com.library.components.books.services;

import com.library.components.books.models.Book;
import com.library.services.IAbstractService;

import java.util.List;

public interface IBookService extends IAbstractService<Book,Long> {



    Book findByISBN(String isbn);

    boolean existByISBN(String isbn);

    boolean existsById(Long id);

    List<Book> findAllByTitle(String title);

    List<Book> findAllByAuthor(String author);
}
