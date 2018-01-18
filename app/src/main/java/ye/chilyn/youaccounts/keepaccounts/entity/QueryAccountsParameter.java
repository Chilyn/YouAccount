package ye.chilyn.youaccounts.keepaccounts.entity;

/**
 * Created by Alex on 2018/1/17.
 */

public class QueryAccountsParameter {
    private int userId;
    private long startTime;
    private long endTime;

    public QueryAccountsParameter(int userId, long startTime, long endTime) {
        this.userId = userId;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }
}
