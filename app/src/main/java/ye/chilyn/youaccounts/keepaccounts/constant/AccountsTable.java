package ye.chilyn.youaccounts.keepaccounts.constant;

import ye.chilyn.youaccounts.AccountsApplication;
import ye.chilyn.youaccounts.R;

/**
 * Created by Alex on 2018/1/16.
 * 账目表相关常量
 */

public interface AccountsTable {
    String TABLE_NAME = AccountsApplication.getAppContext().getString(R.string.table_name_accounts);
    String USER_ID = AccountsApplication.getAppContext().getString(R.string.table_user_id);
    String MONEY = AccountsApplication.getAppContext().getString(R.string.table_money);
    String BILL_TYPE = AccountsApplication.getAppContext().getString(R.string.table_bill_type);
    String PAYMENT_TIME_MILL = AccountsApplication.getAppContext().getString(R.string.table_payment_time_mill);
    String PAYMENT_TIME = AccountsApplication.getAppContext().getString(R.string.table_payment_time);
    String TOTAL_PAYMENT = AccountsApplication.getAppContext().getString(R.string.table_total_payment);

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
