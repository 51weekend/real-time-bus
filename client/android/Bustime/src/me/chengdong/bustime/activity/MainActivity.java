package me.chengdong.bustime.activity;

import me.chengdong.bustime.R;
import me.chengdong.bustime.db.TbFavoriteHandler;
import me.chengdong.bustime.task.CheckVersionTask;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

@SuppressWarnings("deprecation")
public class MainActivity extends TabActivity {

    TbFavoriteHandler tbFavoriteHandler = new TbFavoriteHandler(MainActivity.this);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setTabs();

    }

    public void onResume() {
        super.onResume();
        new CheckVersionTask(MainActivity.this).execute();
        // new LoadDataTask(MainActivity.this).execute();
    }

    private void setTabs() {
        // boolean hasFavorite = tbFavoriteHandler.hasFavorite();
        // if (hasFavorite) {
        // addTab("收藏", R.drawable.tab_favorite, FavoriteActivity.class);
        // }
        addTab("车次查询", R.drawable.tab_line, LineActivity.class);
        addTab("站台查询", R.drawable.tab_station, StationActivity.class);
        addTab("收藏", R.drawable.tab_favorite, FavoriteActivity.class);
        // if (!hasFavorite) {
        // }
        addTab("更多", R.drawable.more, MoreActivity.class);
    }

    private void addTab(String labelId, int drawableId, Class<?> c) {
        TabHost tabHost = getTabHost();
        Intent intent = new Intent(this, c);

        TabHost.TabSpec spec = tabHost.newTabSpec("tab" + labelId);

        View tabIndicator = LayoutInflater.from(this).inflate(R.layout.tab_indicator, getTabWidget(), false);
        TextView title = (TextView) tabIndicator.findViewById(R.id.title);
        title.setText(labelId);
        ImageView icon = (ImageView) tabIndicator.findViewById(R.id.icon);
        icon.setImageResource(drawableId);

        spec.setIndicator(tabIndicator);
        spec.setContent(intent);
        tabHost.addTab(spec);
    }

}
