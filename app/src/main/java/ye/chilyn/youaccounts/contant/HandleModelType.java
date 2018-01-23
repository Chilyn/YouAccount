package ye.chilyn.youaccounts.contant;

/**
 * Created by Alex on 2018/1/15.
 * 处理Model层事件的类型常量
 */

public interface HandleModelType {
    /**插入账目数据*/
    int INSERT_ACCOUNTS = 0;
    /**查询账目数据*/
    int QUERY_ACCOUNTS = 1;
    /**更新账目数据*/
    int UPDATE_ACCOUNTS = 2;
    /**删除账目数据*/
    int DELETE_ACCOUNTS = 3;
    /**计算账目数据*/
    int CALCULATE_TOTAL_ACCOUNTS = 4;
}
