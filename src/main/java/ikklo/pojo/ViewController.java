package ikklo.pojo;

import ikklo.sgamer.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ViewController {
    private User user;

    @FXML
    private Button LoginButton;

    @FXML
    private PasswordField passwordInput;

    @FXML
    private Button registButton;

    @FXML
    private TextField userNameInput;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @FXML
    void login(ActionEvent event) throws Exception {
        user.setUsername(userNameInput.getText());
        user.setPassword(passwordInput.getText());
        if(user.getUsername().length() < 4 || user.getUsername().length() > 16
        ||user.getPassword().length() < 8) {
            Alert al = new Alert(Alert.AlertType.INFORMATION);
            al.setTitle("寄！");
            al.setContentText("用户名长度应该在8到16之间，密码长度应该在8字符以上");
            al.showAndWait();
            return;
        }
        LoginReq req = new LoginReq(user.getUsername(),user.getPassword());
        System.out.println("Controller : " + req);
        user.send_requirement(req);
        LoginResult res = (LoginResult)user.get_result();
//        如果验证通过
        /*
        * 加载页面
        */
        if(!res.getUuid().isEmpty()){
            FXMLLoader loader = new FXMLLoader(ViewController.class.getClassLoader().getResource("resource/template/mainPage_task.fxml"));
            user.setUuid(res.getUuid());
            TaskPageController con = new TaskPageController(user);
//            把user对象传给controller
            con.setUser(user);
            loader.setController(con);
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) LoginButton.getScene().getWindow();
            stage.setTitle("SGamer");
            stage.setScene(scene);
            stage.show();
        }else{
            Alert al = new Alert(Alert.AlertType.INFORMATION);
            al.setTitle("寄！");
            al.setContentText("登陆失败！");
            al.showAndWait();
            return;
        }
    }
    @FXML
    void register(ActionEvent event) throws Exception {
        user.setUsername(userNameInput.getText());
        user.setPassword(passwordInput.getText());
        if(user.getUsername().length() < 4 || user.getUsername().length() > 16
                ||user.getPassword().length() < 8) {
            Alert al = new Alert(Alert.AlertType.INFORMATION);
            al.setTitle("寄！");
            al.setContentText("用户名长度应该在8到16之间，密码长度应该在8字符以上");
            al.showAndWait();
            return;
        }
        RegisterReq req = new RegisterReq(user.getUsername(),user.getPassword());
        user.send_requirement(req);
        RegisterResult res = (RegisterResult)user.get_result();
        res.solve();
    }
}