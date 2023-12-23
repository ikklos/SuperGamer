package ikklo.pojo;

import ikklo.sgamer.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ViewController {
    private User user;

    @FXML
    private Button LoginButton;

    @FXML
    private TextField passwordInput;

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
        user.send_requirement(req);
//        如果验证通过
        if(user.get_result()){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("mainPage_task.fxml"));
            MainPageController con = new MainPageController();
//            把user对象传给controller
            con.setUser(user);
            loader.setController(con);
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) LoginButton.getScene().getWindow();
            stage.setTitle("SGamer");
            stage.setScene(scene);
            stage.show();
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
        user.get_result();
    }
}