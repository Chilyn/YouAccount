package ye.chilyn.youaccounts.me.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import ye.chilyn.youaccounts.R;
import ye.chilyn.youaccounts.base.BaseFragment;

public class MeFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_me, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ListView lv = findView(R.id.lv);
        List<String> data = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            data.add("" + i);
        }
        ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, data);
        lv.setAdapter(adapter);
    }
}
