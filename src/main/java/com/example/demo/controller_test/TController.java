package com.example.demo.controller_test;

import com.alibaba.fastjson.JSON;
import com.example.demo.pojo.User;
import com.example.demo.pojo.book.BookBean;
import com.example.demo.pojo.page.PageBean;
import com.example.demo.pojo.user.UserBean;
import com.example.demo.pojo.userList.UserListBean;
import com.example.demo.utils.HttpUtils;
import com.example.demo.utils.RedisUtil;
import okhttp3.Response;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Controller
@RequestMapping("test")
public class TController {
    
    @RequestMapping("/")
    public String welcome()
    {
        return "login";
    }
    
    @RequestMapping(value = "login" ,method = RequestMethod.POST)
    public String  login(HttpServletRequest request, Model model) throws IOException {
        String username=request.getParameter("username");
        String password=request.getParameter("password");
        Response response=HttpUtils.login(username, password);
        String responseData = response.body().string();
        UserBean info=JSON.parseObject( responseData,UserBean.class);
        if(info.getResultCode()==200) {
            Jedis jedis = new Jedis("127.0.0.1",6379);
            jedis.set("token",info.getData().getToken());
//            System.out.println(redisUtil.get("token"));
            Response responses=HttpUtils.show((String) jedis.get("token"));
            String responseDatas = responses.body().string();
            UserBean userBean=JSON.parseObject(responseDatas,UserBean.class);
//            model.addAttribute("userList",userListBean.getData().getUserList());
            
            Response res=HttpUtils.allBook(jedis.get("token"));
            String resDatas=res.body().string();
            BookBean bookBean=JSON.parseObject(resDatas,BookBean.class);
            if(userBean.getData().getUser().getRole()==0)
            {
                model.addAttribute("bookList",bookBean.getData().getBook());
                return "book";
            }
            else
            {
                return "error";
            }
        }
        else
        {
            return "login";
        }
        
    }
    
    //注册页
    @RequestMapping("showreg")
    public String showreg()
    {
        return "register";
    }
    
    //注册
    @ResponseBody
    @RequestMapping(value = "register",method = RequestMethod.POST)
    public String register(HttpServletRequest request) throws IOException {
        String username=request.getParameter("username");
        String password=request.getParameter("password");
        Response response=HttpUtils.register(username,password);
        String responseData = response.body().string();
        UserBean info=JSON.parseObject( responseData,UserBean.class);
        if(info.getResultCode()==200)
        {
            return "success";
        }
        else
        {
            return "error";
        }
    }
    //判断用户名是否存在
    @ResponseBody
    @RequestMapping(value = "decide")
    public String decide(HttpServletRequest request) throws IOException {
        String username=request.getParameter("username");
        Response response=HttpUtils.decide(username);
        String responseData = response.body().string();
        return responseData;
        
    }
    
    @RequestMapping(value = "showInfo")
    public String showInfo(Model model) throws IOException {
        Jedis jedis = new Jedis("127.0.0.1",6379);
        String token=(String)jedis.get("token");
        Response response=HttpUtils.showInfo(token);
        String responseData = response.body().string();
        UserBean info=JSON.parseObject( responseData,UserBean.class);
        if(info.getResultCode()==200)
        {
            model.addAttribute("username",info.getData().getUser().getUsername());
            model.addAttribute("password",info.getData().getUser().getPassword());
            return "info";
        }
        else
        {

            return "login";
        }
    }
    
    @ResponseBody
    @RequestMapping("error")
    public String error()
    {
        return "error";
    }
    
    @RequestMapping(value = "update",method = RequestMethod.POST)
    public String  update(HttpServletRequest request,Model model) throws IOException {
        Jedis jedis = new Jedis("127.0.0.1",6379);
        String username=request.getParameter("username");
        String password=request.getParameter("password");
        System.out.println(username+password);
        String token=jedis.get("token");
//        System.out.println(token);
        Response responses=HttpUtils.showInfo(token);
        String responseDatas = responses.body().string();
        UserBean infos=JSON.parseObject( responseDatas,UserBean.class);
       
        if(infos.getResultCode()==200)
        { 
            User user=infos.getData().getUser();
            if(username==null||username.equals(""))
            {
                username=user.getUsername();
            }
            if(password==null||password.equals(""))
            {
                password=user.getPassword();
            }
            System.out.println(username+password);
            Response response=HttpUtils.update(token,username,password);
            String responseData = response.body().string();
            UserBean info=JSON.parseObject( responseData,UserBean.class);
            if(info.getResultCode()==200)
            {
                model.addAttribute("username",info.getData().getUser().getUsername());
                model.addAttribute("password",info.getData().getUser().getPassword());
                return "info";
            }
            else
            {

                return "login";
            }
        }
        else {
            return "login";
        }
   
    }
    @RequestMapping("tLogin")
    public String tLogin()
    {
        return "test_login";
    }
    
    @RequestMapping(value = "showBook",method = RequestMethod.POST)
    public String showBook(Model model) throws IOException {
        Jedis jedis = new Jedis("127.0.0.1",6379);
        Response res=HttpUtils.allBook(jedis.get("token"));
        String resDatas=res.body().string();
        BookBean bookBean=JSON.parseObject(resDatas,BookBean.class);
        if(bookBean.getResultCode()==200)
        {
            model.addAttribute("bookList",bookBean.getData().getBook());
            return "book";
        }
        else
        {
            return "login";
        }
    }
    @RequestMapping(value = "borrowBook",method = RequestMethod.POST)
    public String borrowBook(HttpServletRequest request,Model model) throws IOException {
        Jedis jedis = new Jedis("127.0.0.1",6379);
        String bookId=request.getParameter("bookId");
        String bookName=request.getParameter("bookName");
        String author=request.getParameter("author");
        Response res=HttpUtils.borrow(jedis.get("token"),bookName,author,bookId);
        String resDatas=res.body().string();
        BookBean bookBean=JSON.parseObject(resDatas,BookBean.class);
        if(bookBean.getResultCode()==200)
        {
            return "redirect:/test/showBook";
        }
        else
        {
            return "login";
        }
    }
    
}
