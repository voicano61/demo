package com.example.demo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试控制器
 *
 * @author: @肖朋伟CSDN
 * @create: 2018-11-18
 */
@RestController
public class HelloController {

//    // 获取.yml 文件中值
//    @Value("${name}")
//    private String name;
//
//    // 获取 age
//    @Value("${url}")
//    private String csdnUrl;
    @Autowired
    private author author;
    //路径映射，对应浏览器访问的地址，访问该路径则执行下面函数
    @RequestMapping("/hello")
    public String hello() {
        return "name:"+author.getName() + " url："+ author.getUrl();
    }
}
