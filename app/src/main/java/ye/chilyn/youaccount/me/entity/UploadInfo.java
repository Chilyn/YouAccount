package ye.chilyn.youaccount.me.entity;

/**
 * Created by Alex on 2018/6/27
 */
public class UploadInfo {

    private int id = -1;
    private String info;

    public UploadInfo(int id, String info) {
        this.id = id;
        this.info = info;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
