package com.example.demo.pojo.transaction;

public class TransactionBean {
    private int resultCode;
    private TransactionDataBean data;
    private String resultString;

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public TransactionDataBean getData() {
        return data;
    }

    public void setData(TransactionDataBean data) {
        this.data = data;
    }

    public String getResultString() {
        return resultString;
    }

    public void setResultString(String resultString) {
        this.resultString = resultString;
    }
}
