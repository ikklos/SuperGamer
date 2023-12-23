package ikklo.pojo;

import ikklo.sgamer.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class MainPageController {
    private User user;
    @FXML
    private Button AccountPageButton;

    @FXML
    private ScrollPane LeftArea;

    @FXML
    private VBox RightArea;

    @FXML
    private Button TaskPageButton;

    @FXML
    private Button ToolsPageButton;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @FXML
    void changeToAccountsPage(ActionEvent event) {

    }

    @FXML
    void changeToTaskPage(ActionEvent event) {

    }

    @FXML
    void changeToToolsPage(ActionEvent event) {

    }

}
