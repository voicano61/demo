package com.example.demo.utils;

import com.alibaba.fastjson.JSONObject;
import okhttp3.*;

import java.io.IOException;

public class HttpUtils {
    //登录
    public static Response login(String username, String password) throws IOException {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("username",username)
                .add("password",password)
                .build();
        Request request = new Request.Builder()
                .url("http://localhost:8080/user/login")
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response;
    }
    
    //列表
    public static Response show(String token) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://localhost:8080/user/list")
                .post(okhttp3.internal.Util.EMPTY_REQUEST)
                .header( "token",token )
                .build();
        Response response = client.newCall(request).execute();
        return response;
    }
    
    //注册
    public static Response register(String username,String password) throws IOException {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("username",username)
                .add("password",password)
                .build();
        Request request = new Request.Builder()
                .url("http://localhost:8080/user/register")
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response;
    }
    //判断用户名是否存在
    public static Response decide(String username) throws IOException {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("username",username)
                .build();
        Request request = new Request.Builder()
                .url("http://localhost:8080/user/decide")
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response;
    }

    public static Response showInfo(String token) throws IOException {
        OkHttpClient client = new OkHttpClient();;
        Request request = new Request.Builder()
                .url("http://localhost:8080/user/showInfo")
                .post(okhttp3.internal.Util.EMPTY_REQUEST)
                .header( "token",token )
                .build();
        Response response = client.newCall(request).execute();
        return response;
    }
    public static Response update(String token,String username,String password) throws IOException {
        OkHttpClient client = new OkHttpClient();
        RequestBody body= new FormBody.Builder()
                    .add("username",username)
                    .add("password",password)
                    .build();
        Request request = new Request.Builder()
                .url("http://localhost:8080/user/update")
                .post(body)
                .header( "token",token )
                .build();
        Response response = client.newCall(request).execute();
        return response;
    }
    public static Response allBook(String token) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://localhost:8080/book/selBook")
                .post(okhttp3.internal.Util.EMPTY_REQUEST)
                .header( "token",token )
                .build();
        Response response = client.newCall(request).execute();
        return response;
    }

    public static Response page(String token,int pageNum) throws IOException {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("pageNum", String.valueOf(pageNum))
                .build();
        Request request = new Request.Builder()
                .url("http://localhost:8080/book/page")
                .post(body)
                .header("token",token)
                .build();
        Response response = client.newCall(request).execute();
        return response;
    }
    public static Response borrow(String token,String bookName,String author,String bookId) throws IOException {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("bookId", bookId)
                .add("bookName",bookName)
                .add("author",author)
                .build();
        Request request = new Request.Builder()
                .url("http://localhost:8080/book/borrow")
                .post(body)
                .header("token",token)
                .build();
        Response response = client.newCall(request).execute();
        return response;
    }
    
    public static Response myBorrow(String token) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://localhost:8080/book/myBorrow")
                .post(okhttp3.internal.Util.EMPTY_REQUEST)
                .header("token",token)
                .build();
        Response response = client.newCall(request).execute();
        return response;
    }

    public static Response renew(String token,String id,String deadLine) throws IOException {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("id", id)
                .add("deadLine",deadLine)
                .build();
        Request request = new Request.Builder()
                .url("http://localhost:8080/book/renew")
                .post(body)
                .header("token",token)
                .build();
        Response response = client.newCall(request).execute();
        return response;
    }
    public static Response selPrice(String token,String bookId) throws IOException {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("bookId",bookId)
                .build();
        Request request = new Request.Builder()
                .url("http://localhost:8080/book/selPrice")
                .post(body)
                .header("token",token)
                .build();
        Response response = client.newCall(request).execute();
        return response;
    }
    public static Response returnBook(String token,String id,String bookId) throws IOException {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("id", id)
                .add("bookId",bookId)
                .build();
        Request request = new Request.Builder()
                .url("http://localhost:8080/book/returnBook")
                .post(body)
                .header("token",token)
                .build();
        Response response = client.newCall(request).execute();
        return response;
    }
    public static Response compensate(String token,String id,String price) throws IOException {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("id", id)
                .add("price",price)
                .build();
        Request request = new Request.Builder()
                .url("http://localhost:8080/book/compensate")
                .post(body)
                .header("token",token)
                .build();
        Response response = client.newCall(request).execute();
        return response;
    }
    public static Response selRe(String token) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://localhost:8080/book/selRe")
                .post(okhttp3.internal.Util.EMPTY_REQUEST)
                .header("token",token)
                .build();
        Response response = client.newCall(request).execute();
        return response;
    }
    public static Response selOne(String token) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://localhost:8080/user/selOne")
                .post(okhttp3.internal.Util.EMPTY_REQUEST)
                .header("token",token)
                .build();
        Response response = client.newCall(request).execute();
        return response;
    }
    public static Response searchBook(String token,String name) throws IOException {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("name", name)
                .build();
        Request request = new Request.Builder()
                .url("http://localhost:8080/book/searchBook")
                .post(body)
                .header("token",token)
                .build();
        Response response = client.newCall(request).execute();
        return response;
    }
    public static Response change(String token,String userName,String password) throws IOException {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("userName", userName)
                .add("password",password)
                .build();
        Request request = new Request.Builder()
                .url("http://localhost:8080/user/change")
                .post(body)
                .header("token",token)
                .build();
        Response response = client.newCall(request).execute();
        return response;
    }
}
