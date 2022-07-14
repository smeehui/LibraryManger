package vn.ntp.librus.services;

import vn.ntp.librus.model.BookItem;
import vn.ntp.librus.model.BookLending;
import vn.ntp.librus.model.LendingStatus;

import java.util.List;

public interface IBookLendingService extends IAbstractService<BookLending, Long> {

    void add(BookLending newUser);

    void update(BookLending newUser);

    boolean isBookIssuedQuotaExceeded(long userId);

    int countBookItemLendingByUserIdAndStatus(long userId);

    void lendBook(long userId, long bookItemId);

    void returnBook(long bookItemId, long userId);

    boolean renewBook(BookItem bookItem);

    BookLending findByBookItemIdAndUserIdAndStatus(long bookItemId, long userId, LendingStatus status);

    BookLending findByUserId(long userId);
}
