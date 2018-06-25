package ye.chilyn.youaccount.view;

import android.app.Activity;
import android.support.v4.widget.Space;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ye.chilyn.youaccount.AccountApplication;
import ye.chilyn.youaccount.R;
import ye.chilyn.youaccount.util.DimensionUtils;

/**
 * Created by Alex on 2017/12/1.
 * 标题栏相关操作的View层
 */

public class TitleBarView implements View.OnClickListener{

    /**标题栏根布局*/
    private View mTitleBarView;

    /**
     * 用于设置状态栏间隔的View
     */
    private Space mSpaceView;

    /**左功能键*/
    private View mLeftOptionView;

    /**标题*/
    private TextView mTvTitle;

    /**右功能键*/
    private TextView mRightOptionView;

    /**标题栏所在的Activity*/
    private Activity mActivity;

    /**
     *
     * @param titleBarView 标题栏的layout
     * @param rootActivity 页面所在的Activity
     */
    public TitleBarView(View titleBarView, Activity rootActivity) {
        this.mTitleBarView = titleBarView;
        this.mActivity = rootActivity;
        initViews();
        setStatusBarSpace();
        setListeners();
    }

    private void initViews() {
        mLeftOptionView = findView(R.id.left_option);
        mRightOptionView = findView(R.id.right_option);
        mTvTitle = findView(R.id.tv_title);
    }

    private void setStatusBarSpace() {
        mSpaceView = findView(R.id.space);
        //设置状态栏间隔
        int statusBarHeight = DimensionUtils.getStatusBarHeight(AccountApplication.getAppContext());
        ViewGroup.LayoutParams mLayoutParams = mSpaceView.getLayoutParams();
        mLayoutParams.height = statusBarHeight;
        mSpaceView.setLayoutParams(mLayoutParams);
    }

    private void setListeners() {
        mLeftOptionView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_option:
                mActivity.finish();
                break;
        }
    }

    /**
     * 设置左边功能键监听事件，自定义功能键功能，不进行设置默认功能是退出Activity
     * @param listener
     */
    public void setLeftOptionViewListener(View.OnClickListener listener) {
        mLeftOptionView.setOnClickListener(listener);
    }

    /**
     * 设置右边功能键监听事件，自定义功能键功能，不进行设置默认功能是退出Activity
     * @param listener
     */
    public void setRightOptionViewListener(View.OnClickListener listener) {
        mRightOptionView.setOnClickListener(listener);
    }

    /**
     * 设置标题是否可见
     * @param isVisible
     */
    public void setTitleViewVisibility(boolean isVisible) {
        mTvTitle.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    /**
     * 设置左边功能键是否可见
     * @param isVisible
     */
    public void setLeftOptionViewVisibility(boolean isVisible) {
        mLeftOptionView.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    /**
     * 设置右边功能键是否可见
     * @param isVisible
     */
    public void setRightOptionViewVisibility(boolean isVisible) {
        mRightOptionView.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    /**
     * 设置功能键都不可见
     */
    public void setOptionViewInvisible() {
        mLeftOptionView.setVisibility(View.GONE);
        mRightOptionView.setVisibility(View.GONE);
    }

    /**
     * 设置功能键都可见
     */
    public void setOptionViewVisible() {
        mLeftOptionView.setVisibility(View.VISIBLE);
        mRightOptionView.setVisibility(View.VISIBLE);
    }

    /**
     * 设置标题
     * @param text
     */
    public void setTitle(CharSequence text) {
        mTvTitle.setText(text);
    }

    /**
     * 设置右功能键名称
     * @param text
     */
    public void setRightOptionViewText(CharSequence text) {
        mRightOptionView.setText(text);
    }

    private <T extends View> T findView(int id) {
        return (T) mTitleBarView.findViewById(id);
    }
}
