package ikklo.pojo;

import ikklo.sgamer.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;
import java.util.Map;

public class AccountDetailPageController {
    User user;
    int id;
    public AccountDetailPageController(User user, int id) {
        this.user = user;
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    @FXML
    private Button ChangeButton;

    @FXML
    private Button DeleteButton;

    @FXML
    private TextField passwordField;

    @FXML
    private TextField usernameField;

    @FXML
    private void initialize(){
        String sql = "SELECT * FROM accounts WHERE id = " + id;
        QueryReq queryReq = new QueryReq(user.getUsername(),user.getUuid(),sql);
        queryReq.setCols("accountname","loginpassword");
        try{
            user.send_requirement(queryReq);
            QueryResult queryResult= (QueryResult) user.get_result();
            Map<String,String> map = queryResult.getRecords().get(0);

            if(map != null){
                passwordField.setText(map.get("loginpassword"));
                usernameField.setText(map.get("accountname"));
            }
        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setTitle("寄!");
            alert.setContentText("发生错误" + e.getMessage());
        }

    }
    @FXML
    void changeaccount(ActionEvent event) {
        String sql = "UPDATE accounts SET accountname = '" + usernameField.getText() + "',loginpassword = '" +passwordField.getText() +  "' WHERE id = "+id;
        UpdateReq updateReq = new UpdateReq(user.getUsername(), user.getUuid(), sql);
        try{
            user.send_requirement(updateReq);
            UpdateResult res = (UpdateResult) user.get_result();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            if(res.getColumnChanged() < 1){
                alert.setTitle("我去");
                alert.setContentText("没改，咋回事呢");
            }else{
                alert.setTitle("我去");
                alert.setContentText("修改成功");
            }
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.showAndWait();
        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setTitle("寄!");
            alert.setContentText("发生错误" + e.getMessage());
        }
    }

    @FXML
    void delete(ActionEvent event) {
        String sql = "DELETE FROM ACCOUNTS WHERE id = " + id;
        UpdateReq updateReq = new UpdateReq(user.getUsername(), user.getUuid(), sql);
        Stage stage = (Stage) DeleteButton.getScene().getWindow();
        try{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setTitle("删除");
            alert.setContentText("是否删除?");
            alert.showAndWait().ifPresent(response -> {
                if(response == javafx.scene.control.ButtonType.OK){
                    try{
                        user.send_requirement(updateReq);
                        UpdateResult res = (UpdateResult) user.get_result();
                        if(res.getColumnChanged() < 1){
                            Alert erralert = new Alert(Alert.AlertType.ERROR);
                            erralert.setTitle("寄！");
                            erralert.setContentText("删除失败");
                            erralert.showAndWait();
                        }else{
//                            删除成功直接退出去
                            stage.close();
                        }
                    }catch (Exception e){
                        Alert erralert = new Alert(Alert.AlertType.ERROR);
                        erralert.initModality(Modality.APPLICATION_MODAL);
                        erralert.setTitle("出错了！");
                        erralert.setContentText("删除失败");
                        erralert.showAndWait();
                    }
                }
            });
        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setTitle("寄!");
            alert.setContentText("发生错误" + e.getMessage());
        }
    }
}
