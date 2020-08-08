package com.example.demo.service;

import com.example.demo.pojo.Book;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BookService {
    List<Book> selAllBook();
    List<Book> selBook();
    Book selPrice(int id);
    List<Book> searchBook(String name);
}
