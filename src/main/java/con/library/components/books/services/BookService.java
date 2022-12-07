package con.library.components.books.services;


import con.library.components.books.models.Book;
import con.library.utils.CSVUtils;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class BookService implements IBookService {
    public final static String PATH = "data/books.csv";
    private static BookService instance;


    private BookService() {
    }

    public static BookService getInstance() {
        if (instance == null)
            instance = new BookService();
        return instance;
    }

    @Override
    public List<Book> findAll() {
        List<Book> books = new ArrayList<>();
        List<String> records = CSVUtils.read(PATH);
        for (String record : records) {
            books.add(Book.parse(record));
        }
        return books;
    }

    @Override
    public Book findById(Long id) {
        List<Book> books = findAll();
        for (Book book : books) {
            if (book.getId() == id)
                return book;
        }
        return null;
    }

    @Override
    public void add(Book newBook) {
        List<Book> books = findAll();
        newBook.setId(System.currentTimeMillis() / 1000);
        Instant now = Instant.now();
        newBook.setCreatedAt(now);
        newBook.setUpdatedAt(now);
        books.add(newBook);
        CSVUtils.write(PATH, books);
    }

    @Override
    public void update(Book newBook) {
        List<Book> books = findAll();
        for (Book book : books) {
            if (book.getId() == newBook.getId()) {
                String title = newBook.getTitle();
                if (title != null && !title.isEmpty())
                    book.setTitle(newBook.getTitle());

                String author = newBook.getAuthor();
                if (author != null)
                    book.setAuthor(newBook.getAuthor());

                String subject = newBook.getSubject();
                if (subject != null)
                    book.setSubject(newBook.getSubject());

                String language = newBook.getLanguage();
                if (language != null)
                    book.setLanguage(newBook.getLanguage());

                Instant createdAt = newBook.getCreatedAt();
                if (createdAt != null)
                    book.setCreatedAt(newBook.getCreatedAt());

                Instant updatedAt = newBook.getUpdatedAt();
                if (updatedAt != null)
                    book.setUpdatedAt(newBook.getUpdatedAt());

                book.setUpdatedAt(Instant.now());
                CSVUtils.write(PATH, books);
            }
        }

    }

    @Override
    public Book findByISBN(String isbn) {
        List<Book> books = findAll();
        for (Book book : books) {
            if (book.getIsbn().equals(isbn))
                return book;
        }
        return null;
    }

    @Override
    public boolean existByISBN(String isbn) {
        List<Book> books = findAll();
        for (Book book : books) {
            if (book.getIsbn().equals(isbn))
                return true;
        }
        return false;
    }

    @Override
    public void deleteById(Long id) {
        List<Book> books = findAll();
        books.removeIf(new Predicate<Book>() {
            @Override
            public boolean test(Book book) {
                return book.getId() == id;
            }
        });
        CSVUtils.write(PATH, books);
    }


    @Override
    public boolean existsById(Long id) {
        return findById(id) != null;
    }

    @Override
    public List<Book> findAllByTitle(String title) {
        List<Book> newBooks = new ArrayList<>();
        List<Book> books = findAll();
        for (Book book : books)
            if (book.getTitle().toLowerCase().contains(title.toLowerCase()))
                newBooks.add(book);
        return newBooks;
    }

    @Override
    public List<Book> findAllByAuthor(String author) {
        List<Book> newBooks = new ArrayList<>();
        List<Book> books = findAll();
        for (Book book : books)
            if (book.getAuthor().toLowerCase().contains(author.toLowerCase()))
                newBooks.add(book);
        return newBooks;
    }
}
