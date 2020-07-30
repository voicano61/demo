package com.example.demo.controller_front;

import com.alibaba.fastjson.JSON;
import com.example.demo.pojo.User;
import com.example.demo.pojo.user.UserBean;
import com.example.demo.pojo.userList.UserListBean;
import com.example.demo.utils.HttpUtils;
import com.example.demo.utils.RedisUtil;
import okhttp3.Response;
import org.aspectj.weaver.patterns.IToken;
import org.springframework.data.repository.config.RepositoryNameSpaceHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Controller
@RequestMapping("test")
public class TController {

    @Resource
    private RedisUtil redisUtil;
    @RequestMapping("/")
    public String welcome()
    {
        return "login";
    }
    
    @RequestMapping(value = "login" ,method = RequestMethod.POST)
    public String  login(HttpServletRequest request, Model model) throws IOException {
        String username=request.getParameter("name");
        String password=request.getParameter("password");
        Response response=HttpUtils.login(username, password);
        String responseData = response.body().string();
        UserBean info=JSON.parseObject( responseData,UserBean.class);
        if(info.getResultCode()==200)
        {
            redisUtil.set("token",info.getData().getToken());
//            System.out.println(redisUtil.get("token"));
            Response responses=HttpUtils.show((String) redisUtil.get("token"));
            String responseDatas = responses.body().string();
            UserListBean userListBean=JSON.parseObject(responseDatas,UserListBean.class);
            model.addAttribute("userList",userListBean.getData().getUserList());
            return "index";
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
        String token=(String)redisUtil.get("token");
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

            return "redirect:error";
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
        String username=request.getParameter("username");
        String password=request.getParameter("password");
        System.out.println(username+password);
        String token=(String)redisUtil.get("token");
//        System.out.println(token);
        Response responses=HttpUtils.showInfo(token);
        String responseDatas = responses.body().string();
        UserBean infos=JSON.parseObject( responseDatas,UserBean.class);
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

            return "redirect:error";
        }
    }
}
