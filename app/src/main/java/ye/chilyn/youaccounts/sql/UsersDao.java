package ye.chilyn.youaccounts.sql;

import android.content.ContentValues;
import android.database.Cursor;

import ye.chilyn.youaccounts.constant.UsersTable;
import ye.chilyn.youaccounts.entity.UserBean;

/**
 * Created by Alex on 2018/1/29.
 */

public class UsersDao {

    private YouAccountsSqlHelper mSqlHelper = YouAccountsSqlHelper.getInstance();

    public UsersDao() {
    }

    /**
     * 用户是否存在
     * @param bean
     * @return
     */
    public boolean isUserExisted(UserBean bean) {
        boolean isUserExisted = false;
        Cursor cursor = mSqlHelper.openDatabase().query(UsersTable.TABLE_NAME, null,
                UsersTable.SQL_USER_EXISTED_WHERE,
                new String[]{bean.getNickname()}, null, null, null);
        if (cursor.moveToNext()) {
            isUserExisted = true;
        }

        cursor.close();
        mSqlHelper.closeDatabase();
        return isUserExisted;
    }

    /**
     * 匹配用户，对比用户的昵称密码
     * @param bean
     * @return
     */
    public boolean matchUser(UserBean bean) {
        boolean isMatched = false;
        Cursor cursor = mSqlHelper.openDatabase().query(UsersTable.TABLE_NAME, null,
                UsersTable.SQL_MATCH_USER_WHERE,
                new String[]{bean.getNickname(), bean.getPassword()}, null, null, null);
        if (cursor.moveToNext()) {
            isMatched = true;
        }

        cursor.close();
        mSqlHelper.closeDatabase();
        return isMatched;
    }

    /**
     * 通过昵称查找用户信息
     * @param nickname
     * @return
     */
    public int queryUserId(String nickname) {
        Cursor cursor = mSqlHelper.openDatabase().query(UsersTable.TABLE_NAME, null,
                UsersTable.SQL_QUERY_USER_WHERE,
                new String[]{nickname}, null, null, null);
        int uid = -1;
        if (cursor.moveToNext()) {
            uid = Integer.valueOf(cursor.getString(cursor.getColumnIndex(UsersTable.USER_ID)));
        }

        cursor.close();
        mSqlHelper.closeDatabase();
        return uid;
    }

    /**
     * 插入用户数据
     * @param bean
     * @return
     */
    public boolean insertUsers(UserBean bean) {
        ContentValues values = new ContentValues();
        values.put(UsersTable.NICKNAME, bean.getNickname());
        values.put(UsersTable.PASSWORD, bean.getPassword());
        long errCode = mSqlHelper.openDatabase().insert(UsersTable.TABLE_NAME, null, values);
        mSqlHelper.closeDatabase();
        if (errCode == -1) {
            return false;
        }

        return true;
    }

    /**
     * 修改用户密码
     * @param bean
     * @return
     */
    public boolean updateUserPassword(UserBean bean) {
        ContentValues values = new ContentValues();
        values.put(UsersTable.PASSWORD, bean.getPassword());
        int affectedRows = mSqlHelper.openDatabase().update(UsersTable.TABLE_NAME, values,
                UsersTable.SQL_UPDATE_USER_WHERE,
                new String[]{bean.getUserId() + ""});
        mSqlHelper.closeDatabase();
        if (affectedRows > 0) {
            return true;
        }

        return false;
    }

    /**
     * 修改用户昵称
     * @param bean
     * @return
     */
    public boolean updateUserNickname(UserBean bean) {
        ContentValues values = new ContentValues();
        values.put(UsersTable.NICKNAME, bean.getNickname());
        int affectedRows = mSqlHelper.openDatabase().update(UsersTable.TABLE_NAME, values,
                UsersTable.SQL_UPDATE_USER_WHERE,
                new String[]{bean.getUserId() + ""});
        mSqlHelper.closeDatabase();
        if (affectedRows > 0) {
            return true;
        }

        return false;
    }
}
