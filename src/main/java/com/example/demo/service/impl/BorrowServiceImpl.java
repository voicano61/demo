package com.example.demo.service.impl;

import com.example.demo.mapper.BookMapper;
import com.example.demo.mapper.BorrowMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.pojo.Book;
import com.example.demo.pojo.Borrow;
import com.example.demo.service.BorrowService;
import com.example.demo.service.UserService;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BorrowServiceImpl implements BorrowService {
    @Autowired
    private BorrowMapper borrowMapper;
    @Autowired
    private BookMapper bookMapper;
    @Autowired
    private UserMapper userMapper;
    @Override
    public void borrow(Borrow book) {
        this.borrowMapper.borrowBook(book);
        this.bookMapper.updateNumber(book.getBookId());
    }

    @Override
    public List<Borrow> myBorrow(int userId) {
        return this.borrowMapper.myBorrow(userId);
    }

    @Override
    public void renew(Borrow borrow) {
        this.borrowMapper.renew(borrow);
    }

    @Override
    public void timeout(int id) {
        this.borrowMapper.timeout(id);
    }

    @Override
    public void compensate(int id) 
    {
        this.borrowMapper.compensate(id);
    }

    @Override
    public void returnBook(Borrow book) {
        this.borrowMapper.returnBook(book);
        this.bookMapper.upNumber(book.getBookId());
    }

    @Override
    public List<Borrow> selRe(int userId) {
        List<Borrow> list=this.borrowMapper.selRe(userId);
        return list;
    }

    @Override
    public List<Borrow> searchBorrow(int userId, String name) {
        List<Borrow> list=this.borrowMapper.searchBorrow(userId,name);
        return list;
    }

    @Override
    public List<Borrow> searchRe(int userId, String name) {
        List<Borrow> list=this.borrowMapper.searchRe(userId,name);
        return list;
    }

}
