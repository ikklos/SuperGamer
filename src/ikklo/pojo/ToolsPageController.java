package ikklo.pojo;
import ikklo.sgamer.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

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
    void AddAccount(ActionEvent event) {

    }

    @FXML
    void changeToAccountsPage(ActionEvent event) {
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
            alert.setContentText("给我个痛快");
//            模态窗口，为什么呢，我也不知道，但显然这样避免很多麻烦
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.showAndWait();
        }
    }
    @FXML
    void displayAll(){

    }
}
