package ye.chilyn.youaccount.util;

import android.widget.Toast;

import ye.chilyn.youaccount.AccountApplication;

/**
 * Created by Alex on 2017/4/10.
 */

public class ToastUtil {

    private static final Toast TOAST = Toast.makeText(AccountApplication.getAppContext(), "", Toast.LENGTH_SHORT);

    public static void showShortToast(String msg) {
        TOAST.setText(msg);
        TOAST.show();
    }

    public static void cancelToast() {
        TOAST.cancel();
    }
}
