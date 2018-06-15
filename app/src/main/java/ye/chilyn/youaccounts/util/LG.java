package ye.chilyn.youaccounts.util;

import android.util.Log;

import ye.chilyn.youaccounts.BuildConfig;

/**
 * Created by Alex on 2017/3/31.
 */

public class LG {

    private static boolean isOpen = BuildConfig.DEBUG;
    private static boolean canLogI = true;
    private static final String TAG = "YouAccounts";

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
