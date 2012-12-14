package me.chengdong.bustime.activity;

import java.util.ArrayList;
import java.util.List;

import me.chengdong.bustime.R;
import me.chengdong.bustime.adapter.FavoriteAdapter;
import me.chengdong.bustime.db.TbFavoriteHandler;
import me.chengdong.bustime.meta.FavoriteType;
import me.chengdong.bustime.model.Favorite;
import me.chengdong.bustime.utils.ParamUtil;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class FavoriteActivity extends Activity implements OnItemClickListener {

    ListView favoriteListView;
    FavoriteAdapter mAdapter;
    final List<Favorite> mFavoriteList = new ArrayList<Favorite>();

    TbFavoriteHandler tbFavoriteHandler = new TbFavoriteHandler(FavoriteActivity.this);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorite);

        favoriteListView = (ListView) findViewById(R.id.favorite_listview);

        mAdapter = new FavoriteAdapter(FavoriteActivity.this, mFavoriteList);
        favoriteListView.setAdapter(mAdapter);

        favoriteListView.setOnItemClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        List<Favorite> list = tbFavoriteHandler.selectAll();
        mFavoriteList.clear();
        mFavoriteList.addAll(list);
        mAdapter.notifyDataSetChanged();

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View convertView, int position, long id) {

        Favorite favorite = this.mFavoriteList.get((int) id);
        if (favorite == null) {
            return;
        }

        Intent intent = new Intent();
        if (FavoriteType.resolve(favorite.getType()) == FavoriteType.LINE) {
            intent.putExtra(ParamUtil.LINE_GUID, favorite.getCode());
            intent.putExtra(ParamUtil.LINE_NUMBER, favorite.getName());
            intent.setClass(this, SingleLineActivity.class);
        } else {
            intent.putExtra(ParamUtil.STATION_CODE, favorite.getCode());
            intent.putExtra(ParamUtil.STATION_NAME, favorite.getName());
            intent.setClass(this, StationBusActivity.class);
        }
        startActivity(intent);

    }

}
