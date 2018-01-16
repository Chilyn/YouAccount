package ye.chilyn.youaccounts.keepaccounts.model;

import ye.chilyn.youaccounts.base.BaseModel;

/**
 * Created by Alex on 2018/1/15.
 */

public class KeepAccountsSqlModel extends BaseModel {

    public KeepAccountsSqlModel(OnRefreshViewListener listener) {
        super(listener);
    }

    @Override
    public void handleModelEvent(int type, Object data) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
