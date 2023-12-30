package ikklo.pojo;
import javafx.scene.control.Alert;
import javafx.stage.Modality;


import java.io.Serializable;
import java.util.List;
import java.util.Map;

public abstract class ReqResult implements Serializable {
//    这是服务器处理完需求之后返回的结果
    public abstract boolean solve();
}
/*
* 注册操作的结果
* 返回系统中的用户昵称
* 如果注册失败，返回null*/
class RegisterResult extends ReqResult {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean solve() {
        Alert al = new Alert(Alert.AlertType.INFORMATION);
        al.initModality(Modality.APPLICATION_MODAL);
        if (name == null) {
            al.setTitle("完犊子啦");
            al.setTitle(null);
            al.setContentText("注册失败");
            al.showAndWait();
            return false;
        } else {
            al.setTitle("我去");
            al.setTitle(null);
            al.setContentText("注册成功" + name);
            al.showAndWait();
        }
        return true;
    }
}
class LoginResult extends ReqResult {
    private String name;
    private String uuid = "";
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public boolean solve() {
        Alert al = new Alert(Alert.AlertType.INFORMATION);
        if (name == null) {
            al.setTitle("完犊子啦");
            al.setTitle(null);
            al.setContentText("登录失败");
            al.showAndWait();
            return false;
        } else {
            al.setTitle("我去");
            al.setTitle(null);
            al.setContentText("登录成功" + name);
            al.showAndWait();
        }
        return true;
    }
}
class QueryResult extends ReqResult{

    private List<Map<String,String>> records;

    public QueryResult(List<Map<String, String>> records) {
        this.records = records;
    }

    public List<Map<String, String>> getRecords() {
        return records;
    }

    public void setRecords(List<Map<String, String>> texts) {
        this.records = texts;
    }

    @Override
    public boolean solve() {
        return false;
    }
}
class UpdateResult extends ReqResult{
    private Integer columnChanged;

    public UpdateResult(Integer columnChanged) {
        this.columnChanged = columnChanged;
    }

    public int getColumnChanged() {
        return columnChanged;
    }

    public void setColumnChanged(Integer columnChanged) {
        this.columnChanged = columnChanged;
    }

    @Override
    public boolean solve() {
        return false;
    }
}
class UploadResult extends ReqResult{
    private boolean uploaded;

    public UploadResult(boolean uploaded) {
        this.uploaded = uploaded;
    }

    @Override
    public boolean solve() {
        return uploaded;
    }
}
class DownloadResult extends ReqResult{
    private boolean downloaded;

    public DownloadResult(boolean downloaded) {
        this.downloaded = downloaded;
    }

    @Override
    public boolean solve() {
        return downloaded;
    }
}
class DeleteResult extends ReqResult{
    private boolean deleted;

    public DeleteResult(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public boolean solve() {
        return deleted;
    }
}