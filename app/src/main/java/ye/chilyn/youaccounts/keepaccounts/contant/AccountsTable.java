package ye.chilyn.youaccounts.keepaccounts.contant;

/**
 * Created by Alex on 2018/1/16.
 */

public interface AccountsTable {
    String TABLE_NAME = "accounts";
    String USER_ID = "user_id";
    String MONEY = "money";
    String BILL_TYPE = "bill_type";
    String PAYMENT_TIME_MILL = "payment_time_mill";
    String PAYMENT_TIME = "payment_time";

    String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" +
            USER_ID + " INTEGER NOT NULL," +
            MONEY + " FLOAT DEFAULT 0.0," +
            BILL_TYPE + " TEXT DEFAULT ''," +
            PAYMENT_TIME_MILL +" INTEGER DEFAULT 0," +
            PAYMENT_TIME + " TEXT DEFAULT '')";

    String SQL_DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    String SQL_QUERY_ACCOUNTS_WHERE = AccountsTable.USER_ID + "=? and " +
            AccountsTable.PAYMENT_TIME_MILL + ">=? and "
            + AccountsTable.PAYMENT_TIME_MILL + "<=?";

    String SQL_QUERY_ACCOUNTS_ORDER_BY = AccountsTable.PAYMENT_TIME_MILL + " DESC";

    String SQL_DELETE_ACCOUNT_WHERE = AccountsTable.PAYMENT_TIME_MILL + "=?";
}
