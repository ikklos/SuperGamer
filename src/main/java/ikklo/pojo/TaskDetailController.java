package ikklo.pojo;

import ikklo.sgamer.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class TaskDetailController {
    User user;
    int id;
    public TaskDetailController() {
    }

    public TaskDetailController(User user, int id) {
        this.user = user;
        this.id = id;
    }

    @FXML
    private void initialize() throws Exception{

        String sql = "SELECT * FROM TASKS WHERE ID = " + id;
        QueryReq queryReq = new QueryReq(user.getUsername(), user.getUuid(), sql);
        queryReq.setCols("gamename","contant","time","finished");

        user.send_requirement(queryReq);
        QueryResult res = (QueryResult) user.get_result();
        if(res.getRecords().isEmpty()){
            Stage stage = (Stage)deleteButton.getScene().getWindow();
            stage.setAlwaysOnTop(false);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setTitle("寄！");
            alert.setContentText("没读到任务信息！");
            alert.showAndWait();
            stage.close();
        }
//        日期栏
        dateLabel.setFont(new Font(13));
        dateLabel.setText(res.getRecords().get(0).get("time"));
        boolean finished = res.getRecords().get(0).get("finished").equals("1");
//        复选框
        finishedCheckBox.setSelected(finished);
//        标题
        taskTitle.setFont(new Font(15));
        taskTitle.setText(res.getRecords().get(0).get("gamename"));
//        任务内容
        taskContant.setFont(new Font(20));
        taskContant.setText(res.getRecords().get(0).get("contant"));

    }
    @FXML
    private Label dateLabel;

    @FXML
    private Button deleteButton;

    @FXML
    private CheckBox finishedCheckBox;

    @FXML
    private Label taskContant;

    @FXML
    private Label taskTitle;

    @FXML
    void delete(ActionEvent event) {
//        删除
        String sql = "DELETE FROM TASKS WHERE ID = " + id;
        UpdateReq updateReq = new UpdateReq(user.getUsername(), user.getUuid(), sql);
        try{
            Stage stage = (Stage)deleteButton.getScene().getWindow();
//            把stage从顶层弄下来
            stage.setAlwaysOnTop(false);
//            显示新的交互窗口
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
//                            如果删除失败则再回到stage
                            stage.setAlwaysOnTop(true);
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
                        stage.setAlwaysOnTop(true);
                    }

                }
            });
        }catch (Exception e){
            Alert erralert = new Alert(Alert.AlertType.ERROR);
            erralert.initModality(Modality.APPLICATION_MODAL);
            erralert.setTitle("出错了！");
            erralert.setContentText("哎程序怎么似了");
            erralert.showAndWait();
            Stage stage = (Stage)deleteButton.getScene().getWindow();
//            修改stage属性
            stage.setAlwaysOnTop(true);
        }

    }

    @FXML
    void setFinished(ActionEvent event) {
        String sql = "UPDATE tasks SET finished = " + finishedCheckBox.isSelected() + " WHERE id = " + id;
        Stage stage = (Stage)finishedCheckBox.getScene().getWindow();
        try{
            UpdateReq updateReq = new UpdateReq(user.getUsername(), user.getUuid(), sql);
            user.send_requirement(updateReq);
            UpdateResult res = (UpdateResult) user.get_result();
            if(res.getColumnChanged() < 1){
                stage.setAlwaysOnTop(false);
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("寄！");
                alert.setContentText("无法改变完成状态，出问题了，淦");
                alert.showAndWait();
                stage.setAlwaysOnTop(true);
            }
        }catch (Exception e){
            stage.setAlwaysOnTop(false);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("寄！");
            alert.setContentText("无法改变完成状态，出问题了，淦");
            alert.showAndWait();
            stage.setAlwaysOnTop(true);
        }
    }
}
