package ikklo.pojo;

import ikklo.sgamer.User;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Map;

import javafx.scene.text.Font;
import javafx.stage.StageStyle;

public class TaskPageController {
    User user;

    public TaskPageController(User user) {
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
    private void initialize() throws Exception{
        System.out.println("TaskPageController:initialize被调用了！");
        QueryReq queryReq = new QueryReq(user.getUsername(),user.getUuid(),"select * from tasks where username='"+user.getUsername()+"'");
        queryReq.setCols("id","contant","gamename");
        if(user.getUuid().isEmpty()){
//            如果用户还没有拿到uuid，就不能发出查询请求
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("寄");
            alert.setContentText("你为什么没有登录就能进这个页面呢");
            alert.showAndWait();
            return;
        }
        user.send_requirement(queryReq);
        System.out.println(user.getUsername());
        QueryResult res = (QueryResult) user.get_result();

        if(res.getRecords() == null){
            System.out.println("我啥也没拿着啊");
            return;
        }
        Platform.runLater(
                ()->{
                    try{
                        for(Map<String,String> map: res.getRecords()){
//                              对每一条记录创建一个label
                            Label label = new Label();
                            String text ="【" + map.get("gamename") + "】: " +  map.get("contant");
                            int endIndex = Math.min(50,text.length());
                            label.setText(text.substring(0,endIndex));
//                              定义鼠标点击时间
                            label.setOnMouseClicked(event-> {
//                                System.out.println("文字被点击了！");
                                try {
                                    lookContant(Integer.parseInt(map.get("id")));
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                            });
                            label.setFont(new Font(20));
                            leftBox.getChildren().add(label);
                        }
                        initLikedGame();
                    }catch (Exception e){
                        System.out.println(e);
                    }

                }
        );

    }
    @FXML
    void AddTask(ActionEvent event){
        /*
        * 添加任务
        * 打开添加任务页面*/
        try{
            AddTaskPageController addTaskPageController = new AddTaskPageController(user);
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("add_task_page.fxml"));
            fxmlLoader.setController(addTaskPageController);
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.setOnHiding(windowEvent -> {
                try {
                    leftBox.getChildren().clear();
                    rightBox.getChildren().clear();
                    initialize();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            stage.setScene(scene);
            stage.initStyle(StageStyle.UTILITY);
            stage.setResizable(false);
            stage.show();
        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("寄！");
            alert.setContentText("哎程序怎么似了");
            alert.showAndWait();
        }
    }

    @FXML
    void changeToAccountsPage(ActionEvent event){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("mainPage_accounts.fxml"));
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
    void lookContant(int id) {
        /*
        * 加载任务详情页
        */
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("task_detail.fxml"));
            TaskDetailController taskDetailController = new TaskDetailController(user,id);
            loader.setController(taskDetailController);
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("AQ，Q打断别人的E啊");
            stage.setAlwaysOnTop(true);
//            设置为模态对话框
            stage.initModality(Modality.APPLICATION_MODAL);
//            禁用最大化按钮
            stage.initStyle(StageStyle.UTILITY);
            stage.setResizable(false);
//            关闭后刷新旧页面
            stage.setOnHiding(windowEvent -> {
                try {
                    leftBox.getChildren().clear();
                    rightBox.getChildren().clear();
                    initialize();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            stage.show();
        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("寄！");
            alert.setContentText("为什么呀？为什么装备给挖掘机呀？为什么帽子不给科加斯呀？");
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
                try {
                    initialize();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
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
        try{
            initialize();
        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("寄！");
            alert.setContentText("出问题了" + e.getMessage());
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.showAndWait();
        }

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
                    QueryReq req = new QueryReq(user.getUsername(),user.getUuid(),"select * from tasks where username='"+user.getUsername() +"' and gamename = '" + name + "'");
                    req.setCols("id","gamename","contant");
                    try{
                        user.send_requirement(req);
                        QueryResult tasks = (QueryResult) user.get_result();
//                        每一条数据都建一个label
                        for(Map<String,String> map1: tasks.getRecords()){
                            Label taskText = new Label();
                            String text ="【" + map1.get("gamename") + "】: " +  map1.get("contant");
                            int endIndex = Math.min(50,text.length());
                            taskText.setText(text.substring(0,endIndex));
//                              定义鼠标点击时间
                            taskText.setOnMouseClicked(event-> {
//                                System.out.println("文字被点击了！");
                                try {
                                    lookContant(Integer.parseInt(map1.get("id")));
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                            });
                            taskText.setFont(new Font(20));
                            leftBox.getChildren().add(taskText);
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