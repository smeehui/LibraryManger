package con.library.components.books.services;

import con.library.components.books.models.BookItem;
import con.library.services.IAbstractService;

public interface IBookItemService  extends IAbstractService<BookItem,Long> {

    void add(BookItem newBook);

    void update(BookItem newBook);
}
