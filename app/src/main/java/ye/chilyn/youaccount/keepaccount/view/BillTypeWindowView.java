package ye.chilyn.youaccount.keepaccount.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;

import java.util.List;

import ye.chilyn.youaccount.AccountApplication;
import ye.chilyn.youaccount.R;
import ye.chilyn.youaccount.keepaccount.adapter.BillTypeAdapter;
import ye.chilyn.youaccount.keepaccount.interfaces.OnBillTypeSelectedListener;
import ye.chilyn.youaccount.util.DimensionUtils;

/**
 * Created by Alex on 2018/1/18.
 * 账目类型弹窗的View层
 */

public class BillTypeWindowView {

    private View mAnchorView;
    private Context mContext;
    private PopupWindow mWindowBillType;
    private View mBillTypeWindowView;
    private ListView mLvBillType;
    private BillTypeAdapter mAdapterBillType;
    private int mWindowWidth;
    private int mWindowHeight = DimensionUtils.dip2Px(AccountApplication.getAppContext(), 170);
    private int mWindowXOffset = DimensionUtils.dip2Px(AccountApplication.getAppContext(), -5);
    private int mWindowYOffset = DimensionUtils.dip2Px(AccountApplication.getAppContext(), -3);
    private OnBillTypeSelectedListener mBillTypeSelectedListener;
    private boolean mHasDeleteMode;

    public BillTypeWindowView(View anchorView, OnBillTypeSelectedListener listener, boolean hasDeleteMode) {
        this.mHasDeleteMode = hasDeleteMode;
        init(anchorView, listener);
    }

    public BillTypeWindowView(View anchorView, OnBillTypeSelectedListener listener) {
        init(anchorView, listener);
    }

    private void init(View anchorView, OnBillTypeSelectedListener listener) {
        this.mAnchorView = anchorView;
        this.mContext = anchorView.getContext();
        this.mBillTypeSelectedListener = listener;
        initViews();
        initData();
        setViewListener();
    }

    private void initViews() {
        mBillTypeWindowView = LayoutInflater.from(mContext).inflate(R.layout.dialog_choose_bill_type, null);
        mLvBillType = (ListView) mBillTypeWindowView.findViewById(R.id.lv);
    }

    private void initData() {
        mAdapterBillType = new BillTypeAdapter(mContext);
        mLvBillType.setAdapter(mAdapterBillType);
        //根据锚控件的宽度设置宽度
        ViewGroup.LayoutParams params = mAnchorView.getLayoutParams();
        mWindowWidth = params.width + DimensionUtils.dip2Px(AccountApplication.getAppContext(), 10);
    }

    private void setViewListener() {
        mLvBillType.setOnItemClickListener(mOnItemClickListener);
        if (mHasDeleteMode) {
            mLvBillType.setOnItemLongClickListener(mOnItemLongClickListener);
        }
    }

    private AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String billType = mAdapterBillType.getItem(position);
            if (mBillTypeSelectedListener != null) {
                mBillTypeSelectedListener.onItemSelected(billType);
            }
            dismiss();
        }
    };

    private AdapterView.OnItemLongClickListener mOnItemLongClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            String billType = mAdapterBillType.getItem(position);
            if (mAdapterBillType.isDeletableBillType(billType)) {
                DeleteBillTypeDialog dialog = new DeleteBillTypeDialog(mContext, billType);
                dialog.setConfirmDeleteListener(new DeleteBillTypeDialog.ConfirmDeleteListener() {
                    @Override
                    public void confirmDelete(String billType) {
                        mBillTypeSelectedListener.onItemDelete(billType);
                    }
                });
                dialog.show();
            }

            return true;
        }
    };

    public void show() {
        if (mWindowBillType == null) {
            initWindow();
        }

        mWindowBillType.showAsDropDown(mAnchorView, mWindowXOffset, mWindowYOffset);
    }

    private void initWindow() {
        mWindowBillType = new PopupWindow(mBillTypeWindowView, mWindowWidth, mWindowHeight, true);
        // 设置PopupWindow的背景
        mWindowBillType.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // 设置PopupWindow是否能响应外部点击事件
        mWindowBillType.setOutsideTouchable(true);
        // 设置PopupWindow是否能响应点击事件
        mWindowBillType.setTouchable(true);
    }

    private void dismiss() {
        if (mWindowBillType == null) {
            return;
        }

        mWindowBillType.dismiss();
    }

    public void updateCustomBillTypes(List<String> newData) {
        mAdapterBillType.updateCustomData(newData);
    }
}
