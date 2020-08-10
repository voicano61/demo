package com.example.demo.mapper;

import com.example.demo.pojo.Transaction;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionMapper {
    @Insert("insert into transaction (userId,state,amount,date) values(#{userId},#{state},#{amount},#{date})")
    void add(Transaction transaction);
    @Select("<script>"+
            "select * from transaction "+
            "where userId=#{userId}"+
            "<if test=\"state != 3\"> and state=#{state} </if>"+
            "</script>")
    List<Transaction> allTransaction(@Param("userId") int userId,@Param("state") int state);
}
