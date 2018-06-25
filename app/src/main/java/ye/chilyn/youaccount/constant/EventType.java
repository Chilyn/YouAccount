package ye.chilyn.youaccount.constant;

/**
 * Created by Alex on 2018/1/17.
 * EventBus时间类型常量
 */

public interface EventType {
    /**更新账单后查询账目*/
    int QUERY_ACCOUNTS_AFTER_UPDATE = 1001;
    /**删除账单后查询账目*/
    int QUERY_ACCOUNTS_AFTER_DELETE = 1002;
    /**修改昵称成功*/
    int MODIFY_NICKNAME_SUCCESS = 1003;
    /**修改密码成功*/
    int MODIFY_PASSWORD_SUCCESS = 1004;
}
