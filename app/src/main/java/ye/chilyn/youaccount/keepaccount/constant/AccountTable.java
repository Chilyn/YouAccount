package ye.chilyn.youaccount.keepaccount.constant;

import ye.chilyn.youaccount.AccountApplication;
import ye.chilyn.youaccount.R;

/**
 * Created by Alex on 2018/1/16.
 * 账目表相关常量
 */

public interface AccountTable {
    String TABLE_NAME = AccountApplication.getAppContext().getString(R.string.table_name_accounts);
    String USER_ID = AccountApplication.getAppContext().getString(R.string.table_user_id);
    String MONEY = AccountApplication.getAppContext().getString(R.string.table_money);
    String BILL_TYPE = AccountApplication.getAppContext().getString(R.string.table_bill_type);
    String PAYMENT_TIME_MILL = AccountApplication.getAppContext().getString(R.string.table_payment_time_mill);
    String PAYMENT_TIME = AccountApplication.getAppContext().getString(R.string.table_payment_time);
    String TOTAL_PAYMENT = AccountApplication.getAppContext().getString(R.string.table_total_payment);

    String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" +
            USER_ID + " INTEGER NOT NULL," +
            MONEY + " FLOAT DEFAULT 0.0," +
            BILL_TYPE + " TEXT DEFAULT ''," +
            PAYMENT_TIME_MILL +" INTEGER DEFAULT 0," +
            PAYMENT_TIME + " TEXT DEFAULT '')";

    String SQL_DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    String SQL_QUERY_ACCOUNTS_WHERE = USER_ID + "=? AND " + PAYMENT_TIME_MILL + ">=? AND " + PAYMENT_TIME_MILL + "<=?";

    String SQL_QUERY_ACCOUNTS_ORDER_BY = PAYMENT_TIME_MILL + " DESC";

    String SQL_CALCULATE_ACCOUNTS_COLUMN = "sum(" + MONEY + ") as " + TOTAL_PAYMENT;

    String SQL_DELETE_ACCOUNT_WHERE = USER_ID + "=? AND " + PAYMENT_TIME_MILL + "=?";

    String SQL_UPDATE_ACCOUNT_WHERE = SQL_DELETE_ACCOUNT_WHERE;
}
