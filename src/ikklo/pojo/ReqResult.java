package ikklo.pojo;
import javafx.scene.control.Alert;


import java.io.Serializable;

/*
* 这是一个*/
public abstract class ReqResult implements Serializable {
//    这是服务器处理完需求之后返回的结果
    public abstract boolean solve();
}
/*
* 注册操作的结果
* 返回系统中的用户昵称
* 如果注册失败，返回null*/
class RegisterResult extends ReqResult {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean solve() {
        Alert al = new Alert(Alert.AlertType.INFORMATION);
        if (name == null) {
            al.setTitle("完犊子啦");
            al.setTitle(null);
            al.setContentText("注册失败");
            al.showAndWait();
            return false;
        } else {
            al.setTitle("我去");
            al.setTitle(null);
            al.setContentText("注册成功" + name);
            al.showAndWait();
        }
        return true;
    }
}
class LoginResult extends ReqResult {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean solve() {
        Alert al = new Alert(Alert.AlertType.INFORMATION);
        if (name == null) {
            al.setTitle("完犊子啦");
            al.setTitle(null);
            al.setContentText("登录失败");
            al.showAndWait();
            return false;
        } else {
            al.setTitle("我去");
            al.setTitle(null);
            al.setContentText("登录成功" + name);
            al.showAndWait();
        }
        return true;
    }
}