package ikklo.server;

import com.mysql.cj.QueryResult;

import java.rmi.server.ExportException;
import java.sql.*;
import java.util.ArrayList;

public class AppUser {
    private String username;
    private String password;
    private String name;
    private boolean accessed = false;
    private ArrayList<ServerResult> resultPool = new ArrayList<ServerResult>();
    //构造方法
    public AppUser(String username, String password) {
        this.username = username;
        this.password = password;
        this.name = username;
    }
    //getter setter
    public String getUsername() {
        return username;
    }
    private void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    private void setPassword(String password) {
        this.password = password;
    }
    public void setName(String s){
        this.name = s;
    }
    public String getName(){
        return name;
    }
    //验证登录
    public String verify(Connection conn) throws Exception {
        String ans = "";
        Statement stat = conn.createStatement();
        String sql = "SELECT password FROM user WHERE username=" + username;
        ResultSet res = stat.executeQuery(sql);
        if(!res.next()){
            ans = "本用户名未注册！";
        }
        else if(password.equals(res.getString(1))){
            ans = "验证成功！";
            this.accessed = true;
        }else{
            ans = "验证失败！";
        }
        return ans;
    }
    //注册
    private void register(Connection conn) throws Exception{
        try{
            conn.setAutoCommit(false);

            Statement stat = conn.createStatement();
            String sql = "INSERT INTO user VALUES("+username + ","+password + "," + name + ");";
            if(password.length() < 8){
                throw new RuntimeException("密码太短了");
            }
            conn.commit();
        }catch (Exception e){
            System.out.printf(e.getMessage());
            conn.rollback();
        }
    }

    //将从服务器中拿到的结果读入
    public void regist_result(ServerResult re){
        resultPool.add(re);
    }

    //返回一个结果
    public ServerResult get_result(){
        if(resultPool.isEmpty())return null;
        ServerResult re =  resultPool.get(0);

        resultPool.remove(0);
        return re;
    }
}
