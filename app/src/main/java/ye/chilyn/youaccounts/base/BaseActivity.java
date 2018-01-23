package ye.chilyn.youaccounts.base;

import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by Alex on 2018/1/19.
 * app中Activity基类
 */

public abstract class BaseActivity extends AppCompatActivity {

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
