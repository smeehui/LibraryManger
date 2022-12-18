package com.library.components.booklending.services;

import com.library.components.books.services.IBookItemService;
import com.library.services.Constants;
import com.library.components.books.models.BookItem;
import com.library.components.booklending.model.BookLending;
import com.library.components.books.models.BookStatus;
import com.library.components.booklending.model.LendingStatus;
import com.library.components.books.services.BookItemService;
import com.library.components.fine.services.IFineService;
import com.library.utils.CSVUtils;

import java.time.Instant;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

public class BookLendingService implements IBookLendingService {
    public final static String PATH = "data/book-lending.csv";
    private IBookItemService bookItemService;
    private IFineService fineService;

    private static BookLendingService instance;

    private BookLendingService() {
        bookItemService = BookItemService.getInstance();
    }

    public static BookLendingService getInstance() {
        if (instance == null)
            instance = new BookLendingService();
        return instance;
    }

    @Override
    public List<BookLending> findAll() {
        List<BookLending> bookLendingList = new ArrayList<>();
        List<String> records = CSVUtils.read(PATH);
        for (String record : records) {
            bookLendingList.add(BookLending.parse(record));
        }
        return bookLendingList;
    }


    @Override
    public BookLending findById(Long id) {
        List<BookLending> bookLendingList = findAll();
        for (BookLending BookLending : bookLendingList) {
            if (BookLending.getId() == id)
                return BookLending;
        }
        return null;
    }

    public BookLending findByUserId(long userId) {
        List<BookLending> bookLendingList = findAll();
        for (BookLending bookLending : bookLendingList) {
            if (bookLending.getUserId() == userId)
                return bookLending;
        }
        return null;
    }

    @Override
    public List<BookLending> findBookLendingByUserId(long userId) {
        List<BookLending> bookLendingList = findAll();
        List<BookLending> result = new ArrayList<>();
        for (BookLending bookLending : bookLendingList) {
            if (bookLending.getUserId() == userId) {
                result.add(bookLending);
            }
        }return result;
    }

    @Override
    public List<BookLending> findBookLendingByUserIdAndStatus(long userId, LendingStatus lending) {
        List<BookLending> bookLendingList = findAll();
        List<BookLending> result = new ArrayList<>();
        for (BookLending bookLending : bookLendingList) {
            if (bookLending.getUserId() == userId && bookLending.getStatus().equals(LendingStatus.LENDING)) {
                result.add(bookLending);
            }
        }
        return result;
    }

    @Override
    public boolean isBookIssuedQuotaExceeded(long userId) {
        return countBookItemLendingByUserIdAndStatus(userId) >= Constants.MAX_BOOKS_ISSUED_TO_A_USER;
    }

    @Override
    public int countBookItemLendingByUserIdAndStatus(long userId) {
        int count = 0;
        List<BookLending> bookLendingList = findAll();
        for (BookLending bookLending : bookLendingList)
            //tim lending co status != Return && userId ==userId tham so
            if (bookLending.getStatus() != LendingStatus.RETURN
                && bookLending.getUserId() == userId)
                count += 1;
        return count;
    }


    @Override
    public void lendBook(long userId, long bookItemId) {
        BookItem bookItem = bookItemService.findById(bookItemId);
        BookLending newBookLending = new BookLending();

        newBookLending.setBookItemId(bookItemId);
        newBookLending.setUserId(userId);
       // newBookLending.setUserName(newBookLending.getUserName());
        newBookLending.setStatus(LendingStatus.LENDING);
        Instant now = Instant.now();
        newBookLending.setCreatedAt(now);

        Instant dueAt = now.plus(Period.ofDays(Constants.MAX_LENDING_DAYS));
        newBookLending.setDueAt(dueAt);
        bookItem.setBorrowedAt(now);
        bookItem.setDueAt(dueAt);
        bookItem.setStatus(BookStatus.LOANED);
        bookItemService.update(bookItem);
        add(newBookLending);
    }

    @Override
    public void returnBook(long id) {
        BookLending bookLending = findById(id);
        BookItem bookItem = bookItemService.findById(bookLending.getBookItemId());
//        Instant now = Instant.now();
//        Instant dueAt = bookLending.getDueAt();
//        if (now.isAfter(dueAt)) {
//            long millis = System.currentTimeMillis() - dueAt.toEpochMilli();
//            int days = (int) (millis / 86400000);
//            if (days > 0) {
//                double fineAmount = days * Constants.FINE_AMOUNT;
//            }
//
//        }
        bookItem.setStatus(BookStatus.AVAILABLE);
        bookItem.setBorrowedAt(null);
        bookItem.setDueAt(null);
        bookItemService.update(bookItem);
        bookLending.setReturnAt(Instant.now());
        bookLending.setStatus(LendingStatus.RETURN);
        update(bookLending);
    }

    // gia han sach,
    @Override
    public boolean renewBook(BookItem bookItem) {
        return false;
    }

    @Override
    public BookLending findByBookItemIdAndUserIdAndStatus(long bookItemId, long userId, LendingStatus status) {
        List<BookLending> booksLending = findAll();
        for (BookLending bookLending : booksLending) {
            if (bookLending.getUserId() == userId &&
                    bookLending.getBookItemId() == bookItemId &&
                    bookLending.getStatus() == status)
                return bookLending;
        }
        return null;
    }

    @Override
    public boolean existsById(Long id) {
        return findById(id) != null;
    }


    @Override
    public void add(BookLending newBookLending) {
        List<BookLending> bookLendingList = findAll();
        newBookLending.setId(System.currentTimeMillis() / 1000);
        bookLendingList.add(newBookLending);
        CSVUtils.write(PATH, bookLendingList);
    }

    @Override
    public void update(BookLending newBookLending) {
        List<BookLending> bookLendingList = findAll();
        for (BookLending bookLending : bookLendingList) {
            if (bookLending.getId() == newBookLending.getId()) {
                LendingStatus status = newBookLending.getStatus();
                if (status != null)
                    bookLending.setStatus(newBookLending.getStatus());

                Instant createdAt = newBookLending.getCreatedAt();
                if (createdAt != null)
                    bookLending.setCreatedAt(newBookLending.getCreatedAt());

                Instant dueAt = newBookLending.getDueAt();
                if (dueAt != null)
                    bookLending.setDueAt(newBookLending.getDueAt());

                Instant returnAt = newBookLending.getReturnAt();
                if (returnAt != null)
                    bookLending.setReturnAt(newBookLending.getReturnAt());
                CSVUtils.write(PATH, bookLendingList);

            }
        }
    }

    @Override
    public void deleteById(Long id) {

    }

}
