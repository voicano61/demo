package com.example.demo.pojo.book;

public class BookBean {
    private int resultCode;
    private BookDataBean data;
    private String resultString;

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public BookDataBean getData() {
        return data;
    }

    public void setData(BookDataBean data) {
        this.data = data;
    }

    public String getResultString() {
        return resultString;
    }

    public void setResultString(String resultString) {
        this.resultString = resultString;
    }
}
