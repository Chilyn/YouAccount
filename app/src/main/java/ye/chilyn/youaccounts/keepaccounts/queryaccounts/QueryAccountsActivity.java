package ye.chilyn.youaccounts.keepaccounts.queryaccounts;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import ye.chilyn.youaccounts.R;
import ye.chilyn.youaccounts.base.BaseActivity;
import ye.chilyn.youaccounts.base.interfaces.IBaseModel;
import ye.chilyn.youaccounts.view.TitleBarView;

public class QueryAccountsActivity extends BaseActivity {

    private TitleBarView mTitleBarView;
    private IBaseModel mKeepAccountsSqlModel;
    private IBaseModel mAccountsCalculateModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_accounts);
        initView();
        initData();
    }

    private void initView() {
        mTitleBarView = new TitleBarView(findView(R.id.title_bar), this);
        mTitleBarView.setRightOptionViewVisibility(false);
    }

    private void initData() {
        mTitleBarView.setTitle(getString(R.string.query_accounts));
    }
}
