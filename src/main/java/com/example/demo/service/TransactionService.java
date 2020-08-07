package com.example.demo.service;

import com.example.demo.mapper.TransactionMapper;
import com.example.demo.pojo.Transaction;
import org.springframework.stereotype.Service;

@Service
public interface TransactionService {
    void add(Transaction transaction);
}
