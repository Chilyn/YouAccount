package ye.chilyn.youaccounts.constant;

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
    /**注册新用户*/
    int REGISTER_USER = 5;
    /**用户登录*/
    int USER_LOGIN = 6;
    /**修改用户密码*/
    int MODIFY_USER_PASSWORD = 7;
    /**修改用户昵称*/
    int MODIFY_USER_NICKNAME = 8;
    /**上传至本地服务器*/
    int UPLOAD_TO_LOCAL_SERVER = 9;
    /**上传至远程服务器*/
    int UPLOAD_TO_REMOTE_SERVER = 10;
    /**取消上传*/
    int CANCEL_UPLOAD = 11;
}
