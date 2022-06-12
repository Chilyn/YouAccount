package ye.chilyn.youaccount.sql;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import ye.chilyn.youaccount.keepaccount.constant.AccountTable;
import ye.chilyn.youaccount.keepaccount.entity.AccountBean;

/**
 * Created by Alex on 2018/1/16.
 * 账目数据库操作的Helper类
 */

public class AccountsDao {

    private YouAccountSqlHelper mSqlHelper = YouAccountSqlHelper.getInstance();

    public AccountsDao() {
    }

    /**
     * 插入账目数据
     * @param bean
     * @return
     */
    public boolean insertAccounts(AccountBean bean) {
        ContentValues values = new ContentValues();
        values.put(AccountTable.USER_ID, bean.getUserId());
        values.put(AccountTable.MONEY, bean.getMoney());
        values.put(AccountTable.BILL_TYPE, bean.getBillType());
        values.put(AccountTable.PAYMENT_TIME_MILL, bean.getTimeMill());
        values.put(AccountTable.PAYMENT_TIME, bean.getTime());
        long errCode = mSqlHelper.openDatabase().insert(AccountTable.TABLE_NAME, null, values);
        mSqlHelper.closeDatabase();
        if (errCode == -1) {
            return false;
        }

        return true;
    }

    /**
     * 查询账目数据
     * @param userId
     * @param startTime
     * @param endTime
     * @return
     */
    public List<AccountBean> queryAccounts(int userId, long startTime, long endTime) {
        Cursor cursor = mSqlHelper.openDatabase().query(AccountTable.TABLE_NAME, null,
                AccountTable.SQL_QUERY_ACCOUNTS_WHERE,
                new String[]{userId + "", startTime + "", endTime + ""},
                null, null,
                AccountTable.SQL_QUERY_ACCOUNTS_ORDER_BY);

        List<AccountBean> listAccountBean = new ArrayList<>();
        while(cursor.moveToNext()) {
            AccountBean bean = new AccountBean();
            int uid = Integer.valueOf(cursor.getString(cursor.getColumnIndex(AccountTable.USER_ID)));
            float money = cursor.getFloat(cursor.getColumnIndex(AccountTable.MONEY));
            String billType = cursor.getString(cursor.getColumnIndex(AccountTable.BILL_TYPE));
            long timeMill = cursor.getLong(cursor.getColumnIndex(AccountTable.PAYMENT_TIME_MILL));
            String time = cursor.getString(cursor.getColumnIndex(AccountTable.PAYMENT_TIME));
            bean.setUserId(uid);
            bean.setMoney(money);
            bean.setBillType(billType);
            bean.setTimeMill(timeMill);
            bean.setTime(time);
            listAccountBean.add(bean);
        }

        cursor.close();
        mSqlHelper.closeDatabase();
        return listAccountBean;
    }

    /**
     * 查询给定时间范围内的总支出
     * @param userId
     * @param startTime
     * @param endTime
     * @return
     */
    public float queryTotalPayments(int userId, long startTime, long endTime) {
        float totalPayment = 0.0f;
        Cursor cursor = mSqlHelper.openDatabase().query(AccountTable.TABLE_NAME,
                new String[]{AccountTable.SQL_CALCULATE_ACCOUNTS_COLUMN},
                AccountTable.SQL_QUERY_ACCOUNTS_WHERE,
                new String[]{userId + "", startTime + "", endTime + ""},
                null, null, null);
        if (cursor.moveToNext()) {
            totalPayment = cursor.getFloat(cursor.getColumnIndex(AccountTable.TOTAL_PAYMENT));
        }

        cursor.close();
        mSqlHelper.closeDatabase();
        return totalPayment;
    }

    /**
     * 删除账目数据
     * @param bean
     * @return
     */
    public boolean deleteAccount(AccountBean bean) {
        int deleteRows = mSqlHelper.openDatabase().delete(AccountTable.TABLE_NAME,
                AccountTable.SQL_DELETE_ACCOUNT_WHERE, new String[]{bean.getUserId() + "", bean.getTimeMill() + ""});
        mSqlHelper.closeDatabase();
        if (deleteRows == 0) {
            return false;
        }

        return true;
    }

    /**
     * 更新账目数据
     * @param bean
     * @return
     */
    public boolean updateAccount(AccountBean bean) {
        ContentValues values = new ContentValues();
        values.put(AccountTable.MONEY, bean.getMoney());
        values.put(AccountTable.BILL_TYPE, bean.getBillType());
        values.put(AccountTable.PAYMENT_TIME_MILL, bean.getUpdateTimeMill());
        values.put(AccountTable.PAYMENT_TIME, bean.getTime());
        int affectedRows = mSqlHelper.openDatabase().update(AccountTable.TABLE_NAME, values,
                AccountTable.SQL_UPDATE_ACCOUNT_WHERE,
                new String[]{bean.getUserId() + "", bean.getTimeMill() + ""});
        mSqlHelper.closeDatabase();
        if (affectedRows > 0) {
            return true;
        }

        return false;
    }
}
