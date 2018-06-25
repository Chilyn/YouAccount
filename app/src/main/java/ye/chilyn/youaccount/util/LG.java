package ye.chilyn.youaccount.util;

import android.util.Log;

import ye.chilyn.youaccount.AccountApplication;
import ye.chilyn.youaccount.BuildConfig;
import ye.chilyn.youaccount.R;

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
