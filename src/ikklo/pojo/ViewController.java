package ikklo.pojo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class ViewController {

    @FXML
    private Button LoginButton;

    @FXML
    private TextField passwordInput;

    @FXML
    private Button registButton;

    @FXML
    private TextField userNameInput;

    @FXML
    void login(ActionEvent event) {
        System.out.println("点了登录！");
    }
}