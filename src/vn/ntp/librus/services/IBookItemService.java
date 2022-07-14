package vn.ntp.librus.services;

import vn.ntp.librus.model.BookItem;

public interface IBookItemService  extends IAbstractService<BookItem,Long>{

    void add(BookItem newBook);

    void update(BookItem newBook);
}
