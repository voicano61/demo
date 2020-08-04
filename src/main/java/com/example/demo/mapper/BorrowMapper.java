package com.example.demo.mapper;

import com.example.demo.pojo.Borrow;
import org.apache.ibatis.annotations.Insert;
import org.springframework.stereotype.Repository;

@Repository
public interface BorrowMapper {
    @Insert("insert into borrow (bookName,author,bookId,date,userId) values (#{bookName},#{author},#{bookId},#{date},#{userId})")
    void borrowBook(Borrow book);
}
