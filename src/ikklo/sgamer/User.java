package ikklo.sgamer;

import ikklo.pojo.ReqResult;
import ikklo.server.CanSolve;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class User {
    private String username;
    private String password;
    private String name;


    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Stage nowstage;
/*
* getter 和 setter
* 我也不知道有什么用，但我先放一个在这里
*/
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Stage getNowstage() {
        return nowstage;
    }

    public void setNowstage(Stage nowstage) {
        this.nowstage = nowstage;
    }

    /*
* 构造方法
* */
    public User(){

    }
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /*
    * 呼呼，我要狠狠地连服务器口牙
    * 1.连接服务器拿到socket
    * 2.提示连接成功
    * 3.拿到输入输出流
    */
    public void connect_to_server() throws Exception{
        socket = new Socket("localhost",9999);
        System.out.println("我去，连上了");
        System.out.println(socket);
//        获取输入输出流
//        一定要先获取输出流，不然会卡住
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());

        System.out.println("成功获取输入输出流");
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    //    客户端：处理请求
    /*
    * 准确的说是初步处理请求以及得到请求结果后进行相应的操作
    * 1.建立请求（逻辑处理）
    * 2.发送请求
    * 3.获取结果
    * 4.逻辑处理
    */
    public <T extends CanSolve> void send_requirement(T req) throws Exception {
        System.out.println("正在发送请求");
        out.writeObject(req);
    }
//    获取结果并处理
//    逻辑处理部分写在结果的solve函数中
    public boolean get_result(){
        ReqResult res = new ReqResult() {
            @Override
            public boolean solve() {
                return false;
            }
        };
        try{
//            拿到结果
            res = (ReqResult) in.readObject();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
//        处理结果并返回是否通过
        return res.solve();
    }
    public void UserClose() throws IOException {
        socket.close();
        System.out.println("我似了");
    }
}
