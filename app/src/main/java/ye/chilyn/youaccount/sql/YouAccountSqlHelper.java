package ye.chilyn.youaccount.sql;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import ye.chilyn.youaccount.AccountApplication;
import ye.chilyn.youaccount.constant.AppFilePath;
import ye.chilyn.youaccount.constant.UsersTable;
import ye.chilyn.youaccount.keepaccount.constant.AccountTable;

/**
 * Created by Alex on 2018/1/29.
 */

public class YouAccountSqlHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private int mDbOpenCount = 0;
    private SQLiteDatabase mDb;

    private static class InstanceHolder {
        private static final YouAccountSqlHelper mInstance = new YouAccountSqlHelper();
    }

    public static YouAccountSqlHelper getInstance() {
        return InstanceHolder.mInstance;
    }

    private YouAccountSqlHelper() {
        super(AccountApplication.getAppContext(), AppFilePath.DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(AccountTable.SQL_DROP_TABLE);
        db.execSQL(AccountTable.SQL_CREATE_TABLE);
        db.execSQL(UsersTable.SQL_DROP_TABLE);
        db.execSQL(UsersTable.SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    /**
     * 打开数据库
     * @return
     */
    public synchronized SQLiteDatabase openDatabase() {
        if (mDbOpenCount == 0) {
            mDb = getWritableDatabase();
        }

        mDbOpenCount++;
        return mDb;
    }

    /**
     * 关闭数据库
     */
    public synchronized void closeDatabase() {
        mDbOpenCount--;
        if (mDbOpenCount == 0) {
            mDb.close();
        }
    }
}
