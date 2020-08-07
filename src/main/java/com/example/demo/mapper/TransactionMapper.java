package com.example.demo.mapper;

import com.example.demo.pojo.Transaction;
import org.apache.ibatis.annotations.Insert;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionMapper {
    @Insert("insert into transaction (userId,state,amount,date) values(#{userId},#{state},#{amount},#{date})")
    void add(Transaction transaction);
}
