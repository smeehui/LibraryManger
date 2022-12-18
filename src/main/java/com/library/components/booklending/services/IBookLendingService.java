package com.library.components.booklending.services;

import com.library.components.books.models.BookItem;
import com.library.components.booklending.model.BookLending;
import com.library.components.booklending.model.LendingStatus;
import com.library.services.IAbstractService;

import java.util.List;

public interface IBookLendingService extends IAbstractService<BookLending, Long> {

    void add(BookLending newUser);

    void update(BookLending newUser);

    boolean isBookIssuedQuotaExceeded(long userId);

    int countBookItemLendingByUserIdAndStatus(long userId);

    void lendBook(long userId, long bookItemId);

    void returnBook(long bookLendingId);

    boolean renewBook(BookItem bookItem);

    BookLending findByBookItemIdAndUserIdAndStatus(long bookItemId, long userId, LendingStatus status);

    BookLending findByUserId(long userId);

    List<BookLending> findBookLendingByUserId(long userId);

    List<BookLending> findBookLendingByUserIdAndStatus(long userId, LendingStatus lending);
}
