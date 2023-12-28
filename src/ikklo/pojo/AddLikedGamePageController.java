package ikklo.pojo;

import ikklo.sgamer.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.management.Query;

public class AddLikedGamePageController {
    private User user;

    public AddLikedGamePageController(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @FXML
    private Button addButton;

    @FXML
    private TextField gameNameField;

    @FXML
    void add(ActionEvent event) {
        String gamename = gameNameField.getText();
        Stage stage = (Stage)addButton.getScene().getWindow();
        if(gamename.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setTitle("寄！");
            alert.setContentText("你并没有输入游戏名称");
            alert.showAndWait();
            stage.close();
        }
        try{
            QueryReq queryReq = new QueryReq(user.getUsername(),user.getUuid(),"select * from game where gamename = '" + gamename + "'");
            queryReq.setCols("gamename");
            user.send_requirement(queryReq);
            QueryResult qres = (QueryResult) user.get_result();
            UpdateReq updateReq = new UpdateReq(user.getUsername(), user.getUuid(), "insert into likedgame values('" + user.getUsername() + "','" + gamename + "')");
            if(qres.getRecords().isEmpty()){
                UpdateReq preUpdateReq = new UpdateReq(user.getUsername(),user.getUuid(),"insert into game values('" + gamename + "')");
                user.send_requirement(preUpdateReq);
                UpdateResult preres = (UpdateResult) user.get_result();
                if(preres.getColumnChanged() < 1)throw new RuntimeException("插入失败");
            }
            user.send_requirement(updateReq);
            UpdateResult res = (UpdateResult) user.get_result();
            if(res.getColumnChanged() < 1)throw new RuntimeException("插入失败");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("好！");
            alert.setContentText("添加收藏成功");
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.showAndWait();
            stage.close();
        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("寄！");
            alert.setContentText("出错啦！"+e.getMessage());
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.showAndWait();
            stage.close();
        }
    }
}
