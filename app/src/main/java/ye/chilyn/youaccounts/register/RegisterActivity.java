package ye.chilyn.youaccounts.register;

import android.os.Bundle;

import ye.chilyn.youaccounts.R;
import ye.chilyn.youaccounts.base.BaseActivity;

public class RegisterActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    @Override
    protected void destroyViews() {
    }

    @Override
    protected void releaseModels() {
    }
}
