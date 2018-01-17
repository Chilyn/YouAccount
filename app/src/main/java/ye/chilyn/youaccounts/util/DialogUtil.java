package ye.chilyn.youaccounts.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by Alex on 2017/4/14.
 * Dialog工具类，用于创建Dialog
 */

public class DialogUtil {

    /**
     * 创建普通提示弹窗（默认点击取消按钮做弹窗消失操作）
     * @param context
     * @param message 弹窗内容
     * @param negativeText 取消按钮文字
     * @param positiveText 确定按钮文字
     * @param positiveListener 确定按钮点击后的回调
     * @return
     */
    public static AlertDialog createCommonAlertDialog(Context context, String message, String negativeText,
                                                 String positiveText, DialogInterface.OnClickListener positiveListener) {
        DialogInterface.OnClickListener negativeListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        };

        AlertDialog dialog = createCommonAlertDialog(context, message, negativeText, positiveText,
                negativeListener, positiveListener);
        return dialog;
    }

    /**
     * 创建提示性的弹窗（只有确定按钮，点击后弹窗消失）
     * @param context
     * @param msg 弹窗内容
     * @param positiveText 确定按钮文字
     * @return
     */
    public static AlertDialog createNoNegativeDialog(Context context, String msg, String positiveText) {
        DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        };
        return createCommonAlertDialog(context, msg, null, positiveText,
                null, positiveListener);
    }

    /**
     * 创建普通提示弹窗（全部参数自定义）
     * @param context
     * @param message 弹窗内容
     * @param negativeText 取消按钮文字
     * @param positiveText 确定按钮文字
     * @param negativeListener 取消按钮点击后的回调
     * @param positiveListener 确定按钮点击后的回调
     * @return
     */
    public static AlertDialog createCommonAlertDialog(Context context, String message, String negativeText,
                                                 String positiveText, DialogInterface.OnClickListener negativeListener,
                                                 DialogInterface.OnClickListener positiveListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setNegativeButton(negativeText, negativeListener);
        builder.setPositiveButton(positiveText, positiveListener);
        return builder.create();
    }

    /**
     * 自定义布局
     * @param context
     * @param contentView 自己要对contentView的background设置颜色或背景，不然就是透明
     * @return Dialog
     */
    public static Dialog createDialog (Context context, View contentView) {
        AlertDialog dialog = new AlertDialog.Builder(context).create();
        dialog.show();
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        Window window = dialog.getWindow();
        window.setContentView(contentView, params);
        /**
         *最外层decorView有padding，在有些手机（如oppo）上如果不设置padding为0，contentView的shape圆角效果有白色边角，
         *但是设置完后如果不设置decorView.setBackgroundColor(Color.TRANSPARENT)或者window.setBackgroundDrawableResource(android.R.color.transparent)，
         *就挤掉contentView边框，所以两种方法设置其中一个都可以实现效果
         */
//        window.setBackgroundDrawableResource(android.R.color.transparent);
        View decorView = dialog.getWindow().getDecorView();
        decorView.setPadding(0, 0, 0, 0);
        decorView.setBackgroundColor(Color.TRANSPARENT);
        dialog.dismiss();
        return dialog;
    }

    /**
     * 自定义布局
     * @param context
     * @param layoutResId 自定义布局的layout资源id，自己要对contentView的background设置颜色或背景，不然就是透明
     * @return Dialog
     */
    public static Dialog createDialog (Context context, int layoutResId) {
        AlertDialog dialog = new AlertDialog.Builder(context).create();
        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(layoutResId);
        /**
         *最外层decorView有padding，在有些手机（如oppo）上如果不设置padding为0，contentView的shape圆角效果有白色边角，
         *但是设置完后如果不设置decorView.setBackgroundColor(Color.TRANSPARENT)或者window.setBackgroundDrawableResource(android.R.color.transparent)，
         *就挤掉contentView边框，所以两种方法设置其中一个都可以实现效果
         */
//        window.setBackgroundDrawableResource(android.R.color.transparent);
        View decorView = dialog.getWindow().getDecorView();
        decorView.setPadding(0, 0, 0, 0);
        decorView.setBackgroundColor(Color.TRANSPARENT);
        dialog.dismiss();
        return dialog;
    }

    /**
     * 自定义布局及其宽高，默认屏幕居中显示
     * @param context
     * @param contentView 自己要对contentView的background设置颜色或背景，不然就是透明
     * @param dpWidth 宽度，单位dp
     * @param dpHeight 高度，单位dp
     * @return Dialog
     */
    public static Dialog createDialog (Context context, View contentView, int dpWidth, int dpHeight) {
        AlertDialog dialog = new AlertDialog.Builder(context).create();
        dialog.show();
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        Window window = dialog.getWindow();
        window.setContentView(contentView, params);
        window.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams windowParams  = window.getAttributes();
        windowParams.width = DimensionUtils.dip2Px(context, dpWidth);
        windowParams.height = DimensionUtils.dip2Px(context, dpHeight);
        window.setAttributes(windowParams);
        View decorView = dialog.getWindow().getDecorView();
        decorView.setPadding(0, 0, 0, 0);
        decorView.setBackgroundColor(Color.TRANSPARENT);
        dialog.dismiss();
        return dialog;
    }

    /**
     * 自定义布局及其宽高，默认屏幕居中显示
     * @param context
     * @param cancelDialogViewId 取消弹窗的view的id,值为0代表不在方法内设置点击监听
     * @param contentView 自己要对contentView的background设置颜色或背景，不然就是透明
     * @param dpWidth 宽度，单位dp
     * @param dpHeight 高度，单位dp
     * @return Dialog
     */
    public static Dialog createDialog (Context context, int cancelDialogViewId, View contentView, int dpWidth, int dpHeight) {
        AlertDialog dialog = new AlertDialog.Builder(context).create();
        dialog.show();
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        Window window = dialog.getWindow();
        window.setContentView(contentView, params);
        window.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams windowParams  = window.getAttributes();
        windowParams.width = DimensionUtils.dip2Px(context, dpWidth);
        windowParams.height = DimensionUtils.dip2Px(context, dpHeight);
        window.setAttributes(windowParams);
        View decorView = dialog.getWindow().getDecorView();
        decorView.setPadding(0, 0, 0, 0);
        decorView.setBackgroundColor(Color.TRANSPARENT);
        dialog.dismiss();
        //取消弹窗的view设置点击监听
        if (cancelDialogViewId != 0) {
            OnCancelDialogViewClickListener listener = new OnCancelDialogViewClickListener(dialog);
            contentView.findViewById(cancelDialogViewId).setOnClickListener(listener);
        }
        return dialog;
    }

    /**
     * 自定义布局及其宽高，默认屏幕居中显示
     * @param context
     * @param layoutResId 自定义布局的layout资源id，自己要对contentView的background设置颜色或背景，不然就是透明
     * @param dpWidth 宽度，单位dp
     * @param dpHeight 高度，单位dp
     * @return Dialog
     */
    public static Dialog createDialog (Context context, int layoutResId, int dpWidth, int dpHeight) {
        AlertDialog dialog = new AlertDialog.Builder(context).create();
        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(layoutResId);
        window.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams windowParams  = window.getAttributes();
        windowParams.width = DimensionUtils.dip2Px(context, dpWidth);
        windowParams.height = DimensionUtils.dip2Px(context, dpHeight);
        window.setAttributes(windowParams);
        View decorView = dialog.getWindow().getDecorView();
        decorView.setPadding(0, 0, 0, 0);
        decorView.setBackgroundColor(Color.TRANSPARENT);
        dialog.dismiss();
        return dialog;
    }

    /**
     * 自定义布局及其宽高，屏幕位置
     * @param context
     * @param contentView 自己要对contentView的background设置颜色或背景，不然就是透明
     * @param dpWidth 宽度，单位dp
     * @param dpHeight 高度，单位dp
     * @param gravity 参考Gravity类，dialog在屏幕中的位置
     * @return Dialog
     */
    public static Dialog createDialog (Context context, View contentView, int dpWidth, int dpHeight, int gravity) {
        AlertDialog dialog = new AlertDialog.Builder(context).create();
        dialog.show();
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        Window window = dialog.getWindow();
        window.setContentView(contentView, params);
        window.setGravity(gravity);
        WindowManager.LayoutParams windowParams  = window.getAttributes();
        windowParams.width = DimensionUtils.dip2Px(context, dpWidth);
        windowParams.height = DimensionUtils.dip2Px(context, dpHeight);

        window.setAttributes(windowParams);
        View decorView = dialog.getWindow().getDecorView();
        decorView.setPadding(0, 0, 0, 0);
        decorView.setBackgroundColor(Color.TRANSPARENT);
        dialog.dismiss();
        return dialog;
    }

    /**
     * 自定义布局及其宽高，屏幕位置，dialog外部透明度
     * @param context
     * @param contentView 自己要对contentView的background设置颜色或背景，不然就是透明
     * @param dpWidth 宽度，单位dp
     * @param dpHeight 高度，单位dp
     * @param gravity 参考Gravity类，dialog在屏幕中的位置
     * @param dimAmount dialog周围的透明度
     * @param cancelOnOutsideTouch dialog周围点击能否取消
     * @return Dialog
     */
    public static Dialog createDialog (Context context, View contentView, int dpWidth, int dpHeight,
                                       int gravity, float dimAmount, boolean cancelOnOutsideTouch) {
        AlertDialog dialog = new AlertDialog.Builder(context).create();
        dialog.show();
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        Window window = dialog.getWindow();
        window.setContentView(contentView, params);
        window.setGravity(gravity);
        WindowManager.LayoutParams windowParams  = window.getAttributes();
        windowParams.width = DimensionUtils.dip2Px(context, dpWidth);
        windowParams.height = DimensionUtils.dip2Px(context, dpHeight);
        windowParams.dimAmount = dimAmount; //dialog外部的透明度
        window.setAttributes(windowParams);
        View decorView = dialog.getWindow().getDecorView();
        decorView.setPadding(0, 0, 0, 0);
        decorView.setBackgroundColor(Color.TRANSPARENT);
        dialog.setCanceledOnTouchOutside(cancelOnOutsideTouch);//外部点击可否取消
        dialog.dismiss();
        return dialog;
    }

    public static ProgressDialog createDefaultProgressDialog(Context context, String message, boolean canceledOnTouchOutside) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage(message);
        dialog.setCanceledOnTouchOutside(canceledOnTouchOutside);
        return dialog;
    }

    private static class OnCancelDialogViewClickListener implements View.OnClickListener {

        private Dialog mDialog;

        public OnCancelDialogViewClickListener(Dialog dialog) {
            mDialog = dialog;
        }

        @Override
        public void onClick(View v) {
            if (mDialog != null && mDialog.isShowing()) {
                mDialog.dismiss();
            }
        }
    }

}
