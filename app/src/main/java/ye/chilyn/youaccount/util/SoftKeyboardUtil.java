package ye.chilyn.youaccount.util;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import ye.chilyn.youaccount.AccountApplication;

/**
 * Created by Alex on 2018/1/19.
 * 软键盘工具类
 */

public class SoftKeyboardUtil {

    /**
     * 强制关闭键盘
     * @param view
     */
    public static void forceCloseSoftKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) AccountApplication.getAppContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘
    }

    /**
     * 弹出键盘
     * @param view
     */
    public static void showSoftKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) AccountApplication.getAppContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }
}
