package ye.chilyn.youaccounts.base.interfaces;

/**
 * Created by Alex on 2017/4/26.
 */

public interface IBaseModel {
    void handleModelEvent(int type, Object data);
    void setOnRefreshViewListener(OnRefreshViewListener listener);
    void callRefreshView(int type, Object data);
    void onDestroy();

    interface OnRefreshViewListener {
        void onRefreshView(int refreshType, Object data);
    }
}
