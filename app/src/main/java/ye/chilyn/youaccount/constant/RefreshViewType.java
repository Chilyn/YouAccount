package ye.chilyn.youaccount.constant;

/**
 * Created by Alex on 2018/1/15.
 * 处理View层事件的常量
 */

public interface RefreshViewType {
    /**显示进度条弹窗*/
    int SHOW_PROGRESS_DIALOG = 100;
    /**插入账目成功*/
    int INSERT_ACCOUNTS_SUCCESS = 101;
    /**插入账目失败*/
    int INSERT_ACCOUNTS_FAIL = 102;
    /**查询账目成功*/
    int QUERY_ACCOUNTS_SUCCESS = 103;
    /**查询账目失败*/
    int SHOW_TOTAL_ACCOUNTS = 104;
    /**删除账目成功*/
    int DELETE_ACCOUNT_SUCCESS = 105;
    /**删除账目失败*/
    int DELETE_ACCOUNT_FAIL = 106;
    /**更新账目成功*/
    int UPDATE_ACCOUNT_SUCCESS = 107;
    /**更新账目失败*/
    int UPDATE_ACCOUNT_FAIL = 108;
    /**强制关闭键盘*/
    int FORCE_CLOSE_SOFT_KEYBOARD = 109;
    /**用户之前已注册*/
    int USER_HAS_REGISTERED_BEFORE = 110;
    /**注册成功*/
    int REGISTER_USER_SUCCESS = 111;
    /**注册失败*/
    int REGISTER_USER_FAIL = 112;
    /**用户不存在*/
    int USER_NOT_EXIST = 113;
    /**登录成功*/
    int LOGIN_SUCCESS = 114;
    /**登录失败*/
    int LOGIN_FAIL = 115;
    /**修改用户密码失败*/
    int MODIFY_PASSWORD_SUCCESS = 116;
    /**修改用户密码失败*/
    int MODIFY_PASSWORD_FAIL = 117;
    /**修改用户昵称成功*/
    int MODIFY_NICKNAME_SUCCESS = 118;
    /**修改用户昵称失败*/
    int MODIFY_NICKNAME_FAIL = 119;
    /**刷新上传信息*/
    int REFRESH_UPLOAD_INFO = 120;
    /**上传成功*/
    int UPLOAD_SUCCESS = 121;
    /**上传失败*/
    int UPLOAD_FAILED = 122;
    /**显示选择服务器的弹窗*/
    int SHOW_CHOOSE_SERVER_DIALOG = 123;
    /**初始化我的模块弹窗*/
    int INIT_ME_FRAGMENT_DIALOGS = 124;
}
