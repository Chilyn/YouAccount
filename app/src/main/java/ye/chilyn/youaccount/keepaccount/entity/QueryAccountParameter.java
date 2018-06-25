package ye.chilyn.youaccount.keepaccount.entity;

/**
 * Created by Alex on 2018/1/17.
 * 查询账目的参数实体类
 */

public class QueryAccountParameter {
    private int userId;
    private long startTime;
    private long endTime;

    public QueryAccountParameter(int userId, long startTime, long endTime) {
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
