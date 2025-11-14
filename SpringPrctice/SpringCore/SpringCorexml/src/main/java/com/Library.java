package com;

import java.util.List;

public class Library {
    private int id=0;
    private List<Book> books;
    private static int counter=0;

    public Library() {
        this.id=++counter;
    }

    public Library(List<Book> book) {
        this.id=++counter;
        this.books = book;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    @Override
    public String toString() {
        return "Library{" +
                "id=" + id +
                ", book=" + books +
                '}';
    }
}
