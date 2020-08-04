package com.example.demo.pojo.book;

import com.example.demo.pojo.Book;
import com.example.demo.pojo.Borrow;

import java.util.List;

public class BookDataBean {
    private List<Book> book;
    private List<Borrow> borrows;

    public List<Book> getBook() {
        return book;
    }

    public void setBook(List<Book> book) {
        this.book = book;
    }

    public List<Borrow> getBorrows() {
        return borrows;
    }

    public void setBorrows(List<Borrow> borrows) {
        this.borrows = borrows;
    }
}
