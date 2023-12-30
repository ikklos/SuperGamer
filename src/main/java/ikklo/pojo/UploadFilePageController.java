package ikklo.pojo;

import ikklo.sgamer.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class UploadFilePageController {
    User user;

    public UploadFilePageController(User user) {
        this.user = user;
    }

    @FXML
    private TextField fileNameField;

    @FXML
    private TextField fileUrlField;

    @FXML
    private TextField gameNameField;

    @FXML
    private TextArea noteField;

    @FXML
    private Button selectFileButton;

    @FXML
    private Button uploadButton;

    @FXML
    void select(ActionEvent event) {
        Stage stage = (Stage)selectFileButton.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("选！");
        File f = fileChooser.showOpenDialog(stage);
        fileUrlField.setText(f.getPath());
    }

    @FXML
    void upload(ActionEvent event) {
        try{
            String game = gameNameField.getText();
            String filename = fileNameField.getText();
            String url = fileUrlField.getText();
            String note = noteField.getText();
            if(game.isEmpty() || filename.isEmpty() || url.isEmpty() || note.length() > 100){
                throw new RuntimeException("游戏名，文件名和文件路径均不可为空，备注不宜过长");
            }
            String sql = "INSERT INTO game VALUES('" + game + "')";
            UpdateReq updateReq = new UpdateReq(user.getUsername(), user.getUuid(), sql);
            user.send_requirement(updateReq);
            user.get_result();
            File f = new File(url);
            System.out.println("获取文件成功！" + f.getPath());
            UploadReq uploadReq = new UploadReq(user.getUsername(), user.getUuid(), filename, f.getName(), game);
            uploadReq.setNote(note);
            ObjectOutputStream out = user.getOut();
            System.out.println("获取输出流");
            FileInputStream fileInputStream = new FileInputStream(f);
            System.out.println("获取文件流");
            FileChannel channel = fileInputStream.getChannel();
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024);
            System.out.println("准备传输");
            out.flush();
            user.send_requirement(uploadReq);
            while(channel.read(byteBuffer) != -1){
                byteBuffer.flip();
                System.out.println(byteBuffer.remaining());
                byte[] buffer = new byte[byteBuffer.remaining()];
                byteBuffer.get(buffer);
                FilePackage pac = new FilePackage(buffer);
//                System.out.println("读取一次");
                out.writeObject(pac);
                byteBuffer.clear();
            }
            System.out.println("传输完毕");
            FilePackage endpac = new FilePackage(new byte[0]);
            endpac.setEnd(true);
            out.writeObject(endpac);
            if(!user.get_result().solve()){
                throw new RuntimeException("上传文件失败！");
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("好！");
            alert.setContentText("上传完毕");
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.showAndWait();
            Stage stage = (Stage) uploadButton.getScene().getWindow();
            stage.close();
        }catch (Exception e){
            System.out.println(e);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("寄！");
            alert.setContentText(e.getMessage());
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.showAndWait();
        }
    }
}
