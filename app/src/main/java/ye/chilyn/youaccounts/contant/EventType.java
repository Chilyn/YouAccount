package ye.chilyn.youaccounts.contant;

/**
 * Created by Alex on 2018/1/17.
 * EventBus时间类型常量
 */

public interface EventType {
    /**写入外部存储权限获取成功*/
    int WRITE_FILE_PERMISSION_GOTTEN = 1001;
    /**查询账目*/
    int QUERY_ACCOUNTS = 1002;
    /**删除账单后查询账目*/
    int QUERY_ACCOUNTS_AFTER_DELETE = 1003;
}
