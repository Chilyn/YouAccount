package ye.chilyn.youaccounts.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alex on 2018/1/15.
 */

public abstract class CommonAdapter<T, V extends CommonAdapter.ViewHolder> extends BaseAdapter {

    private Context mContext;
    private int mLayoutResId;
    protected List<T> mListData = new ArrayList<>();

    public CommonAdapter(Context context, int layoutResId) {
        this.mContext = context;
        this.mLayoutResId = layoutResId;
    }

    @Override
    public int getCount() {
        return mListData.size();
    }

    @Override
    public T getItem(int position) {
        return mListData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        V holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(mLayoutResId, parent, false);
            holder = onCreateViewHolder(convertView, getItemViewType(position));
            convertView.setTag(holder);
        } else {
            holder = (V) convertView.getTag();
        }

        onBindViewHolder(holder, getItem(position), position);
        return convertView;
    }

    protected abstract V onCreateViewHolder(View view, int viewType);
    protected abstract void onBindViewHolder(V holder, T item, int position);
    protected abstract void setListData(List<T> data);

    public abstract class ViewHolder {
        public ViewHolder(View rootView) {
        }
    }
}
