package con.library.components.books.services;

import con.library.components.books.models.Book;
import con.library.services.IAbstractService;

import java.util.List;

public interface IBookService extends IAbstractService<Book,Long> {



    Book findByISBN(String isbn);

    boolean existByISBN(String isbn);

    boolean existsById(Long id);

    List<Book> findAllByTitle(String title);

    List<Book> findAllByAuthor(String author);
}
