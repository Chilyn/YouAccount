package ye.chilyn.youaccounts.keepaccounts.entity;

import java.io.Serializable;

/**
 * Created by Alex on 2018/1/16.
 * 账目实体类
 */

public class AccountsBean implements Serializable{

    private static final long serialVersionUID = -5550692612906926408L;
    private int userId;
    private float money;
    private String billType;
    private long timeMill;
    private String time;

    public AccountsBean() {
    }

    public AccountsBean(int userId, float money, String billType, long timeMill, String time) {
        this.userId = userId;
        this.money = money;
        this.billType = billType;
        this.timeMill = timeMill;
        this.time = time;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public long getTimeMill() {
        return timeMill;
    }

    public void setTimeMill(long timeMill) {
        this.timeMill = timeMill;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }
}
