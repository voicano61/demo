package com.example.demo.controller;

import com.example.demo.mapper.UserMapper;
import com.example.demo.pojo.User;
import com.example.demo.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("t")
public class TestController {
//    @RequestMapping(value = "show",method = RequestMethod.GET)
//    public String show(Model model)
//    {
//        model.addAttribute("uid","123456");
//        model.addAttribute("name","hxw");
//        return "index";
//    }

    @Resource
    private RedisUtil redisUtil;
    @Resource
    private RedisTemplate<Object, Object> redisTemplate;
    @ResponseBody
    @RequestMapping(value = "set")
    public void setAndGetMap(){
        Map<String,Object> map = new HashMap<>();
        map.put("name","张三");
        map.put("age","18");
        map.put("gender","男");

        redisTemplate.opsForHash().putAll("userInfo",map);
        redisTemplate.opsForHash().put("userInfo","sel","111");

        System.out.println(redisTemplate.opsForHash().entries("userInfo"));
        System.out.println(redisTemplate.opsForHash().keys("userInfo"));
        System.out.println(redisTemplate.opsForHash().values("userInfo"));
        System.out.println(redisTemplate.opsForHash().get("userInfo","name"));
    }
    
    @ResponseBody
    @RequestMapping("del")
    public void delect()
    {
        redisUtil.del("a");
    }
}
