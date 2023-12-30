package ikklo.pojo;

import ikklo.pojo.ReqResult;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.Connection;

public interface CanSolve {
    /*
    * 登录和注册请求实现并调用这个方法，
    * 返回用户的昵称和uuid，用于验证
    */
    ReqResult solve(Connection conn) throws Exception;
    /*
    * 登录之后发送的请求
    * 1.查询记录
    * 2.添加记录
    * 要先验证uuid，与服务器端对应
    */
    ReqResult solve(Connection conn, StringReference uuid) throws Exception;

    ReqResult solve(Connection conn, StringReference uuid, ObjectInputStream in) throws Exception;
    ReqResult solve(Connection conn, StringReference uuid, ObjectOutputStream out) throws Exception;

}