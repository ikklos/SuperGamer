package ikklo.server;

import com.mysql.cj.QueryResult;

import java.sql.Connection;
import java.sql.DriverManager;

public class GamerServer {
    public static void main(String[] args) throws Exception {
        //注册驱动
        Class.forName("com.mysql.cj.jdbc.Driver");
        //获取连接
        String url = "jdbc:mysql://127.0.0.1:3306/supergamerdb";
        String user = "root";
        String password = "693147xx";
        Connection cnn = DriverManager.getConnection(url,user,password);

        System.out.println(cnn);

        //sql语句
        cnn.close();
    }
}