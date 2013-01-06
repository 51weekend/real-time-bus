package me.chengdong.bustime.activity;

import java.util.ArrayList;
import java.util.List;

import me.chengdong.bustime.R;
import me.chengdong.bustime.adapter.FavoriteAdapter;
import me.chengdong.bustime.db.TbFavoriteHandler;
import me.chengdong.bustime.meta.FavoriteType;
import me.chengdong.bustime.model.Favorite;
import me.chengdong.bustime.utils.LogUtil;
import me.chengdong.bustime.utils.ParamUtil;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

public class FavoriteActivity extends Activity implements OnItemClickListener, OnClickListener {

    private static String TAG = FavoriteActivity.class.getSimpleName();

    ImageView noFavorite, listBottom;
    ListView favoriteListView;

    Button mSelectStationBtn, mSelectLineBtn, mEditBtn;

    private boolean editable;

    FavoriteAdapter mAdapter;

    final List<Favorite> mFavoriteList = new ArrayList<Favorite>();

    private FavoriteType favoriteType = FavoriteType.STATION;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorite);

        favoriteListView = (ListView) findViewById(R.id.favorite_listview);

        noFavorite = (ImageView) findViewById(R.id.noFavorite);
        listBottom = (ImageView) findViewById(R.id.list_bottom);

        mSelectStationBtn = (Button) findViewById(R.id.btn_select_station);
        mSelectStationBtn.setOnClickListener(this);

        mSelectLineBtn = (Button) findViewById(R.id.btn_select_line);
        mSelectLineBtn.setOnClickListener(this);

        mEditBtn = (Button) findViewById(R.id.btn_edit);
        mEditBtn.setOnClickListener(this);

        mAdapter = new FavoriteAdapter(FavoriteActivity.this, mFavoriteList, FavoriteType.STATION, editable);
        favoriteListView.setAdapter(mAdapter);

        favoriteListView.setOnItemClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        new QueryFavoriteTask().execute();
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

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
        case R.id.btn_select_station:
            mSelectStationBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_left_press));
            mSelectLineBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_right));
            this.favoriteType = FavoriteType.STATION;
            mAdapter = new FavoriteAdapter(FavoriteActivity.this, mFavoriteList, FavoriteType.STATION, editable);
            favoriteListView.setAdapter(mAdapter);
            new QueryFavoriteTask().execute();
            break;
        case R.id.btn_select_line:
            mSelectStationBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_left));
            mSelectLineBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_right_press));
            this.favoriteType = FavoriteType.LINE;
            mAdapter = new FavoriteAdapter(FavoriteActivity.this, mFavoriteList, FavoriteType.LINE, editable);
            favoriteListView.setAdapter(mAdapter);
            new QueryFavoriteTask().execute();
            break;

        case R.id.btn_edit:
            if (editable) {
                editable = false;
                mEditBtn.setText("编辑");
            } else {
                editable = true;
                mEditBtn.setText("完成");
            }
            if (this.favoriteType == FavoriteType.LINE) {
                mAdapter = new FavoriteAdapter(FavoriteActivity.this, mFavoriteList, FavoriteType.LINE, editable);
            } else {
                mAdapter = new FavoriteAdapter(FavoriteActivity.this, mFavoriteList, FavoriteType.STATION, editable);
            }
            favoriteListView.setAdapter(mAdapter);
            new QueryFavoriteTask().execute();

        default:
            break;
        }

    }

    private class QueryFavoriteTask extends AsyncTask<Void, Void, Void> {

        @Override
        public void onPreExecute() {
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                TbFavoriteHandler tbFavoriteHandler = new TbFavoriteHandler(FavoriteActivity.this);
                List<Favorite> list = tbFavoriteHandler.selectByType(favoriteType);
                mFavoriteList.clear();
                mFavoriteList.addAll(list);

            } catch (Exception e) {
                LogUtil.e(TAG, "query favorite error", e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            mAdapter.notifyDataSetChanged();

            if (mFavoriteList.size() > 0) {
                listBottom.setVisibility(View.VISIBLE);
                noFavorite.setVisibility(View.GONE);
            } else {
                listBottom.setVisibility(View.GONE);
                noFavorite.setVisibility(View.VISIBLE);
            }
        }
    }

}
