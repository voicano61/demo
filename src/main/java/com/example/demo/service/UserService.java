package com.example.demo.service;

import com.example.demo.pojo.User;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public interface UserService {
    List<User> selAll();
    List<User> sel(int id);
    void upadte(int id,String password);
    void delete(int id);
    void insert(User user);
    List<User> selUser(User user);
    void insUser(User user);
    List<User> selByName(String username);
    void updateInfo(User user);
}
