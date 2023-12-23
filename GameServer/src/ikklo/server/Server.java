package ikklo.server;


import ikklo.pojo.Requirement;

import java.io.EOFException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    InetAddress address;
    Integer port;


    public Server(InetAddress address, Integer port) {
        this.port = port;
        this.address = address;
    }
    /*
    * 启动服务器
    * 1.连接database
    * 2.监听端口
    * 3.当有用户接入时开一个线程
    */
    public void runServer(String database, String admin , String password) throws Exception{
        Class.forName("com.mysql.cj.jdbc.Driver");
//        获取连接
        Connection conn = DriverManager.getConnection(database,admin,password);
        System.out.println("获取数据库连接成功！");
//        线程池
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        try(ServerSocket serversocket = new ServerSocket(9999)){
            System.out.println("正在监听9999端口...");
            System.out.println(serversocket);
            while(true){
                Socket clientsocket = serversocket.accept();
                System.out.println("有客户端连接了！");
                System.out.println(clientsocket);
                executorService.submit(new UserThread(conn,clientsocket));
            }
        }catch (Exception e){
            System.out.printf(e.getMessage());
        }
        finally {
            executorService.shutdown();
            System.out.println("哎我怎么似了");
        }
//        关闭数据库连接
        conn.close();
        System.out.println("我趣，我被关数据库外面了！");
    }
}

/*
 * 用户线程
 * 只负责处理各种用户请求
 * 在多用户之间，每一个用户的不同请求，按先后顺序串行处理
 * 多用户的任务并行处理，因此每一个用户开一个线程
 */

class UserThread implements Runnable{
    ObjectInputStream in;
    ObjectOutputStream out;
//    数据库连接
    Connection conn;
//    用户socket
    Socket clientsocket;

    public UserThread(Connection conn, Socket clientsocket) {
        this.conn = conn;
        this.clientsocket = clientsocket;
    }
/*
* 1.拿到输入输出流
* 2.循环地拿取并处理请求
* 3.关闭线程
*/
    @Override
    public void run() {
        try{
            System.out.println("没关系了，用户线程跑起来了");
//            拿到输入输出流
            /*
            * 注意此处
            * 一定要先获取输出流，要不然会卡住
            */
            out = new ObjectOutputStream(clientsocket.getOutputStream());
            in = new ObjectInputStream(clientsocket.getInputStream());
            System.out.println("成功获取输入输出流");
            while(true){
//                如果用户关闭客户端，则结束线程
                if(clientsocket.isClosed())break;
                Requirement req = (Requirement) in.readObject();
                System.out.println(req);
//                把结果写入输出流
                out.writeObject(req.solve(conn));
            }


        }catch(EOFException e){
            System.out.println("客户端关闭连接");

        }catch (Exception e2){
            System.out.println("出错了！" + e2.getMessage());
        }
    }
}