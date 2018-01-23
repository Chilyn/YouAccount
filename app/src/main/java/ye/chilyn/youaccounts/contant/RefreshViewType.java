package ye.chilyn.youaccounts.contant;

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
}
