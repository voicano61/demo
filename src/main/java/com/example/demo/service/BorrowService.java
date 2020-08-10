package com.example.demo.service;

import com.example.demo.pojo.Book;
import com.example.demo.pojo.Borrow;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BorrowService {
    void borrow(Borrow book);
    List<Borrow> myBorrow(int userId);
    void renew(Borrow borrow);
    void timeout(int id);
    void compensate(int id);
    void returnBook(Borrow book);
    List<Borrow> selRe(int userId);
    List<Borrow> searchBorrow(int userId,String name);
    List<Borrow> searchRe(int userId,String name);
}
