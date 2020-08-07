package com.example.demo.service.impl;

import com.example.demo.mapper.TransactionMapper;
import com.example.demo.pojo.Transaction;
import com.example.demo.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImpl implements TransactionService {
    @Autowired 
    private TransactionMapper transactionMapper;
    @Override
    public void add(Transaction transaction) {
        this.transactionMapper.add(transaction);
    }
}
