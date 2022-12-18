package com.library.components.books.models;

import com.library.utils.InstantUtils;

import java.time.Instant;

public class Book {
    private Long id;
    private String isbn;
    private String title;
    private String author;
    private String subject;
    private String language;
    private Instant createdAt;
    private Instant updatedAt;

    public Book() {
    }

    public Book(Long id, String isbn, String title, String author, String subject, String language, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.subject = subject;
        this.language = language;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Book(String title, String isbn, String author, String subject, String language) {
        this.title = title;
        this.isbn = isbn;
        this.author = author;
        this.subject = subject;
        this.language = language;
    }


    public static Book parse(String raw) {
        String[] fields = raw.split(",");
        long id = Long.parseLong(fields[0]);
        String ISBN = fields[1];
        String title = fields[2];
        String author = fields[3];
        String subject = fields[4];
        String language = fields[5];
        Instant createdAt = InstantUtils.parseInstant(fields[6]);
        Instant updatedAt = InstantUtils.parseInstant(fields[7]);
        return new Book(id, ISBN, title, author, subject, language, createdAt, updatedAt);
    }

    public long getId() {
        return id;
    }

    public long setId(long id) {
        this.id = id;
        return id;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }


    @Override
    public String toString() {
        return String.format("%s,%s,%s,%s,%s,%s,%s,%s",
                id, isbn, title, author, subject, language, createdAt, updatedAt);
    }

    public String getPreview() {
        return String.format("Tiêu đề: %s, ISBN: %s, Tác giả: %s, Chủ đề: %s, Ngôn ngữ: %s", title,isbn,author,subject,language);
    }
}