package ye.chilyn.youaccounts;

import android.os.Bundle;

import ye.chilyn.youaccounts.base.BaseActivity;

public class WelcomeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
    }

    @Override
    protected void destroyViews() {
    }

    @Override
    protected void releaseModels() {
    }
}
