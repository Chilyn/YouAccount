package ye.chilyn.youaccounts.util;

import android.util.Log;

import ye.chilyn.youaccounts.AccountApplication;
import ye.chilyn.youaccounts.BuildConfig;
import ye.chilyn.youaccounts.R;

/**
 * Created by Alex on 2017/3/31.
 */

public class LG {

    private static boolean isOpen = BuildConfig.DEBUG;
    private static boolean canLogI = true;
    private static final String TAG = AccountApplication.getAppContext().getString(R.string.app_name);

    public static void i(String msg) {
        if (isOpen && canLogI) {
            Log.i(TAG, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (isOpen && canLogI) {
            Log.i(tag, msg);
        }
    }
}
