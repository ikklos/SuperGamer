package ikklo.sgamer;

import ikklo.pojo.ViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;



public class SuperGamerApp extends Application {
    private static User user = new User();
    public static void main(String[] args) throws Exception{
//        连接到服务器
        user.connect_to_server();
//        显示主界面
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("view.fxml"));
//        主界面控制器
        ViewController viewController = new ViewController();
        viewController.setUser(user);
        System.out.println("设置user");
//        注册控制器
        loader.setController(viewController);
        System.out.println("设置controller");
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setTitle("登录");
        stage.setOnCloseRequest(this::handleClose);
        stage.setScene(scene);
        stage.show();
        System.out.println("start调用完毕");
    }
    private void handleClose(WindowEvent eve){
        try{
            user.getSocket().close();
        }
        catch (Exception e){
            System.out.println("WARNING：未正常关闭");
        }
    }
}