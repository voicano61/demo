package com.example.demo.service.impl;

import com.example.demo.mapper.UserMapper;
import com.example.demo.pojo.User;
import com.example.demo.service.UserService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public List<User> selAll() {
        List<User> list=this.userMapper.selAll();
        return list;
    }

    @Override

    public List<User> sel(int id) {

        List<User> list = this.userMapper.sel(id);

        return list;

    }

    @Override
    public void upadte(int id, String password) {
        this.userMapper.update(id,password);
    }

    @Override
    public void delete(int id) {
        this.userMapper.del(id);
    }

    @Override
    public void insert(User user) {
        this.userMapper.insert(user);
    }

    @Override
    public List<User> selUser(User user) {
        List<User> list=this.userMapper.selUser(user);
        return list;
    }

    @Override
    public void insUser(User user) {
        this.userMapper.insUser(user);
    }

    @Override
    public List<User> selByName(String username) {
        List<User> list=this.userMapper.selByName(username);
        
        return list;
    }

    @Override
    public void updateInfo(User user) {
        this.userMapper.updateInfo(user);
    }


}
