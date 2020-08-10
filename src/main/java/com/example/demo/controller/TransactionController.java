package com.example.demo.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.demo.pojo.Transaction;
import com.example.demo.pojo.transaction.TransactionDataBean;
import com.example.demo.pojo.transaction.TransactionBean;
import com.example.demo.service.TransactionService;
import com.example.demo.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.List;

@Controller
@RequestMapping("transaction")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;
    @ResponseBody
    @RequestMapping(value = "allTransaction",method = RequestMethod.POST)
    public TransactionBean allTransaction(HttpServletRequest request) throws ParseException {
        String token=request.getHeader("token");
        String states=request.getParameter("state");
        int state=3;
        if(states!=null&&!states.equals(""))
        {
            state=Integer.parseInt(states);
        }
        TransactionBean transactionBean=new TransactionBean();
        if(JWTUtils.verify(token))
        {
            DecodedJWT jwt = JWT.decode(token);
            int userId=jwt.getClaim("id").asInt();
            List<Transaction> list=this.transactionService.allTransaction(userId,state);
            transactionBean.setResultCode(200);
            transactionBean.setResultString("成功");
            TransactionDataBean dataBean=new TransactionDataBean();
            dataBean.setTransactionList(list);
            transactionBean.setData(dataBean);
        }
        else
        {
            transactionBean.setResultCode(500);
            transactionBean.setResultString("失败");
        }
        return transactionBean;
    }
}
