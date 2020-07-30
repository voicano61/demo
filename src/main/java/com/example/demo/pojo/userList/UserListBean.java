package com.example.demo.pojo.userList;

public class UserListBean {
    private int resultCode;
    private UserDataBean data;
    private String resultString;

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public UserDataBean getData() {
        return data;
    }

    public void setData(UserDataBean data) {
        this.data = data;
    }

    public String getResultString() {
        return resultString;
    }

    public void setResultString(String resultString) {
        this.resultString = resultString;
    }
}
