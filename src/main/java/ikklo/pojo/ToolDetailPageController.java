package ikklo.pojo;

import ikklo.sgamer.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Map;

public class ToolDetailPageController {
    private User user;
    private int id = -1;
    private String filename = "";
    public ToolDetailPageController(User user) {
        this.user = user;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    @FXML
    private TextField addressField;
    @FXML
    private Button chooseButton;
    @FXML
    private Button downloadButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Label noteLabel;
    @FXML
    private void initialize(){
//        初始化
        String sql = "SELECT * FROM gametool WHERE id=" + id;
        try{
            QueryReq queryReq = new QueryReq(user.getUsername(), user.getUuid(), sql);
            queryReq.setCols("note","url");
            user.send_requirement(queryReq);
            QueryResult res = (QueryResult) user.get_result();
            if(res.getRecords() == null){
                throw new RuntimeException("初始化：查询失败");
            }
            Map<String,String> map = res.getRecords().get(0);
            filename = map.get("url");
            if(!map.get("note").isEmpty()){
                noteLabel.setText(map.get("note"));
            }
        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("寄！");
            alert.setContentText(e.getMessage());
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.showAndWait();
        }
    }
    @FXML
    void chooseAddress(ActionEvent event) {
        Stage stage = (Stage) chooseButton.getScene().getWindow();
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("房子修得再好那是个临时住所");
        File f = directoryChooser.showDialog(stage);
        addressField.setText(f.getPath());
    }
    @FXML
    void delete(ActionEvent event) {
        try{
            DeleteReq deleteReq = new DeleteReq(user.getUsername(), user.getUuid(), "DELETE FROM gametool WHERE id = " + id, filename);
            user.send_requirement(deleteReq);
            if(user.get_result().solve()){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("好！");
                alert.setContentText("删除成功");
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.showAndWait();
                Stage stage = (Stage)deleteButton.getScene().getWindow();
                stage.close();
            }else{
                throw new RuntimeException("删除失败");
            }
        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("寄！");
            alert.setContentText(e.getMessage());
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.showAndWait();
        }
    }

    @FXML
    void download(ActionEvent event) {
        String address = addressField.getText();
        File f = new File(address + "/" + filename);
        if(address.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("你要去哪啊我说");
            alert.setContentText("请选择下载地址");
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.showAndWait();
            return;
        }
        try{
            user.getSocket().setSoTimeout(1000);
            System.out.println("设置超时时间");
//            如果这一块代码出问题，直接进行一个清理缓冲区
//            需要回滚吗？显然不用
            FileOutputStream fileOutputStream = new FileOutputStream(address + "/" + filename);
            System.out.println("获取文件输出流");
//            获取地址
            FileChannel channel = fileOutputStream.getChannel();
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024);
            System.out.println("获取channel");
            DownLoadReq downLoadReq = new DownLoadReq(user.getUsername(), user.getUuid(), id);
//            取得输入流
            ObjectInputStream in = user.getIn();
//            发送请求
            user.send_requirement(downLoadReq);
            System.out.println("发送请求成功，准备接收...");
            while(true){
                FilePackage pac = (FilePackage) in.readObject();
                byteBuffer.put(pac.getData());
                byteBuffer.flip();
                channel.write(byteBuffer);
                byteBuffer.clear();
                if(pac.isEnd())break;
            }
            System.out.println("传输结束");
            DownloadResult res = (DownloadResult)user.get_result();
        }catch (Exception e){
            f.delete();
            try {
                user.getSocket().setSoTimeout(1000);
            } catch (SocketException ex) {
                throw new RuntimeException(ex);
            }
            clear_stream(user.getIn());
            try {
                user.getSocket().setSoTimeout(0);
            } catch (SocketException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("寄！");
                alert.setContentText(ex.getMessage());
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.showAndWait();
            }
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("寄！");
            alert.setContentText("下载失败!" + e.getMessage());
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.showAndWait();
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("好！");
        alert.setContentText("下载完成！");
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.showAndWait();
        Stage stage = (Stage) downloadButton.getScene().getWindow();
        stage.close();
    }
//        如果出错，为了防止之后的字节流存取混乱，在结束请求处理之前应该先清理输入输出流
    private void clear_stream(ObjectInputStream in){
        while(true){
            try {
                Object pac = in.readObject();
            } catch (Exception e){
                return;
            }
        }
    }
}
