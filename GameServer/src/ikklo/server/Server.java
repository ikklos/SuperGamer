package ikklo.server;


import ikklo.pojo.Requirement;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private InetAddress address;
    private Integer port;


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
            Thread commandThread = new Thread(()->{
                Scanner scanner= new Scanner(System.in);
                while(true){
                    System.out.println("可以输入指令（stop）停止");
                    String command = scanner.nextLine().trim().toLowerCase();
                    if(command.equals("stop")){
                        System.out.println("服务器正在停止");
//                        关闭线程池
                        executorService.shutdownNow();
//                        关闭socket
                        try {
                            serversocket.close();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        System.out.println("成功停止");
                        break;
                    }else {
                        System.out.println("错误指令");
                    }
                }

            });
            commandThread.start();
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
