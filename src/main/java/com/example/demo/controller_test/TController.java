package com.example.demo.controller_test;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.demo.pojo.Book;
import com.example.demo.pojo.Borrow;
import com.example.demo.pojo.User;
import com.example.demo.pojo.book.BookBean;
import com.example.demo.pojo.user.UserBean;
import com.example.demo.utils.HttpUtils;
import com.example.demo.utils.JWTUtils;
import okhttp3.Response;
import org.hibernate.boot.jaxb.hbm.spi.JaxbHbmEntityDiscriminatorType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.Jedis;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("test")
public class TController {
    
    @RequestMapping("/")
    public String welcome()
    {
        Jedis jedis = new Jedis("127.0.0.1",6379);
//        jedis.set("token","");
        if(jedis.get("token")==null)
        {
            
            return "login";
        }
        else
        {
            return "redirect:/test/showBook";
        }
       
    }
    
    @RequestMapping(value = "login" ,method = RequestMethod.POST)
    public String  login(HttpServletRequest request, Model model) throws IOException {
        String username=request.getParameter("username");
        String password=request.getParameter("password");
        Response response=HttpUtils.login(username, password);
        String responseData = response.body().string();
        UserBean info=JSON.parseObject( responseData,UserBean.class);
        Jedis jedis = new Jedis("127.0.0.1",6379);
        if(info.getResultCode()==200) {
//            System.out.println(redisUtil.get("token"));
            jedis.set("token",info.getData().getToken());
            Response responses=HttpUtils.show((String) jedis.get("token"));
            String responseDatas = responses.body().string();
            UserBean userBean=JSON.parseObject(responseDatas,UserBean.class);
//            model.addAttribute("userList",userListBean.getData().getUserList());
            
            Response res=HttpUtils.allBook(jedis.get("token"));
            String resDatas=res.body().string();
            BookBean bookBean=JSON.parseObject(resDatas,BookBean.class);
            if(userBean.getData().getUser().getRole()==0)
            {
                model.addAttribute("user",userBean.getData().getUser());
                model.addAttribute("bookList",bookBean.getData().getBook());
                return "book";
            }
            else
            {
                return "admin";
            }
        }
        else
        {
            model.addAttribute("message",info.getResultString());
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
            model.addAttribute("message","账户登录信息过期");
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
                model.addAttribute("message","账户登录信息过期");
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
        return "";
    }
    
    @RequestMapping(value = "showBook",method = RequestMethod.GET)
    public String showBook(Model model) throws IOException {
        Jedis jedis = new Jedis("127.0.0.1",6379);
        if(jedis.get("token")!=null)
        {
            Response res=HttpUtils.allBook(jedis.get("token"));
            String resDatas=res.body().string();
            BookBean bookBean=JSON.parseObject(resDatas,BookBean.class);
            if(bookBean.getResultCode()==200)
            {
                User user=new User();
                if(userMessage().getResultCode()==200)
                {
                    user=userMessage().getData().getUser();
                }
                model.addAttribute("user",user);
                model.addAttribute("bookList",bookBean.getData().getBook());
                return "book";
            }
            else
            {
                jedis.del("token");
                model.addAttribute("message","账户登录信息过期");
                return "login";
            }
        }
        return "login";
    }
    @RequestMapping(value = "borrowBook",method = RequestMethod.GET)
    public String borrowBook(HttpServletRequest request,Model model) throws IOException {
        Jedis jedis = new Jedis("127.0.0.1",6379);
        String bookId=request.getParameter("bookId");
        String bookName=request.getParameter("bookName");
        String author=request.getParameter("author");
        if(jedis.get("token")!=null)
        {
            Response res=HttpUtils.borrow(jedis.get("token"),bookName,author,bookId);
            String resDatas=res.body().string();
            BookBean bookBean=JSON.parseObject(resDatas,BookBean.class);
            if(bookBean.getResultCode()==200)
            {
                return "redirect:/test/showBook";
            }
            else
            {
                jedis.del("token");
                model.addAttribute("message","账户登录信息过期");
                return "login";
            }
        }
        return "login";
    }
    @RequestMapping(value = "myborrow",method = RequestMethod.GET)
    public String myBorrow(HttpServletRequest request,Model model) throws IOException {
        Jedis jedis = new Jedis("127.0.0.1",6379);
        if(jedis.get("token")!=null)
        {
            Response res=HttpUtils.myBorrow(jedis.get("token"));
            String resDatas=res.body().string();
            BookBean bookBean=JSON.parseObject(resDatas,BookBean.class);
            if(bookBean.getResultCode()==200)
            {

                User user=new User();
                if(userMessage().getResultCode()==200)
                {
                    user=userMessage().getData().getUser();
                }
                model.addAttribute("user",user);
                model.addAttribute("borrowList",bookBean.getData().getBorrows());
                return "myBorrow";
            }
            else
            {
                jedis.del("token");
                model.addAttribute("message","账户登录信息过期");
                return "login";
            }
        }
        return "login";
    }
    @RequestMapping(value = "renew",method = RequestMethod.GET)
    public String renew(HttpServletRequest request,Model model) throws IOException {
        String id=request.getParameter("id");
        String deadline=request.getParameter("deadLine");
        Jedis jedis = new Jedis("127.0.0.1",6379);
        if(jedis.get("token")!=null)
        {
            Response res=HttpUtils.renew(jedis.get("token"),id,deadline);
            String resDatas=res.body().string();
            BookBean bookBean=JSON.parseObject(resDatas,BookBean.class);
            if(bookBean.getResultCode()==200)
            {
                return "redirect:/test/myborrow";
            }
            else
            {
                jedis.del("token");
                model.addAttribute("message","账户登录信息过期");
                return "login";
            }
        }
        return "login";
    }
    @ResponseBody
    @RequestMapping(value = "selPrice",method = RequestMethod.GET)
    public String selPrice(HttpServletRequest request,Model model) throws IOException {
        String bookId=request.getParameter("bookId");
        Jedis jedis = new Jedis("127.0.0.1",6379);
        if(jedis.get("token")!=null)
        {
            Response res=HttpUtils.selPrice(jedis.get("token"),bookId);
            String resDatas=res.body().string();
            BookBean bookBean=JSON.parseObject(resDatas,BookBean.class);
            if(bookBean.getResultCode()==200)
            {
                return bookBean.getResultString();
            }
            else if(bookBean.getResultCode()==300)
            {
                return "lack";
            }
            else
            {
                jedis.del("token");
                model.addAttribute("message","账户登录信息过期");
                return "error";
            }
        }
        return "error";
    }
    @RequestMapping(value = "returnBook",method = RequestMethod.GET)
    public String returnBook(HttpServletRequest request,Model model) throws IOException {
        String id=request.getParameter("id");
        String bookId=request.getParameter("bookId");
        Jedis jedis = new Jedis("127.0.0.1",6379);
        if(jedis.get("token")!=null)
        {
            Response res=HttpUtils.returnBook(jedis.get("token"),id,bookId);
            String resDatas=res.body().string();
            BookBean bookBean=JSON.parseObject(resDatas,BookBean.class);
            if(bookBean.getResultCode()==200)
            {
                return "redirect:/test/selRe";
            }
            else
            {
                jedis.del("token");
                model.addAttribute("message","账户登录信息过期");
                return "login";
            }
        }
        return "login";
    }
    @RequestMapping(value = "compensate",method = RequestMethod.GET)
    public String compensate(HttpServletRequest request,Model model) throws IOException {
        String id=request.getParameter("id");
        String price=request.getParameter("price");
        Jedis jedis = new Jedis("127.0.0.1",6379);
        if(jedis.get("token")!=null)
        {
            Response res=HttpUtils.compensate(jedis.get("token"),id,price);
            String resDatas=res.body().string();
            BookBean bookBean=JSON.parseObject(resDatas,BookBean.class);
            if(bookBean.getResultCode()==200)
            {
                return "redirect:/test/myborrow";
            }
            else
            {
                jedis.del("token");
                model.addAttribute("message","账户登录信息过期");
                return "login";
            }
        }
        return "login";
    }
    @RequestMapping(value = "selRe",method = RequestMethod.GET)
    public String selRe(HttpServletRequest request,Model model) throws IOException {
        Jedis jedis = new Jedis("127.0.0.1",6379);
        if(jedis.get("token")!=null)
        {
            Response res=HttpUtils.selRe(jedis.get("token"));
            String resDatas=res.body().string();
            BookBean bookBean=JSON.parseObject(resDatas,BookBean.class);
            if(bookBean.getResultCode()==200)
            {
                User user=new User();
                if(userMessage().getResultCode()==200)
                {
                    user=userMessage().getData().getUser();
                }
                model.addAttribute("user",user);
                model.addAttribute("reBookList",bookBean.getData().getBorrows());
                return "retBook";
            }
            else
            {
                jedis.del("token");
                model.addAttribute("message","账户登录信息过期");
                return "login";
            }
        }
        return "login";

    }
    @RequestMapping(value = "signout",method = RequestMethod.GET)
    public String signout(HttpServletRequest request,Model model) throws IOException {
        Jedis jedis = new Jedis("127.0.0.1",6379);
        jedis.del("token");
        model.addAttribute("message","已退出");
        return "login";
    }
    public UserBean userMessage() throws IOException {
        Jedis jedis = new Jedis("127.0.0.1",6379);
        Response responses=HttpUtils.selOne((String) jedis.get("token"));
        String responseDatas = responses.body().string();
        UserBean userBean=JSON.parseObject(responseDatas,UserBean.class);
        return userBean;
    }
    @RequestMapping(value = "searchBook",method = RequestMethod.GET)
    public String searchBook(HttpServletRequest request,Model model) throws IOException {
        Jedis jedis = new Jedis("127.0.0.1",6379);
        String name=request.getParameter("name");
        if(jedis.get("token")!=null)
        {
            Response res=HttpUtils.searchBook(jedis.get("token"),name);
            String resDatas=res.body().string();
            BookBean bookBean=JSON.parseObject(resDatas,BookBean.class);
            if(bookBean.getResultCode()==200)
            {
                User user=new User();
                if(userMessage().getResultCode()==200)
                {
                    user=userMessage().getData().getUser();
                }
                List<Book> list=bookBean.getData().getBook();
                model.addAttribute("user",user);
                model.addAttribute("bookList",list);
                return "book";
            }
            else
            {
                jedis.del("token");
                model.addAttribute("message","账户登录信息过期");
                return "login";
            } 
        }
        return "login";
    }
    @RequestMapping(value = "change",method = RequestMethod.GET)
    public String change(HttpServletRequest request,Model model) throws IOException {
        Jedis jedis = new Jedis("127.0.0.1",6379);
        String userName=request.getParameter("userName");
        String password=request.getParameter("password");
        if(jedis.get("token")!=null)
        {
            Response res=HttpUtils.change(jedis.get("token"),userName,password);
            String resDatas=res.body().string();
            UserBean userBean=JSON.parseObject(resDatas,UserBean.class);
            if(userBean.getResultCode()==200)
            {
                jedis.del("token");
                model.addAttribute("message","更改成功,请重新登录");
                return "login";
            }
            else
            {
                jedis.del("token");
                model.addAttribute("message","账户登录信息过期");
                return "login";
            }
        }
        return "login";
    }
    @RequestMapping(value = "searchBorrow",method = RequestMethod.GET)
    public String searchBorrow(HttpServletRequest request,Model model) throws IOException {
        Jedis jedis = new Jedis("127.0.0.1",6379);
        String name=request.getParameter("name");
        if(jedis.get("token")!=null)
        {
            Response res=HttpUtils.searchBorrow(jedis.get("token"),name);
            String resDatas=res.body().string();
            BookBean bookBean=JSON.parseObject(resDatas,BookBean.class);
            if(bookBean.getResultCode()==200)
            {
                User user=new User();
                if(userMessage().getResultCode()==200)
                {
                    user=userMessage().getData().getUser();
                }
                List<Borrow> list=bookBean.getData().getBorrows();
                model.addAttribute("user",user);
                model.addAttribute("borrowList",list);
                return "myBorrow";
            }
            else
            {
                jedis.del("token");
                model.addAttribute("message","账户登录信息过期");
                return "login";
            }  
        }
        return "login";
    }
    @RequestMapping(value = "searchRe",method = RequestMethod.GET)
    public String searchRe(HttpServletRequest request,Model model) throws IOException {
        Jedis jedis = new Jedis("127.0.0.1",6379);
        String name=request.getParameter("name");
        if(jedis.get("token")!=null)
        {
            Response res=HttpUtils.searchRe(jedis.get("token"),name);
            String resDatas=res.body().string();
            BookBean bookBean=JSON.parseObject(resDatas,BookBean.class);
            if(bookBean.getResultCode()==200)
            {
                User user=new User();
                if(userMessage().getResultCode()==200)
                {
                    user=userMessage().getData().getUser();
                }
                List<Borrow> list=bookBean.getData().getBorrows();
                model.addAttribute("user",user);
                model.addAttribute("reBookList",list);
                return "retBook";
            }
            else
            {
                jedis.del("token");
                model.addAttribute("message","账户登录信息过期");
                return "login";
            }
        }
        return "login";
    }
    @RequestMapping(value = "admin",method = RequestMethod.GET)
    public String admin(HttpServletRequest request,Model model) throws IOException {
        Jedis jedis = new Jedis("127.0.0.1",6379);
        String token = jedis.get("token");
        if(token!=null)
        {
            Response res=HttpUtils.admin(jedis.get("token"));
            String resDatas=res.body().string();
            UserBean userBean=JSON.parseObject(resDatas,UserBean.class);
            if(userBean.getResultCode()==200)
            {
                return "admin";
            }
            else if(userBean.getResultCode()==300)
            {
                model.addAttribute("message","普通用户请登录");
                return "redirect:/test/";
            }
        }
        return "login";
    }
}
