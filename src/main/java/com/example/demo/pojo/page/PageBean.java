package com.example.demo.pojo.page;

import com.example.demo.pojo.Book;
import com.example.demo.pojo.User;
import com.example.demo.pojo.user.DataBean;
import com.github.pagehelper.PageInfo;

public class PageBean {
    private int resultCode;
    private PageInfo<Book> pageInfo;
    private String resultString;

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public PageInfo<Book> getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo<Book> pageInfo) {
        this.pageInfo = pageInfo;
    }

    public String getResultString() {
        return resultString;
    }

    public void setResultString(String resultString) {
        this.resultString = resultString;
    }
}
