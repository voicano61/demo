package com.example.demo.mapper;

import com.example.demo.pojo.Book;
import com.example.demo.pojo.Borrow;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BorrowMapper {
    @Insert("insert into borrow (bookName,author,bookId,date,userId,deadline,state) values (#{bookName},#{author},#{bookId},#{date},#{userId},#{deadLine},0)")
    void borrowBook(Borrow book);
    
    @Select("Select * from borrow where userId=#{userId}")
    List<Borrow> myBorrow(int userId);
    
    @Update("update borrow set deadline=#{deadLine},renew=#{renew},state=1 where id=#{id}")
    void renew(Borrow borrow);
    @Update("update borrow set state=3 where id=#{id}")
    void timeout(int id);
    @Update("update borrow set state=4 where id=#{id}")
    void compensate(int id);
    @Update("update borrow set state=2,reBook=#{returnDate} where id=#{id}")
    void returnBook(Borrow book);
    @Select("select * from borrow where state=0 or state=1")
    List<Borrow> selRe();
    @Select("select * from borrow where userId=#{userId} and (state=0 or state=1) AND (author LIKE '%#{name}%' or bookName LIKE '%#{name}%')")
    List<Borrow> searchBorrow(@Param("userId") int userId,@Param("name")String name);
}
