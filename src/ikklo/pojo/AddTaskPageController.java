package ikklo.pojo;

import ikklo.sgamer.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalDate;

public class AddTaskPageController {
    User user;

    public AddTaskPageController(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @FXML
    private Button AddButton;

    @FXML
    private DatePicker DateInput;

    @FXML
    private TextField GameNameInput;

    @FXML
    private TextArea TextInput;

    @FXML
    void add(ActionEvent event) {
        try{
            LocalDate date = DateInput.getValue();
            String gamename = GameNameInput.getText();
            String text = TextInput.getText();
            UpdateTaskReq insertTaskReq = new UpdateTaskReq(user.getUsername(), user.getUuid(), gamename, text, date);
            user.send_requirement(insertTaskReq);
            int col = ((UpdateResult)user.get_result()).getColumnChanged();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
//            模态对话框
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setTitle("我去");
            if(col < 1){
                alert.setContentText("添加失败！");
            }else{
                alert.setContentText("加了，芜湖");
            }
            alert.showAndWait();
            Stage stage = (Stage)AddButton.getScene().getWindow();
            stage.close();
        }catch (Exception e){
            System.out.println(e);
        }

    }

}
