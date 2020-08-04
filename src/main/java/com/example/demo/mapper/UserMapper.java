package com.example.demo.mapper;

import com.example.demo.pojo.User;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Indexed;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public interface UserMapper {
    @Select("select * from user")
    List<User> selAll();

    @Select("select * from user where id = #{id}")
    List<User> sel(int id);

    @Update("update user set password=#{password} where id=#{id}")
    void update(@Param("id")int id, @Param("password")String password);

    @Delete("delete from user where id=#{id}")
    void del(int id);

    @Insert("insert into user(id,username,password) values(#{id},#{username},#{password})")
    void insert(User user);
    
    @Select(" select * from user where username=#{username} and password=#{password}")
    List<User> selUser(User user);
    
    @Insert("insert into user(username,password,role) values(#{username},#{password},#{role})")
    void insUser(User user);
    @Select("select * from user where username=#{username}")
    List<User> selByName(String username);
    
    @Update("<script>"+
            "update user"+
            "<set>"+
            "<if test=\"username != null\">username=#{username},</if>"+
            "<if test=\"password != null\">password=#{password}</if>"+
            "</set>"+
            "where id=#{id}"+
            "</script>")
    void updateInfo(User user);
    @Select("select * from user where id=#{id}")
    User selOne(int id);

    @Update("update user set money=money-#{price} where id=#{id}")
    void upMoney(@Param("id")int id, @Param("price")int price);
}
