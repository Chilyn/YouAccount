package ye.chilyn.youaccount.keepaccount.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ye.chilyn.youaccount.R;
import ye.chilyn.youaccount.base.common.CommonAdapter;
import ye.chilyn.youaccount.constant.SharePreferenceKey;
import ye.chilyn.youaccount.util.SharePreferencesUtils;

/**
 * Created by Alex on 2018/1/18.
 * 账目类型的Adapter
 */

public class BillTypeAdapter extends CommonAdapter<String, BillTypeAdapter.ViewHolder> {

    private List<String> mDefaultBillTypes = new ArrayList<>();

    public BillTypeAdapter(Context context) {
        super(context, R.layout.list_item_bill_type);
        createBillTypeData();
    }

    /**
     * 创建账单类型列表数据
     */
    private void createBillTypeData() {
        String[] billTypes = mContext.getResources().getStringArray(R.array.bill_type);
        mDefaultBillTypes.addAll(Arrays.asList(billTypes));
        mListData.addAll(Arrays.asList(billTypes));

        String customJson = SharePreferencesUtils.getStringValue(SharePreferenceKey.BILL_TYPES);
        if (!TextUtils.isEmpty(customJson)) {
            String[] customBillTypes = new Gson().fromJson(customJson, String[].class);
            mListData.addAll(Arrays.asList(customBillTypes));
        }
    }

    @Override
    protected ViewHolder onCreateViewHolder(View view, int viewType) {
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(ViewHolder holder, String item, int position) {
        holder.mTvBillType.setText(item);
        if (position == (getCount() - 1)) {
            holder.mTvDivider.setVisibility(View.GONE);
        } else {
            holder.mTvDivider.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setListData(List<String> data) {
        if (data == null || data.size() == 0) {
            return;
        }

        mListData.clear();
        mListData.addAll(data);
        notifyDataSetChanged();
    }

    public void updateCustomData(List<String> data) {
        mListData.clear();
        mListData.addAll(mDefaultBillTypes);
        mListData.addAll(data);
        notifyDataSetChanged();
    }

    public boolean isDeletableBillType(String billType) {
        return !mDefaultBillTypes.contains(billType);
    }

    protected class ViewHolder extends CommonAdapter.ViewHolder {

        private TextView mTvBillType, mTvDivider;

        public ViewHolder(View rootView) {
            super(rootView);
            mTvBillType = (TextView) rootView.findViewById(R.id.tv_bill_type);
            mTvDivider = (TextView) rootView.findViewById(R.id.tv_divider);
        }
    }
}
