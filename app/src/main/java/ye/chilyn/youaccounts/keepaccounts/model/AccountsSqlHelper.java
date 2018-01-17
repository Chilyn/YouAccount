package ye.chilyn.youaccounts.keepaccounts.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import ye.chilyn.youaccounts.AccountsApplication;
import ye.chilyn.youaccounts.R;
import ye.chilyn.youaccounts.contant.AppFilePath;
import ye.chilyn.youaccounts.keepaccounts.contant.AccountsTable;
import ye.chilyn.youaccounts.keepaccounts.entity.AccountsBean;

/**
 * Created by Alex on 2018/1/16.
 */

public class AccountsSqlHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = AppFilePath.DB_FILE_PATH +
            AccountsApplication.getAppContext().getString(R.string.db_name);
    private static final int VERSION = 1;

    public AccountsSqlHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(AccountsTable.SQL_DROP_TABLE);
        db.execSQL(AccountsTable.SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public boolean insertAccounts(AccountsBean bean) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(AccountsTable.USER_ID, bean.getUserId());
        values.put(AccountsTable.MONEY, bean.getMoney());
        values.put(AccountsTable.BILL_TYPE, bean.getBillType());
        values.put(AccountsTable.PAYMENT_TIME_MILL, bean.getTimeMill());
        values.put(AccountsTable.PAYMENT_TIME, bean.getTime());
        long errCode = db.insert(AccountsTable.TABLE_NAME, null, values);
        db.close();
        if (errCode == -1) {
            return false;
        }

        return true;
    }

    public List<AccountsBean> queryAccounts(int userId, long startTime, long endTime) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(AccountsTable.TABLE_NAME, null,
                AccountsTable.USER_ID + "=? and " + AccountsTable.PAYMENT_TIME_MILL + ">=? and " + AccountsTable.PAYMENT_TIME_MILL + "<=?",
                new String[]{userId + "", startTime + "", endTime + ""},
                null, null,
                AccountsTable.PAYMENT_TIME_MILL);

        List<AccountsBean> listAccountsBean = new ArrayList<>();
        while(cursor.moveToNext()) {
            AccountsBean bean = new AccountsBean();
            int uid = Integer.valueOf(cursor.getString(cursor.getColumnIndex(AccountsTable.USER_ID)));
            float money = cursor.getFloat(cursor.getColumnIndex(AccountsTable.MONEY));
            String billType = cursor.getString(cursor.getColumnIndex(AccountsTable.BILL_TYPE));
            long timeMill = Long.valueOf(cursor.getColumnIndex(AccountsTable.PAYMENT_TIME_MILL));
            String time = cursor.getString(cursor.getColumnIndex(AccountsTable.PAYMENT_TIME));
            bean.setUserId(uid);
            bean.setMoney(money);
            bean.setBillType(billType);
            bean.setTimeMill(timeMill);
            bean.setTime(time);
            listAccountsBean.add(bean);
        }

        cursor.close();
        db.close();
        return listAccountsBean;
    }
}
