package com.example.demo.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.demo.pojo.Book;
import com.example.demo.pojo.Borrow;
import com.example.demo.pojo.User;
import com.example.demo.pojo.book.BookBean;
import com.example.demo.pojo.book.BookDataBean;
import com.example.demo.pojo.page.PageBean;
import com.example.demo.service.BookService;
import com.example.demo.service.BorrowService;
import com.example.demo.utils.JWTUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("book")
public class BookController {
    @Autowired
    private BookService bookService;
    @Autowired
    private BorrowService borrowService;
    @ResponseBody
    @RequestMapping(value = "selAllBook",method = RequestMethod.POST)
    public BookBean selAllBook(HttpServletRequest request)
    {
        String token=request.getHeader("token");
        BookBean bookBean=new BookBean();
        if(JWTUtils.verify(token))
        {
            List<Book> list=this.bookService.selAllBook();
            bookBean.setResultCode(200);
            bookBean.setResultString("查询成功");
            BookDataBean dataBean=new BookDataBean();
            dataBean.setBook(list);
            bookBean.setData(dataBean);
        }
        else
        {
            bookBean.setResultCode(500);
            bookBean.setResultString("查询失败");
        }
        return bookBean;
    }
    @ResponseBody
    @RequestMapping(value = "selBook",method = RequestMethod.POST)
    public BookBean selBook(HttpServletRequest request)
    {
        String token =request.getHeader("token");
        BookBean bookBean=new BookBean();
        if(JWTUtils.verify(token))
        {
            List<Book> list=this.bookService.selBook();
            bookBean.setResultCode(200);
            bookBean.setResultString("查询成功");
            BookDataBean dataBean=new BookDataBean();
            dataBean.setBook(list);
            bookBean.setData(dataBean);
        }
        else
        {
            bookBean.setResultCode(500);
            bookBean.setResultString("查询失败");
        }
        return bookBean;
    }
    
    @ResponseBody
    @RequestMapping("page")
    public PageBean page(HttpServletRequest request,int pageNum)
    {
        String token=request.getHeader("token");
        PageBean pageBean=new PageBean();
        if (JWTUtils.verify(token))
        {
            PageHelper.startPage(pageNum,3);
            List<Book> blogList = this.bookService.selBook();
            PageInfo<Book> pageInfo = new PageInfo<Book>(blogList);
            pageBean.setResultCode(200);
            pageBean.setResultString("成功");
            pageBean.setPageInfo(pageInfo);
        }
        else
        {
            pageBean.setResultCode(500);
            pageBean.setResultString("失败");
        }
        return pageBean;
    }
    @ResponseBody
    @RequestMapping(value = "borrow",method = RequestMethod.POST)
    public BookBean borrow(HttpServletRequest request)
    {
        String token=request.getHeader("token");
        BookBean bookBean=new BookBean();
        if (JWTUtils.verify(token)) {
            Borrow book=new Borrow();
            int bookId=Integer.parseInt(request.getParameter("bookId"));
            String bookName=request.getParameter("bookName");
            String author=request.getParameter("author");
            Date date=new Date();
            DecodedJWT jwt = JWT.decode(token);
            int id=jwt.getClaim("id").asInt();
            book.setAuthor(author);
            book.setBookId(bookId);
            book.setBookName(bookName);
            book.setDate(date);
            book.setUserId(id);
            this.borrowService.borrow(book);
            bookBean.setResultCode(200);
            bookBean.setResultString("借阅成功");
            
        }
        else
        {
            bookBean.setResultCode(500);
            bookBean.setResultString("借阅失败");
        }
        return bookBean;
    }
}
