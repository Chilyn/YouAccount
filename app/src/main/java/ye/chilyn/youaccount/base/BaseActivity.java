package ye.chilyn.youaccount.base;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by Alex on 2018/1/19.
 * app中Activity基类
 */

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            setStatusBarTranslucentApi19();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setStatusBarTranslucentApi21();
        }
    }

    /**
     * Android4.4以上系统设置透明状态栏
     */
    @TargetApi(19)
    private void setStatusBarTranslucentApi19() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }

    /**
     * Android5.0以上系统设置透明状态栏
     */
    @TargetApi(21)
    private void setStatusBarTranslucentApi21() {
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);
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
