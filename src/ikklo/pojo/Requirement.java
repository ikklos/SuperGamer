package ikklo.pojo;

import ikklo.server.CanSolve;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Requirement implements CanSolve, Serializable {
    @Override
    public ReqResult solve(Connection conn)throws Exception {
        return null;
    }
}
//注册请求
class RegisterReq extends Requirement{
    private String username;
    private String password;
    private String name;

    public RegisterReq(String username, String password) {
        this.username = username;
        this.password = password;
        this.name = username;
    }

    public RegisterReq(String username, String password, String name) {
        this.username = username;
        this.password = password;
        this.name = name;
    }

    @Override
    public RegisterResult solve(Connection conn) throws Exception{
        RegisterResult res = new RegisterResult();
        try{
            conn.setAutoCommit(false);

            Statement stat = conn.createStatement();
            java.lang.String sql = "INSERT INTO user VALUES(\""+username + "\",\""+password + "\", \"" + name + "\")";
            var col = stat.executeUpdate(sql);
            if(col < 1)throw new RuntimeException("注册失败！也许你已经有一个帐号了");
            conn.commit();
            res.setName(name);
        }catch (Exception e){
            System.out.println("插入出错"+e.getMessage());
            conn.rollback();
            res.setName(null);
        }
        return res;
    }
}
/*
*   登录请求
*   1.账号密码
*   2.在数据库中寻找相关的记录
*   3.如果匹配，则返回用户信息，不匹配则显示登录失败
*/
class LoginReq extends Requirement{
//    账号密码
    private String username;
    private String password;

    public LoginReq(String username, String password) {
        this.username = username;
        this.password = password;
    }
//
//    solve方法，返回一个LoginResult对象
    public LoginResult solve(Connection conn) throws Exception{
        LoginResult re = new LoginResult();
        try{
            conn.setAutoCommit(false);
            Statement stat = conn.createStatement();
//            在数据库中寻找相关记录
            String sql = "SELECT password,name FROM USER WHERE USERNAME="+"\"" + username + "\"";
            System.out.println(sql);
            ResultSet res = stat.executeQuery(sql);
            if(res.next()){
                String respassword =  res.getString("password");
                if(respassword.equals(password))re.setName(res.getString("name"));
                else re.setName(null);
            }else{
                re.setName(null);
            }
        }catch (Exception e){
            System.out.println("查询出错"+e.getMessage());
            conn.rollback();
            re.setName(null);
        }
//        返回用户信息
        return re;
    }
}