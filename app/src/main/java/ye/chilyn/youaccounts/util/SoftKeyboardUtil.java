package ye.chilyn.youaccounts.util;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import ye.chilyn.youaccounts.AccountsApplication;

/**
 * Created by Alex on 2018/1/19.
 */

public class SoftKeyboardUtil {

    public static void forceCloseSoftKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) AccountsApplication.getAppContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘
    }
}
