package ye.chilyn.youaccount.base.interfaces;

/**
 * Created by Alex on 2017/4/26.
 * MVC或MVP的Model层基类接口
 */

public interface IBaseModel {
    void handleModelEvent(int type, Object data);
    void setOnRefreshViewListener(OnRefreshViewListener listener);
    void onDestroy();

    interface OnRefreshViewListener {
        void onRefreshView(int refreshType, Object data);
    }
}
