package com.library.components.books.services;

import com.library.components.books.models.BookItem;
import com.library.services.IAbstractService;

public interface IBookItemService  extends IAbstractService<BookItem,Long> {

    void add(BookItem newBook);

    void update(BookItem newBook);
}
