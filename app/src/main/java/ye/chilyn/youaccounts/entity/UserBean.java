package ye.chilyn.youaccounts.entity;

/**
 * Created by Alex on 2018/1/29.
 */

public class UserBean {

    private int userId;
    private String nickname;
    private String password;

    public UserBean() {
    }

    public UserBean(int userId, String nickname, String password) {
        this.userId = userId;
        this.nickname = nickname;
        this.password = password;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
