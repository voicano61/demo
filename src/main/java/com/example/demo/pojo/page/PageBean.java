package com.example.demo.pojo.page;

import com.example.demo.pojo.User;
import com.example.demo.pojo.user.DataBean;
import com.github.pagehelper.PageInfo;

public class PageBean {
    private int resultCode;
    private PageInfo<User> pageInfo;
    private String resultString;

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public PageInfo<User> getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo<User> pageInfo) {
        this.pageInfo = pageInfo;
    }

    public String getResultString() {
        return resultString;
    }

    public void setResultString(String resultString) {
        this.resultString = resultString;
    }
}
