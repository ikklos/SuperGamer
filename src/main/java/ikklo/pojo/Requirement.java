package ikklo.pojo;


import javafx.stage.Stage;

import javax.swing.plaf.nimbus.State;
import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.*;

/*
* 子类可以选择实现其中任意一种方法
*/
public class Requirement implements CanSolve, Serializable {
    @Override
    public ReqResult solve(Connection conn)throws Exception {
        return null;
    }

    @Override
    public ReqResult solve(Connection conn, StringReference uuid) throws Exception {
        return null;
    }

    @Override
    public ReqResult solve(Connection conn, StringReference uuid, ObjectInputStream in) throws Exception {
        return null;
    }
    public ReqResult solve(Connection conn, StringReference uuid, ObjectOutputStream out) throws Exception {
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
            conn.setAutoCommit(true);
            res.setName(name);
        }catch (Exception e){
            System.out.println("插入出错"+e.getMessage());
            conn.rollback();
            conn.setAutoCommit(true);
            res.setName(null);
        }
        return res;
    }
}
/*
*   登录请求
*   1.账号密码
*   2.在数据库中寻找相关的记录
*   3.如果匹配，则返回用户信息和，不匹配则显示登录失败
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
    public LoginResult solve(Connection conn, StringReference uuid) throws Exception{
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
                if(respassword.equals(password)){
                    re.setName(res.getString("name"));
                    String str;
                    re.setUuid(str = UUID.randomUUID().toString());
                    uuid.setStr(str);
                }
                else re.setName(null);
            }else{
                re.setName(null);
            }
            conn.commit();
            conn.setAutoCommit(true);
        }catch (Exception e){
            System.out.println("查询出错"+e.getMessage());
            conn.rollback();
            conn.setAutoCommit(true);
            re.setName(null);
            return null;
        }
//        返回用户信息
        System.out.println(re.getUuid());
        return re;
    }
}
/*
* 在页面上显示记录，首先就要查询记录并返回一个ResultSet
* 然后在Controller处排版即可
*/
//QueryReq只能返回单一列的数据
class QueryReq extends Requirement{
    private String username;
    private String uuid;
    private String sql;
    List<String> cols;
    public QueryReq(String username, String uuid, String sql) {
        this.username = username;
        this.uuid = uuid;
        this.sql = sql;
        this.cols = new ArrayList<>();
    }

    public List<String> getCols() {
        return cols;
    }

    public void setCols(String... cols) {
        this.cols = List.of(cols);
    }

    @Override
    public QueryResult solve(Connection conn, StringReference serverside) throws Exception {
        QueryResult queryResult = new QueryResult(null);
//        首先验证uuid
        if(!serverside.getStr().equals(uuid)){
            return queryResult;
        }
//        验证通过,开始处理请求
        try{
            conn.setAutoCommit(false);
            Statement stat = conn.createStatement();

            ResultSet res = stat.executeQuery(sql);
            List<Map<String,String>> maps = new ArrayList<>();
            while(res.next()){
                Map<String, String> stringStringMap = new HashMap<>();
                for(String col: this.cols){
                    stringStringMap.put(col,res.getString(col));
                }
                maps.add(stringStringMap);
            }
            queryResult.setRecords(maps);
            conn.commit();
            conn.setAutoCommit(true);
        }catch (Exception e){
            conn.rollback();
            conn.setAutoCommit(true);
            throw new Exception(e);
//            如果发生异常，返回null
            //return new QueryResult(null);
        }
        return queryResult;
    }
}

class UpdateReq extends Requirement{
    private String username;
    private String uuid;
    private String sql;

    public UpdateReq(String username, String uuid, String sql) {
        this.username = username;
        this.uuid = uuid;
        this.sql = sql;
    }

    @Override
    public UpdateResult solve(Connection conn, StringReference serverside) throws Exception {
        UpdateResult updateResult = new UpdateResult(0);
//        首先验证uuid
        if(!serverside.getStr().equals(uuid)){
            return updateResult;
        }
//        验证通过,开始处理请求
        try{
            conn.setAutoCommit(false);
            Statement stat = conn.createStatement();

            updateResult.setColumnChanged(stat.executeUpdate(sql));

            conn.commit();
            conn.setAutoCommit(true);
        }catch (Exception e){
            conn.rollback();
            conn.setAutoCommit(true);
//            如果发生异常，返回null
            System.out.println(e);
            return new UpdateResult(0);
        }
        return updateResult;
    }
}
class UpdateTaskReq extends UpdateReq {
    private String gamename;
    public UpdateTaskReq(String username, String uuid, String sql) {
        super(username, uuid, sql);
    }
    public UpdateTaskReq(String username, String uuid, String gamename, String text, LocalDate date){
        super(username,uuid,"Insert into tasks(username,gamename,contant,time) values('" + username + "','" + gamename + "','"+ text + "','" + date + "')");
        this.gamename = gamename;
    }

    @Override
    public UpdateResult solve(Connection conn, StringReference serverside) throws Exception {
//        先检查game表里有没有这个游戏，如果没有，先加一个游戏
        try{
            conn.setAutoCommit(false);
            Statement stat = conn.createStatement();
            String str = "SELECT * FROM GAME WHERE GAMENAME='" + gamename + "'";
            ResultSet res = stat.executeQuery(str);
            if(!res.next()){
                str = "INSERT INTO GAME VALUES('" + gamename + "')";
                stat.executeUpdate(str);
            }
            conn.commit();
            conn.setAutoCommit(true);
        }catch (Exception e){
            System.out.println("用户请求处理错误！" + e.getMessage());
            conn.rollback();
            conn.setAutoCommit(true);
            return new UpdateResult(0);
        }

        return super.solve(conn, serverside);
    }
}
class UploadReq extends Requirement{
    private String username;
    private String uuid;
    private String name;
    private String filename;
    private String gamename;
    private String note = "";

    public UploadReq(String username, String uuid, String name, String filename, String gamename) {
        this.username = username;
        this.uuid = uuid;
        this.name = name;
        this.filename = filename;
        this.gamename = gamename;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public UploadResult solve(Connection conn, StringReference serverside, ObjectInputStream in) throws Exception {
        File f = new File("C://Users/ikklo/Downloads/" + username + "/" + filename);
        if(!serverside.getStr().equals(uuid)){
            clear_stream(in);
            return new UploadResult(false);
        }
        try{
            try{
                conn.setAutoCommit(false);
                String sql = "INSERT INTO gametool(username,gamename,name,url,note) values('" + username + "','" + gamename + "','" + name + "','" + filename + "','" +
                                note + "')";
                Statement stat = conn.createStatement();
                int col = stat.executeUpdate(sql);
                if(col < 1)throw new RuntimeException("添加文件失败！");
            }catch (Exception e){
                throw new Exception(e);
            }
            System.out.println("获取输入流");
            ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
            FileOutputStream fileOutputStream;
            try{
                fileOutputStream = new FileOutputStream("C://Users/ikklo/Downloads/" + username + "/" + filename);
            }catch (Exception e){
//                如果没有这个目录先创建一个
                Path path = Paths.get("C://Users/ikklo/Downloads/" + username + "/" + filename);
                try {
                    Files.createDirectories(path.getParent());
                } catch (IOException e2) {
                    e.printStackTrace();
                }
                fileOutputStream = new FileOutputStream("C://Users/ikklo/Downloads/" + username + "/" + filename);
            }
            System.out.println("获取地址");
            FileChannel channel = fileOutputStream.getChannel();
            FilePackage pac;
            System.out.println("开始传输");
            while(true){
                pac = (FilePackage) in.readObject();
//                System.out.println("读取一次");
                buffer.put(pac.getData());
                buffer.flip();
                channel.write(buffer);
                buffer.clear();
                if(pac.isEnd())break;
            }
            System.out.println("传输完毕");
            clear_stream(in);
            channel.close();
            conn.commit();
            conn.setAutoCommit(true);
            return new UploadResult(true);
        }catch (Exception e){
            f.delete();
            conn.rollback();
            conn.setAutoCommit(true);
            System.out.println("传输文件出错！" + e.getMessage());
            clear_stream(in);
            return new UploadResult(false);
        }
    }
    private void clear_stream(ObjectInputStream in){
        while(true){
            try {
                Object pac = in.readObject();
            } catch (Exception e){
                return;
            }
        }
    }
}
class DownLoadReq extends Requirement{
    private String username;
    private String uuid;

    private int fileid;

    public DownLoadReq(String username, String uuid, int fileid) {
        this.username = username;
        this.uuid = uuid;
        this.fileid = fileid;
    }

    @Override
    public DownloadResult solve(Connection conn, StringReference serverside, ObjectOutputStream out) throws Exception {
        if(!serverside.getStr().equals(uuid)){
            System.out.println("用户未登录！");
            return new DownloadResult(false);
        }
        String sql = "SELECT * FROM gametool WHERE id = " + fileid;
        try{
            conn.setAutoCommit(false);
            Statement stat = conn.createStatement();
            ResultSet set = stat.executeQuery(sql);
            if(!set.next()){
                return new DownloadResult(false);
            }
//            拿到文件名
            String filename = set.getString("url");
            String url = "C://Users/ikklo/Downloads/" + username + "/" + filename;
            File f = new File(url);
            FileInputStream fileInputStream = new FileInputStream(f);
            FileChannel channel = fileInputStream.getChannel();
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024);
            out.flush();
            while(channel.read(byteBuffer) != -1){
                byteBuffer.flip();
                System.out.println(byteBuffer.remaining());
                byte[] buffer = new byte[byteBuffer.remaining()];
                byteBuffer.get(buffer);
                FilePackage pac = new FilePackage(buffer);
//                System.out.println("读取一次");
                out.writeObject(pac);
                byteBuffer.clear();
            }
            FilePackage endpac = new FilePackage(new byte[0]);
            endpac.setEnd(true);
            out.writeObject(endpac);
            conn.commit();
            conn.setAutoCommit(true);
        }catch (Exception e){
            conn.rollback();
            conn.setAutoCommit(true);
            e.printStackTrace();
            return new DownloadResult(false);
        }

        return new DownloadResult(true);
    }
}
class DeleteReq extends Requirement{
    private String username;
    private String uuid;
    private String sql;
    private String filename;

    public DeleteReq(String username, String uuid, String sql, String filename) {
        this.username = username;
        this.uuid = uuid;
        this.sql = sql;
        this.filename = filename;
    }

    @Override
    public DeleteResult solve(Connection conn, StringReference serverside) throws Exception {
        if(!serverside.getStr().equals(uuid)){
            System.out.println("用户未登录");
            return new DeleteResult(false);
        }
        try{
            conn.setAutoCommit(false);
            Statement stat = conn.createStatement();
            int col = stat.executeUpdate(sql);
            if(col < 1)throw new RuntimeException("删除失败");
            File f = new File("C://Users/ikklo/Downloads/" + username + "/" + filename);
            if(!f.delete())throw new RuntimeException("删除失败");
            conn.commit();
            conn.setAutoCommit(true);
        }catch (Exception e){
            System.out.println(e);
            conn.rollback();
            conn.setAutoCommit(true);
            return new DeleteResult(false);
        }
        return new DeleteResult(true);
    }
}