package com.example.demo.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.demo.pojo.Book;
import com.example.demo.pojo.Borrow;
import com.example.demo.pojo.Transaction;
import com.example.demo.pojo.User;
import com.example.demo.pojo.book.BookBean;
import com.example.demo.pojo.book.BookDataBean;
import com.example.demo.pojo.page.PageBean;
import com.example.demo.service.BookService;
import com.example.demo.service.BorrowService;
import com.example.demo.service.TransactionService;
import com.example.demo.service.UserService;
import com.example.demo.utils.JWTUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.persistence.criteria.CriteriaBuilder;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("book")
public class BookController {
    @Autowired
    private BookService bookService;
    @Autowired
    private BorrowService borrowService;
    @Autowired
    private UserService userService;
    @Autowired
    private TransactionService transactionService;
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
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_MONTH, 10);
            Date deadLine=calendar.getTime();
            book.setDeadLine(deadLine);
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
    
    @ResponseBody
    @RequestMapping(value = "myBorrow",method = RequestMethod.POST)
    public BookBean myBorrow(HttpServletRequest request)
    {
        String token=request.getHeader("token");
        BookBean bookBean=new BookBean();
        DecodedJWT jwt = JWT.decode(token);
        int id=jwt.getClaim("id").asInt();
        Date date=new Date();
        if(JWTUtils.verify(token))
        {
            List<Borrow> bookList=this.borrowService.myBorrow(id);
            for(int i=0;i<bookList.size();i++)
            {
                long beginMillisecond = bookList.get(i).getDeadLine().getTime();
                long endMillisecond = date.getTime();
                if(endMillisecond>beginMillisecond&&bookList.get(i).getState()!=4)
                {
                    this.borrowService.timeout(bookList.get(i).getId());
                }
            }
            List<Borrow> list=this.borrowService.myBorrow(id);
            bookBean.setResultCode(200);
            bookBean.setResultString("查询成功");
            BookDataBean bookDataBean=new BookDataBean();
            bookDataBean.setBorrows(list);
            bookBean.setData(bookDataBean);
        }
        else
        {
            bookBean.setResultCode(500);
            bookBean.setResultString("查询失败");
        }
        return bookBean;
    }
    @ResponseBody
    @RequestMapping(value = "renew",method = RequestMethod.POST)
    public BookBean renew(HttpServletRequest request) throws ParseException {
        String token=request.getHeader("token");
        String id=request.getParameter("id");
        String deadLine=request.getParameter("deadLine");
        BookBean bookBean=new BookBean();
        if(JWTUtils.verify(token))
        {
            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date day = sdf.parse(deadLine);
            Calendar c = Calendar.getInstance();
            c.setTime(day);
            c.add(Calendar.DAY_OF_MONTH, 3);
            Borrow borrow=new Borrow();
            borrow.setDeadLine(c.getTime());
            borrow.setId(Integer.parseInt(id));
            borrow.setRenew(c.getTime());
            this.borrowService.renew(borrow);
            bookBean.setResultCode(200);
            bookBean.setResultString("续借成功");
            BookDataBean bookDataBean=new BookDataBean();
            bookBean.setData(bookDataBean);
        }
        else
        {
            bookBean.setResultCode(500);
            bookBean.setResultString("续借失败");
        }
        return bookBean;
    }
    @ResponseBody
    @RequestMapping(value = "selPrice",method = RequestMethod.POST)
    public BookBean selPrice(HttpServletRequest request) throws ParseException {
        String token=request.getHeader("token");
        String bookId=request.getParameter("bookId");
        BookBean bookBean=new BookBean();
        if(JWTUtils.verify(token))
        {
            DecodedJWT jwt = JWT.decode(token);
            int id=jwt.getClaim("id").asInt();
            Book book=this.bookService.selPrice(Integer.parseInt(bookId));
            User user=this.userService.selOne(id);
            if(book.getPrice()*2<=user.getMoney())
            {
                bookBean.setResultCode(200);
                bookBean.setResultString(String.valueOf(book.getPrice()*2));
            }
            else
            {
                bookBean.setResultCode(300);
                bookBean.setResultString("余额不足");
            }
            
        }
        else
        {
            bookBean.setResultCode(500);
            bookBean.setResultString("查询失败");
        }
        return bookBean;
    }
    @ResponseBody
    @RequestMapping(value = "compensate",method = RequestMethod.POST)
    public BookBean compensate(HttpServletRequest request) {
        String token=request.getHeader("token");
        String id=request.getParameter("id");
        String price=request.getParameter("price");
        BookBean bookBean=new BookBean();
        if(JWTUtils.verify(token))
        {
            DecodedJWT jwt = JWT.decode(token);
            int userId=jwt.getClaim("id").asInt();
            Transaction transaction=new Transaction();
            transaction.setUserId(userId);
            transaction.setAmount(Integer.parseInt(price));
            transaction.setState(0);
            transaction.setDate(new Date());
            this.transactionService.add(transaction);
            this.borrowService.compensate(Integer.parseInt(id));
            this.userService.upMoney(userId,Integer.parseInt(price));
            bookBean.setResultCode(200);
            bookBean.setResultString("成功");
            BookDataBean bookDataBean=new BookDataBean();
            bookBean.setData(bookDataBean);
        }
        else
        {
            bookBean.setResultCode(500);
            bookBean.setResultString("失败");
        }
        return bookBean;
    }
    @ResponseBody
    @RequestMapping(value = "returnBook",method = RequestMethod.POST)
    public BookBean returnBook(HttpServletRequest request) throws ParseException {
        String token=request.getHeader("token");
        String id=request.getParameter("id");
        String bookId=request.getParameter("bookId");
        BookBean bookBean=new BookBean();
        if(JWTUtils.verify(token))
        {
            Borrow book=new Borrow();
            book.setId(Integer.parseInt(id));
            Date date=new Date();
            book.setReturnDate(date);
            book.setBookId(Integer.parseInt(bookId));
            this.borrowService.returnBook(book);
            bookBean.setResultCode(200);
            bookBean.setResultString("成功");
            BookDataBean bookDataBean=new BookDataBean();
            bookBean.setData(bookDataBean);
        }
        else
        {
            bookBean.setResultCode(500);
            bookBean.setResultString("失败");
        }
        return bookBean;
    }
    @ResponseBody
    @RequestMapping(value = "selRe",method = RequestMethod.POST)
    public BookBean selRe(HttpServletRequest request) throws ParseException {
        String token=request.getHeader("token");
        BookBean bookBean=new BookBean();
        if(JWTUtils.verify(token))
        {
            List<Borrow> list=this.borrowService.selRe();
            bookBean.setResultCode(200);
            bookBean.setResultString("成功");
            BookDataBean bookDataBean=new BookDataBean();
            bookDataBean.setBorrows(list);
            bookBean.setData(bookDataBean);
        }
        else
        {
            bookBean.setResultCode(500);
            bookBean.setResultString("失败");
        }
        return bookBean;
    }
    @ResponseBody
    @RequestMapping(value = "searchBook",method = RequestMethod.POST)
    public BookBean searchBook(HttpServletRequest request) throws ParseException {
        String token=request.getHeader("token");
        String name=request.getParameter("name");
        BookBean bookBean=new BookBean();
        if(JWTUtils.verify(token))
        {
            List<Book> list=this.bookService.searchBook(name);
            bookBean.setResultCode(200);
            bookBean.setResultString("成功");
            BookDataBean bookDataBean=new BookDataBean();
            bookDataBean.setBook(list);
            bookBean.setData(bookDataBean);
        }
        else
        {
            bookBean.setResultCode(500);
            bookBean.setResultString("失败");
        }
        return bookBean;
    }
}
