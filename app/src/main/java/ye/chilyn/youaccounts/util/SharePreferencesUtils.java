package ye.chilyn.youaccounts.util;

import android.content.Context;
import android.content.SharedPreferences;

import ye.chilyn.youaccounts.AccountsApplication;
import ye.chilyn.youaccounts.R;

/**
 * 共享参数工具类
 */
public class SharePreferencesUtils {

    private static final String NAME = AccountsApplication.getAppContext().getString(R.string.app_name);
    private static Context mContext;

    public static void init(Context context) {
        mContext = context;
    }

    public static boolean save(String key, String value) {
        SharedPreferences sp = mContext.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(key, value);
        return edit.commit();
    }

    public static boolean save(String key, boolean value) {
        SharedPreferences sp = mContext.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putBoolean(key, value);
        return edit.commit();
    }
    
    public static boolean save(String key, Long value) {
        SharedPreferences sp = mContext.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putLong(key, value);
        return edit.commit();
    }

    public static boolean save(String key, int value) {
        SharedPreferences sp = mContext.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putInt(key, value);
        return edit.commit();
    }

    public static String getStringValue(String key) {
        return mContext.getSharedPreferences(NAME, Context.MODE_PRIVATE).getString(key, null);
    }

    public static boolean getBooleanValue(String key) {
        return mContext.getSharedPreferences(NAME, Context.MODE_PRIVATE).getBoolean(key, false);
    }
    

    public static int getIntValue(String key) {
        return mContext.getSharedPreferences(NAME, Context.MODE_PRIVATE).getInt(key, 0);
    }

    public static boolean contain(String key) {
        return mContext.getSharedPreferences(NAME, Context.MODE_PRIVATE).contains(key);
    }

    public static Long getLongValue(String key) {
        return mContext.getSharedPreferences(NAME, Context.MODE_PRIVATE).getLong(key,0);
    }
}
