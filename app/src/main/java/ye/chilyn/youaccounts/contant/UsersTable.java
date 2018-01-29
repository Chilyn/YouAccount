package ye.chilyn.youaccounts.contant;

/**
 * Created by Alex on 2018/1/16.
 * 用户表相关常量
 */

public interface UsersTable {
    String TABLE_NAME = "users";
    String USER_ID = "user_id";
    String NICKNAME = "nickname";
    String PASSWORD = "password";

    String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" +
            USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            NICKNAME + " TEXT NOT NULL," +
            PASSWORD + " TEXT NOT NULL)";

    String SQL_DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    String SQL_USER_EXISTED_WHERE = NICKNAME + "=?";
    String SQL_MATCH_USER_WHERE = NICKNAME + "=? AND " + PASSWORD + "=?";
    String SQL_QUERY_USER_WHERE = NICKNAME + "=?";
}
