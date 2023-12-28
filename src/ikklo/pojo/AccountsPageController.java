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
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Map;

public class AccountsPageController {
    User user;

    public AccountsPageController(User user) {
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
    private Button AddTaskButton;

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
        /*
        * 初始化
        * 从accounts表中读取
        * 1.id
        * 2.gamename
        * 3.accountname
        * 4.note
        * 缩略界面展示【gamename】: accountname -- note
        * 详情页展示
        * 1.gamename
        * 2.accountname
        * 3.loginpassword
        */

        String sql = "SELECT * FROM accounts WHERE USERNAME = '" + user.getUsername() + "'";
        QueryReq queryReq = new QueryReq(user.getUsername(), user.getUuid(), sql);
        queryReq.setCols("id","gamename","accountname","note");
        try{
            user.send_requirement(queryReq);
            QueryResult res = (QueryResult)user.get_result();
            for(Map<String,String> map: res.getRecords()){
                String text = "【" + map.get("gamename") + "】 : " + map.get("accountname") + "  " + map.get("note");
                Label label = new Label();
                label.setFont(Font.font(20));
                label.setText(text);
//                设置点击事件
                label.setOnMouseClicked(mouseEvent->{
                    try{
                        Stage stage = new Stage();
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("account_detail.fxml"));
                        AccountDetailPageController accountDetailPageController = new AccountDetailPageController(user,Integer.parseInt(map.get("id")));
                        loader.setController(accountDetailPageController);

                        Parent root = loader.load();
                        Scene scene = new Scene(root);
                        stage.initModality(Modality.APPLICATION_MODAL);
                        stage.setResizable(false);
                        stage.initStyle(StageStyle.UTILITY);
                        stage.setTitle(map.get("gamename")+":"+map.get("accountname"));
                        stage.setScene(scene);
                        stage.setOnHiding(windowEvent -> {
                            leftBox.getChildren().clear();
                            rightBox.getChildren().clear();
                            initialize();
                        });
                        stage.show();
                    }catch (Exception e){
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("寄！");
                        alert.setContentText("出错了:"+e.getMessage());
                        alert.initModality(Modality.APPLICATION_MODAL);
                        alert.showAndWait();
                    }
                });
                leftBox.getChildren().add(label);
            }
            initLikedGame();
        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("寄！");
            alert.setContentText("出错了:"+e.getMessage());
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.showAndWait();
        }
    }
    @FXML
    void AddAccount(ActionEvent event) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("add_account.fxml"));
            AddAccountPageController addAccountPageController = new AddAccountPageController(user);
            loader.setController(addAccountPageController);
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
//            不可改变大小
            stage.initStyle(StageStyle.UTILITY);
            stage.setResizable(false);
//            模态对话框
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.setOnHiding(windowEvent -> {
                leftBox.getChildren().clear();
                rightBox.getChildren().clear();
                initialize();
            });
            stage.show();
        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("寄！");
            alert.setContentText("出错了:"+e.getMessage());
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.showAndWait();
        }
    }

    @FXML
    void changeToAccountsPage(ActionEvent event) {

    }

    @FXML
    void changeToTaskPage(ActionEvent event) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("mainPage_task.fxml"));
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
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("mainPage_tools.fxml"));
            ToolsPageController toolsPageController = new ToolsPageController(user);
            loader.setController(toolsPageController);
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage)ToolsPageButton.getScene().getWindow();
            stage.setTitle("游戏工具：给我玩怪物猎人（指）");
            stage.setScene(scene);
            stage.show();
        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("寄！");
            alert.setContentText("王大队长你这人就喜欢开玩笑");
            alert.showAndWait();
        }
    }
    @FXML
    void changeToLikedGamePage(ActionEvent event) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("add_liked_game.fxml"));
            AddLikedGamePageController controller = new AddLikedGamePageController(user);
            loader.setController(controller);
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
//            禁止改变大小
            stage.setResizable(false);
            stage.initStyle(StageStyle.UTILITY);
//            模态窗口
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
            alert.setContentText("给我个痛快");
//            模态窗口，为什么呢，我也不知道，但显然这样避免很多麻烦
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.showAndWait();
        }
    }

    @FXML
    void displayAll(ActionEvent event) {
        leftBox.getChildren().clear();
        rightBox.getChildren().clear();
        initialize();
    }
    private void initLikedGame() throws Exception{
        QueryReq queryReq = new QueryReq(user.getUsername(), user.getUuid(), "select * from likedgame where username='" + user.getUsername() + "'");
        queryReq.setCols("gamename");
        try{
            user.send_requirement(queryReq);
            QueryResult res = (QueryResult) user.get_result();
            for(Map<String,String> map:res.getRecords()){
                String name = map.get("gamename");
                Label label = new Label();
                label.setFont(new Font(20));
                label.setText(name);
//                设置鼠标点击事件
                label.setOnMouseClicked(mouseEvent -> {
//                    把左边的box清掉
                    leftBox.getChildren().clear();
                    QueryReq req = new QueryReq(user.getUsername(),user.getUuid(),"select * from accounts where username='"+user.getUsername() +"' and gamename = '" + name + "'");
                    req.setCols("id","gamename","accountname","note");
                    try{
                        user.send_requirement(req);
                        QueryResult tasks = (QueryResult) user.get_result();
//                        每一条数据都建一个label
                        for(Map<String,String> map1: tasks.getRecords()){
                            Label accountText = new Label();
                            String text ="【" + map1.get("gamename") + "】 : " + map1.get("accountname") + "  " + map1.get("note");
                            accountText.setText(text);
//                              定义鼠标点击时间
                            accountText.setOnMouseClicked(event-> {
//                                System.out.println("文字被点击了！");
                                try{
                                    Stage stage = new Stage();
                                    FXMLLoader loader = new FXMLLoader(getClass().getResource("account_detail.fxml"));
                                    AccountDetailPageController accountDetailPageController = new AccountDetailPageController(user,Integer.parseInt(map.get("id")));
                                    loader.setController(accountDetailPageController);

                                    Parent root = loader.load();
                                    Scene scene = new Scene(root);
                                    stage.initModality(Modality.APPLICATION_MODAL);
                                    stage.setResizable(false);
                                    stage.initStyle(StageStyle.UTILITY);
                                    stage.setTitle(map.get("gamename")+":"+map.get("accountname"));
                                    stage.setScene(scene);
                                    stage.setOnHiding(windowEvent -> {
                                        leftBox.getChildren().clear();
                                        rightBox.getChildren().clear();
                                        initialize();
                                    });
                                    stage.show();
                                }catch (Exception e){
                                    Alert alert = new Alert(Alert.AlertType.ERROR);
                                    alert.setTitle("寄！");
                                    alert.setContentText("出错了:"+e.getMessage());
                                    alert.initModality(Modality.APPLICATION_MODAL);
                                    alert.showAndWait();
                                }
                            });
                            accountText.setFont(new Font(20));
                            leftBox.getChildren().add(accountText);
                        }
                    }catch (Exception e){
                        throw new RuntimeException(e);
                    }
                });
                rightBox.getChildren().add(label);
            }
        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("寄！");
            alert.setContentText("给我个痛快");
            alert.showAndWait();
        }
    }
}
