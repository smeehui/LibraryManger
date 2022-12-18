package com.library.components.books.services;

import com.library.components.books.models.Book;
import com.library.components.books.models.BookFormat;
import com.library.components.books.models.BookItem;
import com.library.components.books.models.BookStatus;
import com.library.utils.CSVUtils;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class BookItemService implements IBookItemService {
    public final static String PATH = "data/book-items.csv";
    private static BookItemService instance;
    private final IBookService bookService;

    private BookItemService() {
        bookService = BookService.getInstance();
    }

    public static BookItemService getInstance() {
        if (instance == null)
            instance = new BookItemService();
        return instance;
    }

    @Override
    public List<BookItem> findAll() {
        List<BookItem> books = new ArrayList<>();
        List<String> records = CSVUtils.read(PATH);
        for (String record : records) {
            BookItem bookItem = BookItem.parse(record);
            long bookId = bookItem.getBookId();
            Book book = bookService.findById(bookId);
            bookItem.setBook(book);
            books.add(bookItem);
        }
        return books;
    }


    @Override
    public BookItem findById(Long id) {
        List<BookItem> bookItems = findAll();
        for (BookItem bookItem : bookItems) {
            if (bookItem.getBookItemID() == id) {
                long bookId = bookItem.getBookId();
                Book book = bookService.findById(bookId);
                bookItem.setBook(book);
                return bookItem;
            }
        }
        return null;
    }

    @Override
    public boolean existsById(Long id) {
        return findById(id) != null;
    }



    @Override
    public void add(BookItem newBookItem) {
        List<BookItem> bookItems = findAll();
        newBookItem.setBookItemID(System.currentTimeMillis() / 1000);
        Instant now = Instant.now();
        newBookItem.setStatus(BookStatus.AVAILABLE);
        newBookItem.setDateOfPurchase(now);
        bookItems.add(newBookItem);
        CSVUtils.write(PATH, bookItems);
    }

    @Override
    public void update(BookItem newBookItem) {
        List<BookItem> bookItems = findAll();
        for (BookItem book : bookItems) {
            if (book.getBookItemID() == newBookItem.getBookItemID()) {
                String publisher = newBookItem.getPublisher();
                if (publisher != null && !publisher.isEmpty())
                    book.setPublisher(newBookItem.getPublisher());

                Integer publicationAt = newBookItem.getPublicationAt();
                if (publicationAt != null)
                    book.setPublicationAt(newBookItem.getPublicationAt());

                double price = newBookItem.getPrice();
                if (price != 0)
                    book.setPrice(newBookItem.getPrice());

                BookFormat format = newBookItem.getFormat();
                if (format != null)
                    book.setFormat(newBookItem.getFormat());

                BookStatus status = newBookItem.getStatus();
                if (status != null)
                    book.setStatus(newBookItem.getStatus());

                Instant dueAt = newBookItem.getDueAt();
                if (dueAt != null)
                    book.setDueAt(newBookItem.getDueAt());

                Instant borrowedAt = newBookItem.getBorrowedAt();
                if (borrowedAt != null)
                    book.setBorrowedAt(newBookItem.getBorrowedAt());

                CSVUtils.write(PATH, bookItems);

            }
        }
    }

    @Override
    public void deleteById(Long id) {

    }

}
