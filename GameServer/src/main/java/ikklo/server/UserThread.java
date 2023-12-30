package ikklo.server;


/*
 * 用户线程
 * 只负责处理各种用户请求
 * 在多用户之间，每一个用户的不同请求，按先后顺序串行处理
 * 多用户的任务并行处理，因此每一个用户开一个线程
 */

import ikklo.pojo.ReqResult;
import ikklo.pojo.Requirement;
import ikklo.pojo.StringReference;

import java.io.EOFException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.Connection;

public class UserThread implements Runnable{
    private ObjectInputStream in;
    private ObjectOutputStream out;
    //    数据库连接
    private Connection conn;
    //    用户socket
    private Socket clientsocket;
    private StringReference uuid = new StringReference();

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
                clientsocket.setSoTimeout(1000);
                ReqResult res = req.solve(conn,uuid);
                if(res == null)res = req.solve(conn);
                if(res == null)res = req.solve(conn,uuid,in);
                if(res == null)res = req.solve(conn,uuid,out);
                clientsocket.setSoTimeout(0);
                out.writeObject(res);
            }
        }catch(EOFException e){
            System.out.println("客户端关闭连接");
        }catch (Exception e2){
            System.out.println("出错了！" + e2.getMessage());
        }
    }
}