package ye.chilyn.youaccounts.util;

import android.app.Service;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class DimensionUtils {
	
	/** 大平板最小尺寸 */
	public static final int TABLET_MIN_SIZE = 8;
	
	/**
	 * 像素转换成独立像素
	 * @param context 上下文
	 * @param px           像素值
	 * @return
	 */
	public static int px2Dip(Context context, int px) {
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (px / scale + 0.5f * (px >= 0 ? 1 : -1));
	}

	/**
	 * 独立像素转换成像素
	 * @param context 上下文
	 * @param dip           独立像素值
	 * @return
	 */
	public static int dip2Px(Context context, int dip) {
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dip * scale + 0.5f * (dip >= 0 ? 1 : -1));
	}
	
	/**
	 * 获取屏幕宽度
	 * @param context
	 * @return 单位：px
	 */
	public static int getScreenWidth(Context context){
		return getScreenDimen(context, 0);
	}
	
	/**
	 * 获取屏幕宽度
	 * @param context
	 * @param percent 百分比(0-100)
	 * @return 单位：px
	 */
	public static int getScreenWidth(Context context, int percent){
		int width = getScreenDimen(context, 0);
		return width*percent/100;
	}
	
	/**
	 * 获取屏幕高度
	 * @param context
	 * @return 单位：px
	 */
	public static int getScreenHeight(Context context){
		return getScreenDimen(context, 1);
	}
	
	/**
	 * 获取屏幕高度
	 * @param context
	 * @param percent 百分比(0-100)
	 * @return 单位：px
	 */
	public static int getScreenHeight(Context context, int percent){
		int height = getScreenDimen(context, 1);
		return height*percent/100;
	}
	
	/**获取屏幕尺寸
	 * @param context
	 * @param flag  宽度或长度的标志，如果为0，则返回屏幕宽度，否则为高度
	 * @return  返回值
	 */
	public static int getScreenDimen(Context context, int flag){
		WindowManager wm = (WindowManager)context.getSystemService(Service.WINDOW_SERVICE);
		DisplayMetrics metrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(metrics);
		if(0 == flag){
			return metrics.widthPixels;
		}else{
			return metrics.heightPixels;
		}
	}
	
	/**
	 * 是否大平板，屏幕尺寸 {@link #getScreenSize(Context)} >= {@link #TABLET_MIN_SIZE}
	 * @param context
	 * @return
	 */
	public static boolean isLargeTablet(Context context){
		return getScreenSize(context) >= TABLET_MIN_SIZE;
	}
	
	/**
	 * 获取屏幕尺寸
	 * @param context
	 * @return 屏幕尺寸，如 5.0，5.5
	 */
	public static double getScreenSize(Context context){
		WindowManager wm = (WindowManager)context.getSystemService(Service.WINDOW_SERVICE);
		DisplayMetrics metrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(metrics);
		double diagonalPixels = Math.sqrt(Math.pow(metrics.widthPixels, 2) + Math.pow(metrics.heightPixels, 2));
		return diagonalPixels / (metrics.density * metrics.densityDpi);
	}
	
	/**
	 * 判断是否为平板
	 * @param context
	 * @return
	 */
	public static boolean isTablet(Context context) {
		return (context.getResources().getConfiguration().screenLayout 
				& android.content.res.Configuration.SCREENLAYOUT_SIZE_MASK) 
				>= android.content.res.Configuration.SCREENLAYOUT_SIZE_LARGE;
	}
	
	/**
	 * 获取屏幕当前方向
	 * @param context
	 * @return 
	 */
	public static int getOrientation(Context context) {
		int w = getScreenWidth(context);
		int h = getScreenHeight(context);
		int orientation = 0;
		if (w < h) {
			orientation = android.content.res.Configuration.ORIENTATION_PORTRAIT;
		} else {
			orientation = android.content.res.Configuration.ORIENTATION_LANDSCAPE;
		}
		return orientation;
	}
	
	/**
	 * 屏幕是否为竖屏
	 * @param context
	 * @return
	 */
	public static boolean isScreenPortait(Context context){
		int orientationFlag = getOrientation(context);
		int portaitFlag = android.content.res.Configuration.ORIENTATION_PORTRAIT;
		return orientationFlag == portaitFlag;
	}
	
	/**
	 * 屏幕是否为横屏
	 * @param context
	 * @return
	 */
	public static boolean isScreenLandscape(Context context){
		int orientationFlag = getOrientation(context);
		int LandscapeFlag = android.content.res.Configuration.ORIENTATION_LANDSCAPE;
		return orientationFlag == LandscapeFlag;
	}
	
	/**
     * 用于获取状态栏的高度。 使用Resource对象获取（推荐这种方式）
     *
     * @return 返回状态栏高度的像素值。
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen",
                "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

}
