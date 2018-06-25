package ye.chilyn.youaccount.keepaccount.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import ye.chilyn.youaccount.R;
import ye.chilyn.youaccount.base.common.CommonAdapter;

/**
 * Created by Alex on 2018/1/18.
 * 账目类型的Adapter
 */

public class BillTypeAdapter extends CommonAdapter<String, BillTypeAdapter.ViewHolder> {

    public BillTypeAdapter(Context context) {
        super(context, R.layout.list_item_bill_type);
        createBillTypeData();
    }

    /**
     * 创建账单类型列表数据
     */
    private void createBillTypeData() {
        String[] billTypes = mContext.getResources().getStringArray(R.array.bill_type);
        for (int i = 0; i < billTypes.length; i++) {
            mListData.add(billTypes[i]);
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

    protected class ViewHolder extends CommonAdapter.ViewHolder {

        private TextView mTvBillType, mTvDivider;

        public ViewHolder(View rootView) {
            super(rootView);
            mTvBillType = (TextView) rootView.findViewById(R.id.tv_bill_type);
            mTvDivider = (TextView) rootView.findViewById(R.id.tv_divider);
        }
    }
}
