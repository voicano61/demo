package com.example.demo.service;

import com.example.demo.pojo.Borrow;
import org.springframework.stereotype.Service;

@Service
public interface BorrowService {
    void borrow(Borrow book);
}
