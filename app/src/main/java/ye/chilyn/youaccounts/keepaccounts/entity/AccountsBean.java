package ye.chilyn.youaccounts.keepaccounts.entity;

/**
 * Created by Alex on 2018/1/16.
 */

public class AccountsBean {

    private String billType;
    private String time;
    private long timeMill;
    private float money;

    public AccountsBean() {
    }

    public AccountsBean(String billType, String time, long timeMill, float money) {
        this.billType = billType;
        this.time = time;
        this.timeMill = timeMill;
        this.money = money;
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
