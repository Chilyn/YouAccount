package ye.chilyn.youaccounts.util;

import android.content.Context;
import android.widget.Toast;

import ye.chilyn.youaccounts.AccountsApplication;

/**
 * Created by Alex on 2017/4/10.
 */

public class ToastUtil {

    public static void showShortToast(String msg) {
        Toast.makeText(AccountsApplication.getAppContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public static void showShortToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void showLongToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

}
