package ye.chilyn.youaccount.base;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import ye.chilyn.youaccount.R;

/**
 * Created by Alex on 2018/1/19.
 * app中Activity基类
 */

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        switchStatusBarMode(isDarkFontMode());
    }

    protected boolean isDarkFontMode() {
        return false;
    }

    /**
     * 切换状态栏字体颜色
     *
     * @param isDarkFontMode 状态栏字体深色或亮色
     */
    public void switchStatusBarMode(boolean isDarkFontMode) {
        Window window = getWindow();
        //6.0以上版本设置setSystemUiVisibility进行字体颜色切换
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int visibility = isDarkFontMode ? View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR : View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | visibility);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            return;
        }

        //5.0-6.0版本不能设置深色字体模式，只能将状态栏颜色设置为浅灰才能让字体看清
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int color = isDarkFontMode ? ContextCompat.getColor(this, R.color.colorPrimary) : Color.TRANSPARENT;
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    @Override
    protected void onDestroy() {
        destroyViews();
        releaseModels();
        super.onDestroy();
    }

    /**销毁View层相关数据*/
    protected abstract void destroyViews();
    /**释放Model层*/
    protected abstract void releaseModels();

    public <T extends View> T findView(int id) {
        return (T) findViewById(id);
    }
}
