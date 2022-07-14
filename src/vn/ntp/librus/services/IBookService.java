package vn.ntp.librus.services;

import vn.ntp.librus.model.Book;

import java.util.List;

public interface IBookService extends IAbstractService<Book,Long>{

    void add(Book newBook);

    void update(Book newBook);

    Book findByISBN(String isbn);

    boolean existByISBN(String isbn);

    void deleteById(long id);

    boolean existsById(long id);

    List<Book> findAllByTitle(String title);

    List<Book> findAllByAuthor(String author);
}
