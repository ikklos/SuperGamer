package ikklo.pojo;

import java.io.Serializable;

public class FilePackage implements Serializable {
    private byte[] data;
    private boolean end;

    public FilePackage(byte[] data) {
        this.data = data;
        this.end = false;
    }

    public void setEnd(boolean end) {
        this.end = end;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public boolean isEnd() {
        return end;
    }
}
