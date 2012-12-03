package me.chengdong.bustime.activity;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class StationBusActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.station_bus);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.station_bus, menu);
        return true;
    }
}
