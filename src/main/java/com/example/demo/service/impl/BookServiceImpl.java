package com.example.demo.service.impl;

import com.example.demo.mapper.BookMapper;
import com.example.demo.pojo.Book;
import com.example.demo.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class BookServiceImpl implements BookService {
    @Autowired
    private BookMapper bookMapper;
    @Override
    public List<Book> selAllBook() {
        return this.bookMapper.selAllBook();
    }

    @Override
    public List<Book> selBook() {
        return this.bookMapper.selBook();
    }

    @Override
    public Book selPrice(int id) {
       Book book=this.bookMapper.selPrice(id);
        return book;
    }
}
