package ikklo.pojo;

import ikklo.sgamer.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Map;

public class AddAccountPageController {
    private User user;

    public AddAccountPageController(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    @FXML
    private TextField accountName;

    @FXML
    private Button addButton;

    @FXML
    private TextField gameName;

    @FXML
    private TextField loginPassword;

    @FXML
    private TextArea note;

    @FXML
    void add(ActionEvent event) {
        String name = accountName.getText();
        String loginpassword = loginPassword.getText();
        String notetext = note.getText();
        String game = gameName.getText();
        if(game.isEmpty() || name.isEmpty() || notetext.length() > 50){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("寄！");
            alert.setContentText("请注意：游戏名称和用户名都不能为空！备注不要过长！");
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.showAndWait();
            return;
        }
//        本地检查过了就先检查game有没有添加
        QueryReq queryReq = new QueryReq(user.getUsername(),user.getUuid(),"SELECT * FROM game WHERE gamename = '" + game + "'");
        try{
            queryReq.setCols("gamename");
            user.send_requirement(queryReq);
            QueryResult res = (QueryResult) user.get_result();
            if(res.getRecords().isEmpty()){
//                如果没有，添加一个
                String ins = "INSERT INTO game VALUES('" + game + "')";
                UpdateReq req = new UpdateReq(user.getUsername(),user.getUuid(),ins);
                user.send_requirement(req);
                UpdateResult insres = (UpdateResult) user.get_result();
                if(insres.getColumnChanged() < 1){
                    throw new RuntimeException("向数据库中添加游戏失败");
                }
            }
        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("寄！");
            alert.setContentText("出错了:"+e.getMessage());
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.showAndWait();
        }
        String sql = "INSERT INTO accounts(username,gamename,accountname,loginpassword,note) VALUES('"+user.getUsername()+"','" + game + "','" + name + "','" + loginpassword + "','" + notetext + "')";
        UpdateReq updateReq = new UpdateReq(user.getUsername(), user.getUuid(), sql);
        try{
            user.send_requirement(updateReq);
            UpdateResult res = (UpdateResult) user.get_result();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.initModality(Modality.APPLICATION_MODAL);
            if(res.getColumnChanged() < 1){
                alert.setTitle("好");
                alert.setContentText("寄了，添加失败");
                alert.showAndWait();
            }else{
                alert.setTitle("好");
                alert.setContentText("添加成功！");
                alert.showAndWait();
                Stage stage = (Stage) addButton.getScene().getWindow();
                stage.close();
            }
        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("寄！");
            alert.setContentText("出错了:"+e.getMessage());
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.showAndWait();
        }
    }
}