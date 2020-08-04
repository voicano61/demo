package com.example.demo.mapper;

import com.example.demo.pojo.Book;
import com.example.demo.pojo.User;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Repository
public interface BookMapper {
    @Select("select * from book")
    List<Book> selAllBook();
    @Select("select * from book where number>0")
    List<Book> selBook();
    @Update("update book set number=number-1 where id=#{id}")
    void updateNumber(int id);
    
    @Select("select * from book where id=#{id}")
    Book selPrice(int id);
    @Update("update book set number=number+1 where id=#{id}")
    void upNumber(int id);


}
