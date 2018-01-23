package ye.chilyn.youaccounts.base.interfaces;

/**
 * Created by Alex on 2017/4/26.
 * MVC或MVP的View层基类接口
 */

public interface IBaseView {
    void initViews();
    void initData();
    void setViewListener();
    void refreshViews(int refreshType, Object data);
    void setOnHandleModelListener(OnHandleModelListener listener);
    void onDestroy();

    interface OnHandleModelListener {
        void onHandleModel(int type, Object data);
    }
}
