package ye.chilyn.youaccount.constant;

import ye.chilyn.youaccount.AccountApplication;
import ye.chilyn.youaccount.R;

/**
 * Created by Alex on 2018/1/16.
 * 用户表相关常量
 */

public interface UsersTable {
    String TABLE_NAME = AccountApplication.getAppContext().getString(R.string.table_name_users);
    String USER_ID = AccountApplication.getAppContext().getString(R.string.table_user_id);
    String NICKNAME = AccountApplication.getAppContext().getString(R.string.table_nickname);
    String PASSWORD = AccountApplication.getAppContext().getString(R.string.table_password);

    String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" +
            USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            NICKNAME + " TEXT NOT NULL," +
            PASSWORD + " TEXT NOT NULL)";

    String SQL_DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    String SQL_USER_EXISTED_WHERE = NICKNAME + "=?";

    String SQL_MATCH_USER_WHERE = NICKNAME + "=? AND " + PASSWORD + "=?";

    String SQL_QUERY_USER_WHERE = NICKNAME + "=?";

    String SQL_UPDATE_USER_WHERE = USER_ID + "=?";
}
