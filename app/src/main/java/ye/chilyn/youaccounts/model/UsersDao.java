package ye.chilyn.youaccounts.model;

import android.content.ContentValues;
import android.database.Cursor;

import ye.chilyn.youaccounts.contant.UsersTable;
import ye.chilyn.youaccounts.entity.UserBean;

/**
 * Created by Alex on 2018/1/29.
 */

public class UsersDao {

    private YouAccountsSqlHelper mSqlHelper = YouAccountsSqlHelper.getInstance();

    public UsersDao() {
    }

    public boolean isUserExisted(UserBean bean) {
        Cursor cursor = mSqlHelper.getWritableDatabase().query(UsersTable.TABLE_NAME, null,
                UsersTable.SQL_USER_EXISTED_WHERE,
                new String[]{bean.getNickname()}, null, null, null);
        if (cursor.moveToNext()) {
            return true;
        }

        cursor.close();
        mSqlHelper.closeDatabase();
        return false;
    }

    public boolean matchUser(UserBean bean) {
        Cursor cursor = mSqlHelper.getWritableDatabase().query(UsersTable.TABLE_NAME, null,
                UsersTable.SQL_MATCH_USER_WHERE,
                new String[]{bean.getNickname(), bean.getPassword()}, null, null, null);
        if (cursor.moveToNext()) {
            return true;
        }

        cursor.close();
        mSqlHelper.closeDatabase();
        return false;
    }

    public boolean insertUsers(UserBean bean) {
        ContentValues values = new ContentValues();
        values.put(UsersTable.NICKNAME, bean.getNickname());
        values.put(UsersTable.PASSWORD, bean.getPassword());
        long errCode = mSqlHelper.getWritableDatabase().insert(UsersTable.TABLE_NAME, null, values);
        mSqlHelper.closeDatabase();
        if (errCode == -1) {
            return false;
        }

        return true;
    }
}
