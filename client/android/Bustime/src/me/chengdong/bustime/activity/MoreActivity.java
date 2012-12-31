package me.chengdong.bustime.activity;

import java.util.ArrayList;
import java.util.List;

import me.chengdong.bustime.R;
import me.chengdong.bustime.adapter.MoreAdapter;
import me.chengdong.bustime.model.More;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class MoreActivity extends Activity implements OnItemClickListener {

    ListView mListView;
    private final List<More> mList = new ArrayList<More>(0);
    private MoreAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.more);

        mListView = (ListView) findViewById(R.id.more_listview);

        mAdapter = new MoreAdapter(MoreActivity.this, mList);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);

        mList.add(new More("应用推荐", LineActivity.class));
        mList.add(new More("更新版本", UpdateVersionActivity.class));
        mList.add(new More("我要反馈", UpdateVersionActivity.class));
        mList.add(new More("关于我们", AboutActivity.class));
        mAdapter.notifyDataSetInvalidated();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View convertView, int position, long id) {

        More more = this.mList.get((int) id);

        Intent intent = new Intent();
        intent.setClass(this, more.getActivity());
        setIntent(intent);
        startActivity(intent);
    }

}
