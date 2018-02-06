package ye.chilyn.youaccounts.model;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import ye.chilyn.youaccounts.keepaccounts.constant.AccountsTable;
import ye.chilyn.youaccounts.keepaccounts.entity.AccountsBean;

/**
 * Created by Alex on 2018/1/16.
 * 账目数据库操作的Helper类
 */

public class AccountsDao {

    private YouAccountsSqlHelper mSqlHelper = YouAccountsSqlHelper.getInstance();

    public AccountsDao() {
    }

    /**
     * 插入账目数据
     * @param bean
     * @return
     */
    public boolean insertAccounts(AccountsBean bean) {
        ContentValues values = new ContentValues();
        values.put(AccountsTable.USER_ID, bean.getUserId());
        values.put(AccountsTable.MONEY, bean.getMoney());
        values.put(AccountsTable.BILL_TYPE, bean.getBillType());
        values.put(AccountsTable.PAYMENT_TIME_MILL, bean.getTimeMill());
        values.put(AccountsTable.PAYMENT_TIME, bean.getTime());
        long errCode = mSqlHelper.openDatabase().insert(AccountsTable.TABLE_NAME, null, values);
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
    public List<AccountsBean> queryAccounts(int userId, long startTime, long endTime) {
        Cursor cursor = mSqlHelper.openDatabase().query(AccountsTable.TABLE_NAME, null,
                AccountsTable.SQL_QUERY_ACCOUNTS_WHERE,
                new String[]{userId + "", startTime + "", endTime + ""},
                null, null,
                AccountsTable.SQL_QUERY_ACCOUNTS_ORDER_BY);

        List<AccountsBean> listAccountsBean = new ArrayList<>();
        while(cursor.moveToNext()) {
            AccountsBean bean = new AccountsBean();
            int uid = Integer.valueOf(cursor.getString(cursor.getColumnIndex(AccountsTable.USER_ID)));
            float money = cursor.getFloat(cursor.getColumnIndex(AccountsTable.MONEY));
            String billType = cursor.getString(cursor.getColumnIndex(AccountsTable.BILL_TYPE));
            long timeMill = cursor.getLong(cursor.getColumnIndex(AccountsTable.PAYMENT_TIME_MILL));
            String time = cursor.getString(cursor.getColumnIndex(AccountsTable.PAYMENT_TIME));
            bean.setUserId(uid);
            bean.setMoney(money);
            bean.setBillType(billType);
            bean.setTimeMill(timeMill);
            bean.setTime(time);
            listAccountsBean.add(bean);
        }

        cursor.close();
        mSqlHelper.closeDatabase();
        return listAccountsBean;
    }

    /**
     * 删除账目数据
     * @param bean
     * @return
     */
    public boolean deleteAccount(AccountsBean bean) {
        int deleteRows = mSqlHelper.openDatabase().delete(AccountsTable.TABLE_NAME,
                AccountsTable.SQL_DELETE_ACCOUNT_WHERE, new String[]{bean.getUserId() + "", bean.getTimeMill() + ""});
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
    public boolean updateAccount(AccountsBean bean) {
        ContentValues values = new ContentValues();
        values.put(AccountsTable.MONEY, bean.getMoney());
        values.put(AccountsTable.BILL_TYPE, bean.getBillType());
        int affectedRows = mSqlHelper.openDatabase().update(AccountsTable.TABLE_NAME, values,
                AccountsTable.SQL_UPDATE_ACCOUNT_WHERE,
                new String[]{bean.getUserId() + "", bean.getTimeMill() + ""});
        mSqlHelper.closeDatabase();
        if (affectedRows > 0) {
            return true;
        }

        return false;
    }
}
