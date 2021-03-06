package com.example.demo.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.demo.mapper.BookMapper;
import com.example.demo.pojo.Book;
import com.example.demo.pojo.User;
import com.example.demo.pojo.book.BookBean;
import com.example.demo.pojo.page.PageBean;
import com.example.demo.pojo.user.DataBean;
import com.example.demo.pojo.user.UserBean;
import com.example.demo.pojo.userList.UserDataBean;
import com.example.demo.pojo.userList.UserListBean;
import com.example.demo.service.BookService;
import com.example.demo.service.UserService;
import com.example.demo.utils.HttpUtils;
import com.example.demo.utils.JWTUtils;
import com.example.demo.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("user")
public class UserController {
    @Autowired
    private UserService userService;
    //获取全部用户信息
    @ResponseBody
    @RequestMapping(value = "list",method = RequestMethod.POST)
    public UserBean selAll(HttpServletRequest request)
    {
        String token=request.getHeader("token");
        UserBean userBean=new UserBean();
        //解析token中的数据
//        DecodedJWT jwt = JWT.decode(token);
//        String name=jwt.getClaim("userName").asString();
        if(JWTUtils.verify(token))
        {
//            model.addAttribute("userlist",list);
//        model.addAttribute("test","tset");
            DecodedJWT jwt = JWT.decode(token);
            int role=jwt.getClaim("role").asInt();
            int id=jwt.getClaim("id").asInt();
            userBean.setResultCode(200);
            DataBean dataBean=new DataBean();
            dataBean.setUser(this.userService.sel(id).get(0));
            userBean.setData(dataBean);
            userBean.setResultString("成功");
        }
        else
        {
            userBean.setResultCode(500);
            userBean.setResultString("失败");
        }
        return userBean;
    }
    
    
    //获取对应id的用户信息
    @ResponseBody
    @RequestMapping("list/{id}")
    public List<User> sel(@PathVariable int id) {

        List<User> list=this.userService.sel(id);
        return list;
    }
    
    //更新对应id的用户密码
//    @ResponseBody
//    @RequestMapping(value = "update",method = RequestMethod.POST)
//    public List<User> update(HttpServletRequest request)
//    {
//        int id=Integer.parseInt(request.getParameter("id"));
//        String password=request.getParameter("password");
//        this.userService.upadte(id,password);
//        List<User> list=this.userService.sel(id);
//        return list;
//    }
    
    //删除对应id的用户
    @ResponseBody
    @RequestMapping(value = "delete",method = RequestMethod.POST)
    public void delete(HttpServletRequest request)
    {
        int id=Integer.parseInt(request.getParameter("id"));
        this.userService.delete(id);
    }
    
    //新增用户
    @ResponseBody
    @RequestMapping(value = "insert",method = RequestMethod.POST)
    public void insert(HttpServletRequest request)
    {
        int id=Integer.parseInt(request.getParameter("id"));
        String name=request.getParameter("name");
        String password=request.getParameter("password");
        User user=new User();
        user.setId(id);
        user.setUsername(name);
        user.setPassword(password);
        this.userService.insert(user);

    }
    
    //登录
    @ResponseBody
    @RequestMapping(value = "login",method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public UserBean login(String username,String password)
    {
        UserBean userBean=new UserBean();
        if(!username.equals("")&&!password.equals(""))
        {
            User user=new User();
            user.setUsername(username);
            user.setPassword(password);
            List<User> list=this.userService.selUser(user);
            if(list.size()>0)
            {
                userBean.setResultCode(200);
                userBean.setResultString("登陆成功");
                DataBean data=new DataBean();
                data.setUser(list.get(0));
                userBean.setData(data);
//                String token= TokenUtils.getToken(username+new Date());
                String token= JWTUtils.sign(username,list.get(0).getId(),list.get(0).getRole());
                data.setToken(token);
            }
            else
            {
                userBean.setResultCode(500);
                userBean.setResultString("用户名或密码错误");
            }
        }
        else
        {
            userBean.setResultCode(500);
            userBean.setResultString("用户名或密码填写不完整");
        }
        return userBean;
    }
    
    //注册
    @ResponseBody
    @RequestMapping(value = "register",method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public UserBean register(HttpServletRequest request)
    {
        String username=request.getParameter("username");
        String password=request.getParameter("password");
        int role=Integer.parseInt(request.getParameter("role"));
        int money=Integer.parseInt(request.getParameter("money"));
        UserBean userBean=new UserBean();
        if(!username.equals("")&&!password.equals(""))
        {
            String message=decide(username);
            if(message.equals("error"))
            {
                userBean.setResultCode(500);
                userBean.setResultString("用户名已存在");
            }
            else
            {
                User user=new User();
                user.setUsername(username);
                user.setPassword(password);
                user.setRole(role);
                user.setMoney(money);
                user.setRole(0);
                this.userService.insUser(user);
                userBean.setResultCode(200);
                DataBean dataBean=new DataBean();
                dataBean.setUser(this.userService.selUser(user).get(0));
                userBean.setData(dataBean);
                userBean.setResultString("注册成功");
            }
     
        }
        else
        {
            userBean.setResultCode(500);
            userBean.setResultString("用户名或密码填写不完整");
        }
        return userBean;
    }
    //判断用户名是否存在
    @ResponseBody
    @RequestMapping(value = "decide")
    public String decide(String username) {
        List<User> list=this.userService.selByName(username);
        if(list.size()>0)
        {
            return "error";
        }
        else
        {
            return "success";
        }
    }
    //分页
//    @ResponseBody
//    @RequestMapping(value = "page",method = RequestMethod.POST)
//    public PageBean ajaxBlog(HttpServletRequest request, int pageNum, int pageSize){
//        String token=request.getHeader("token");
//        PageBean pageBean=new PageBean();
//        if(JWTUtils.verify(token))
//        {
//            PageHelper.startPage(pageNum,pageSize);
//            List<User> blogList = this.userService.selAll();
//            PageInfo<User> pageInfo = new PageInfo<User>(blogList);
//            pageBean.setResultCode(200);
//            pageBean.setResultString("成功");
//            pageBean.setPageInfo(pageInfo);
//            return pageBean;
//        }
//        else
//        {
//            pageBean.setResultCode(500);
//            pageBean.setResultString("失败");
//            return pageBean;
//        }
//       
//    }
//    
    @ResponseBody
    @RequestMapping(value = "showInfo",method = RequestMethod.POST)
    public UserBean showInfo(HttpServletRequest request)
    {
        String token=request.getHeader("token");
        UserBean userBean=new UserBean();
        if(JWTUtils.verify(token))
        {
            DecodedJWT jwt = JWT.decode(token);
            int id=jwt.getClaim("id").asInt();
            List<User> list=this.userService.sel(id);
            userBean.setResultCode(200);
            userBean.setResultString("success");
            DataBean dataBean=new DataBean();
            dataBean.setUser(list.get(0));
            userBean.setData(dataBean);
        }
        else
        {
            userBean.setResultCode(500);
            userBean.setResultString("error");
        }
        return userBean;
    }
    @ResponseBody
    @RequestMapping(value = "update",method = RequestMethod.POST)
    public UserBean update(HttpServletRequest request)
    {
        String token=request.getHeader("token");
        UserBean userBean=new UserBean();
        if(JWTUtils.verify(token)) {
            
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            System.out.println(username+password);
            DecodedJWT jwt = JWT.decode(token);
            int id=jwt.getClaim("id").asInt();
            if(username.equals("")||username==null)
            {
                username=null;
            }
            if(password.equals("")||password==null)
            {
                password=null;
            }
            if(username!=null||password!=null)
            {
                User user=new User();
                user.setUsername(username);
                user.setPassword(password);
                user.setId(id);
                this.userService.updateInfo(user);
            }
            List<User> list=this.userService.sel(id);
            userBean.setResultCode(200);
            userBean.setResultString("success");
            DataBean dataBean=new DataBean();
            dataBean.setUser(list.get(0));
            userBean.setData(dataBean);
        }
        else 
         {
                userBean.setResultCode(500);
                userBean.setResultString("error");
        }
        return userBean;
    }
    @ResponseBody
    @RequestMapping(value = "selOne",method = RequestMethod.POST)
    public UserBean selOne(HttpServletRequest request)
    {
        String token=request.getHeader("token");
        UserBean userBean=new UserBean();
        if(JWTUtils.verify(token)) {
            DecodedJWT jwt = JWT.decode(token);
            int userId=jwt.getClaim("id").asInt();
            User user=this.userService.selOne(userId);
            userBean.setResultCode(200);
            userBean.setResultString("success");
            DataBean dataBean=new DataBean();
            dataBean.setUser(user);
            userBean.setData(dataBean);
            
        }
        else
        {
            userBean.setResultCode(500);
            userBean.setResultString("error");
        }
        return userBean;
    }
    @ResponseBody
    @RequestMapping(value = "change",method = RequestMethod.POST)
    public UserBean change(HttpServletRequest request)
    {
        String token=request.getHeader("token");
        String userName=request.getParameter("userName");
        String password=request.getParameter("password");
        UserBean userBean=new UserBean();
        if(JWTUtils.verify(token)) {
            DecodedJWT jwt = JWT.decode(token);
            int userId=jwt.getClaim("id").asInt();
            User user=new User();
            user.setId(userId);
            user.setUsername(userName);
            user.setPassword(password);
            this.userService.updateInfo(user);
            userBean.setResultCode(200);
            userBean.setResultString("success");
        }
        else
        {
            userBean.setResultCode(500);
            userBean.setResultString("error");
        }
        return userBean;
    }
    @ResponseBody
    @RequestMapping(value = "admin",method = RequestMethod.POST)
    public UserBean admin(HttpServletRequest request)
    {
        String token=request.getHeader("token");
        UserBean userBean=new UserBean();
        if(JWTUtils.verify(token)) {
            DecodedJWT jwt = JWT.decode(token);
            int role=jwt.getClaim("role").asInt();
            if(role==1)
            {
                userBean.setResultCode(200);
                userBean.setResultString("success");
            }
            else
            {
                userBean.setResultCode(300);
                userBean.setResultString("没有管理员权限");
            }
        }
        else
        {
            userBean.setResultCode(500);
            userBean.setResultString("error");
        }
        return userBean;
    }
}
