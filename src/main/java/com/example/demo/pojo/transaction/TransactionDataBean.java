package com.example.demo.pojo.transaction;

import com.example.demo.pojo.Transaction;

import java.util.List;

public class TransactionDataBean {
    private List<Transaction> transactionList;

    public List<Transaction> getTransactionList() {
        return transactionList;
    }

    public void setTransactionList(List<Transaction> transactionList) {
        this.transactionList = transactionList;
    }
}
