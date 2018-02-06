package ye.chilyn.youaccounts.model;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import ye.chilyn.youaccounts.AccountsApplication;
import ye.chilyn.youaccounts.R;
import ye.chilyn.youaccounts.constant.AppFilePath;
import ye.chilyn.youaccounts.constant.UsersTable;
import ye.chilyn.youaccounts.keepaccounts.constant.AccountsTable;

/**
 * Created by Alex on 2018/1/29.
 */

public class YouAccountsSqlHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = AppFilePath.DB_FILE_PATH +
            AccountsApplication.getAppContext().getString(R.string.app_name) +
            AccountsApplication.getAppContext().getString(R.string.dot) +
            AccountsApplication.getAppContext().getString(R.string.db);
    private static final int VERSION = 1;
    private int mDbOpenCount = 0;
    private SQLiteDatabase mDb;

    private static class InstanceHolder {
        private static final YouAccountsSqlHelper mInstance = new YouAccountsSqlHelper();
    }

    public static YouAccountsSqlHelper getInstance() {
        return InstanceHolder.mInstance;
    }

    private YouAccountsSqlHelper() {
        super(AccountsApplication.getAppContext(), DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(AccountsTable.SQL_DROP_TABLE);
        db.execSQL(AccountsTable.SQL_CREATE_TABLE);
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
