package con.library.components.booklending.services;

import con.library.components.books.models.BookItem;
import con.library.components.booklending.model.BookLending;
import con.library.components.booklending.model.LendingStatus;
import con.library.services.IAbstractService;

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
