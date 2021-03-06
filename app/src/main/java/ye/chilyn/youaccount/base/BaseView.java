package ye.chilyn.youaccount.base;

import android.content.Context;
import android.view.View;

import ye.chilyn.youaccount.base.interfaces.IBaseView;

/**
 * Created by Alex on 2018/1/15.
 * MVC或MVP的View层基类
 */

public abstract class BaseView implements IBaseView {

    private OnHandleModelListener mHandleModelListener;
    protected View mRootView;
    protected Context mContext;

    public BaseView(View rootView, OnHandleModelListener listener) {
        this.mRootView = rootView;
        this.mContext = rootView.getContext();
        setOnHandleModelListener(listener);
    }

    @Override
    public void setOnHandleModelListener(OnHandleModelListener listener) {
        this.mHandleModelListener = listener;
    }

    /**
     * 调用操作Model层的回调接口
     * @param type
     * @param data
     */
    protected void callHandleModel(int type, Object data) {
        if (mHandleModelListener != null) {
            mHandleModelListener.onHandleModel(type, data);
        }
    }

    @Override
    public void onDestroy() {
        mHandleModelListener = null;
        releaseHandler();
    }

    /** 释放Handler */
    protected abstract void releaseHandler();

    public <T extends View> T findView(int id) {
        return (T) mRootView.findViewById(id);
    }
}
