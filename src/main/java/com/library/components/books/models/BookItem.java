package com.library.components.books.models;

import com.library.utils.InstantUtils;

import java.time.Instant;

public class BookItem {
    private Long bookItemID;
    private String publisher;
    private Integer numberOfPages;
    private Instant borrowedAt;
    private Instant dueAt;
    private double price;
    private BookFormat format;
    private BookStatus status;
    private Instant dateOfPurchase;
    private Integer publicationAt;
    private boolean referenceOnly;
    private Instant updatedAt;
    private Long bookId;
    private Book book;

    public BookItem() {
    }

    public BookItem(String publisher, int numberOfPages,
                    double price, BookFormat format, int publicationAt,
                    Book newBook) {
        this.bookItemID = System.currentTimeMillis()/ 1000;
        this.publisher = publisher;
        this.numberOfPages = numberOfPages;
        this.price = price;
        this.format = format;
        this.publicationAt = publicationAt;
        this.book = newBook;
        this.bookId = newBook.getId();
    }

    public BookItem(String publisher, int numberOfPages, double price,
                    BookFormat format, int publicationAt,long bookId) {
        this.bookItemID= System.currentTimeMillis()/ 1000;
        this.publisher = publisher;
        this.numberOfPages = numberOfPages;
        this.price = price;
        this.format = format;
        this.publicationAt = publicationAt;
        this.bookId= bookId;
    }


    public static BookItem parse(String record) {
        BookItem bookItem = new BookItem();
        String[] fields = record.split(",");
        bookItem.bookItemID = Long.parseLong(fields[0]);
        bookItem.publisher = fields[1];
        bookItem.numberOfPages = Integer.parseInt(fields[2]);
        bookItem.borrowedAt = InstantUtils.parseInstant(fields[3]);
        bookItem.dueAt = InstantUtils.parseInstant(fields[4]);
        bookItem.price = Double.parseDouble(fields[5]);
        bookItem.format = BookFormat.parserBookFormat(fields[6]);
        bookItem.status = BookStatus.parseBookStatus(fields[7]);
        bookItem.dateOfPurchase = InstantUtils.parseInstant(fields[8]);
        bookItem.publicationAt = Integer.parseInt(fields[9]);
        bookItem.updatedAt = InstantUtils.parseInstant(fields[10]);
        bookItem.bookId = Long.parseLong(fields[11]);
        return bookItem;
    }

    public long getBookItemID() {
        return bookItemID;
    }

    public void setBookItemID(long bookItemID) {
        this.bookItemID = bookItemID;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public int getNumberOfPages() {
        return numberOfPages;
    }

    public void setNumberOfPages(int numberOfPages) {
        this.numberOfPages = numberOfPages;
    }

    public Instant getBorrowedAt() {
        return borrowedAt;
    }

    public void setBorrowedAt(Instant borrowedAt) {
        this.borrowedAt = borrowedAt;
    }

    public Instant getDueAt() {
        return dueAt;
    }

    public void setDueAt(Instant dueAt) {
        this.dueAt = dueAt;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public BookFormat getFormat() {
        return format;
    }

    public void setFormat(BookFormat format) {
        this.format = format;
    }

    public BookStatus getStatus() {
        return status;
    }

    public void setStatus(BookStatus status) {
        this.status = status;
    }

    public Instant getDateOfPurchase() {
        return dateOfPurchase;
    }

    public void setDateOfPurchase(Instant dateOfPurchase) {
        this.dateOfPurchase = dateOfPurchase;
    }

    public Integer getPublicationAt() {
        return publicationAt;
    }

    public void setPublicationAt(Integer publicationAt) {
        this.publicationAt = publicationAt;
    }

    public boolean isReferenceOnly() {
        return referenceOnly;
    }

    public void setReferenceOnly(boolean referenceOnly) {
        this.referenceOnly = referenceOnly;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public long getBookId() {
        return bookId;
    }

    public void setBookId(long bookId) {
        this.bookId = bookId;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Book getBook() {
        return book;
    }


    @Override
    public String toString() {
        return String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s",
                bookItemID,
                publisher,
                numberOfPages,
                borrowedAt,
                dueAt,
                price,
                format,
                status,
                dateOfPurchase,
                publicationAt,
                updatedAt,
                bookId);

    }
    public String getPreview(){
        return String.format("BookItemID: %s, NXB: %s, Giá: %s, Năm XB: %s, BookID: %s, Số trang: %s", this.bookItemID, this.publisher, this.price, this.publicationAt, this.bookId, this.numberOfPages);
    }
}


