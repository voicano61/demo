package com.example.demo.service.impl;

import com.example.demo.mapper.BookMapper;
import com.example.demo.mapper.BorrowMapper;
import com.example.demo.pojo.Borrow;
import com.example.demo.service.BorrowService;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BorrowServiceImpl implements BorrowService {
    @Autowired
    private BorrowMapper borrowMapper;
    @Autowired
    private BookMapper bookMapper;
    @Override
    public void borrow(Borrow book) {
        this.borrowMapper.borrowBook(book);
        this.bookMapper.updateNumber(book.getBookId());
    }
}
