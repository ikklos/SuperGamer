package ikklo.pojo;
import ikklo.sgamer.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Map;

public class ToolsPageController {
    User user;

    public ToolsPageController(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    @FXML
    private Button AccountPageButton;

    @FXML
    private Button AddFileButton;

    @FXML
    private Button TaskPageButton;

    @FXML
    private Button ToolsPageButton;
    @FXML
    private Button likedGameButton;
    @FXML
    private Button displayAllButton;

    @FXML
    private VBox leftBox;

    @FXML
    private VBox rightBox;

    @FXML
    private void initialize(){
//        初始化界面

//        读取用户记录
        try{
            String sql = "SELECT * FROM gametool WHERE username = '" + user.getUsername() + "'";
            QueryReq queryReq = new QueryReq(user.getUsername(), user.getUuid(), sql);
            queryReq.setCols("id","gamename","name");
            user.send_requirement(queryReq);
            QueryResult res = (QueryResult) user.get_result();
            if(res.getRecords() == null)throw new RuntimeException("查询失败!");
//            对每一条记录创建一个label
            for(Map<String,String> map: res.getRecords()){
                initleft(map);
            }
//            初始化筛选词条
            initlikedgame();
        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("寄！");
            alert.setContentText(e.getMessage());
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.showAndWait();
        }

    }

    @FXML
    void addNewFile(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(ToolsPageController.class.getClassLoader().getResource("resource/template/upload_file.fxml"));
        UploadFilePageController uploadFilePageController = new UploadFilePageController(user);
        loader.setController(uploadFilePageController);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("加！");
        stage.setResizable(false);
        stage.initStyle(StageStyle.UTILITY);
        stage.initModality(Modality.APPLICATION_MODAL);
//        关掉之后刷新
        stage.setOnHiding(windowEvent -> {
            leftBox.getChildren().clear();
            rightBox.getChildren().clear();
            initialize();
        });
        stage.show();
    }


    @FXML
    void changeToAccountsPage(ActionEvent event) {
        try{
            FXMLLoader loader = new FXMLLoader(ToolsPageController.class.getClassLoader().getResource("resource/template/mainPage_accounts.fxml"));
            System.out.println("loader加载...");
            AccountsPageController accountsPageController = new AccountsPageController(user);
            loader.setController(accountsPageController);
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage)AccountPageButton.getScene().getWindow();
            stage.setTitle("游戏账号：这游戏手机能玩吗");
            stage.setScene(scene);
            stage.show();
        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("寄！");
            alert.setContentText("哎程序怎么似了");
            alert.showAndWait();
        }
    }

    @FXML
    void changeToTaskPage(ActionEvent event) {
        try{
            FXMLLoader loader = new FXMLLoader(ToolsPageController.class.getClassLoader().getResource("resource/template/mainPage_task.fxml"));
            System.out.println("loader加载...");
            TaskPageController taskPageController = new TaskPageController(user);
            loader.setController(taskPageController);
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage)AccountPageButton.getScene().getWindow();
            stage.setTitle("游戏任务列表：开始上班！");
            stage.setScene(scene);
            stage.show();
        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("寄！");
            alert.setContentText("哎程序怎么似了");
            alert.showAndWait();
        }
    }

    @FXML
    void changeToToolsPage(ActionEvent event) {

    }
    @FXML
    void changeToLikedGamePage(ActionEvent event) {
        try{
            FXMLLoader loader = new FXMLLoader(ToolsPageController.class.getClassLoader().getResource("resource/template/add_liked_game.fxml"));
            AddLikedGamePageController controller = new AddLikedGamePageController(user);
            loader.setController(controller);
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("去听听《挪威的森林》");
            stage.setScene(scene);
//            禁止改变大小
            stage.setResizable(false);
            stage.initStyle(StageStyle.UTILITY);
//            模态窗口
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("寄！");
            alert.setContentText("给我个痛快" + e.getMessage());
//            模态窗口，为什么呢，我也不知道，但显然这样避免很多麻烦
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.showAndWait();
        }
    }
    @FXML
    void displayAll(){
        leftBox.getChildren().clear();
        rightBox.getChildren().clear();
        initialize();
    }
    private void initlikedgame(){
        String sql = "SELECT * FROM likedgame WHERE username = '" + user.getUsername() + "'";
        QueryReq queryReq = new QueryReq(user.getUsername(), user.getUuid(), sql);
        queryReq.setCols("gamename");
        try{
            user.send_requirement(queryReq);
            QueryResult res = (QueryResult) user.get_result();
            if(res.getRecords() == null)throw new RuntimeException("查找筛选标签失败！");
            for(Map<String,String> map : res.getRecords()){
                Label label = new Label();
                label.setFont(new Font(20));
                label.setText(map.get("gamename"));
                label.setOnMouseEntered(mouseEvent -> {
                    label.setTextFill(Color.BROWN);
                });
                label.setOnMouseExited(mouseEvent -> {
                    label.setTextFill(Color.BLACK);
                });
                label.setOnMouseClicked(mouseEvent -> {
                    label.setTextFill(Color.RED);
                    leftBox.getChildren().clear();
                    String newsql = "SELECT * FROM gametool WHERE gamename = '" + map.get("gamename") + "' and username = '" + user.getUsername() + "'";
                    try{
                        QueryReq queryReq2 = new QueryReq(user.getUsername(), user.getUuid(), newsql);
                        queryReq2.setCols("id","gamename","name");
                        user.send_requirement(queryReq2);
                        QueryResult res2 = (QueryResult) user.get_result();
                        if(res2.getRecords() == null)throw new RuntimeException("查询失败");
                        for(Map<String,String> map2: res2.getRecords()){
                            initleft(map2);
                        }
                    }catch (Exception e){
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("寄！");
                        alert.setContentText("给我个痛快" + e.getMessage());
                        alert.initModality(Modality.APPLICATION_MODAL);
                        alert.showAndWait();
                    }
                });
                rightBox.getChildren().add(label);
            }
        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("寄！");
            alert.setContentText("给我个痛快" + e.getMessage());
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.showAndWait();
        }
    }
    private void initleft(Map<String,String> map){
        Label label = new Label();
        label.setFont(new Font(20));
        label.setText("【"+ map.get("gamename") + "】" + map.get("name"));
        label.setOnMouseEntered(mouseEvent -> {
            label.setTextFill(Color.BROWN);
        });
        label.setOnMouseExited(mouseEvent -> {
            label.setTextFill(Color.BLACK);
        });
//                设置点击事件
        label.setOnMouseClicked(mouseEvent -> {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(ToolsPageController.class.getClassLoader().getResource("resource/template/tool_detail.fxml"));
            ToolDetailPageController toolDetailPageController = new ToolDetailPageController(user);
            toolDetailPageController.setId(Integer.parseInt(map.get("id")));
            loader.setController(toolDetailPageController);
            try{
                Parent root = loader.load();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.initStyle(StageStyle.UTILITY);
                stage.setResizable(false);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setOnHiding(windowEvent -> {
                    leftBox.getChildren().clear();
                    rightBox.getChildren().clear();
                    initialize();
                });
                stage.show();
            }catch (Exception e){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("寄！");
                alert.setContentText("给我个痛快" + e.getMessage());
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.showAndWait();
            }
        });
        leftBox.getChildren().add(label);
    }
}
