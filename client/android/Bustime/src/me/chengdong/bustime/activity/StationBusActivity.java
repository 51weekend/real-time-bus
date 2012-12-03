package me.chengdong.bustime.activity;

import me.chengdong.bustime.utils.ParamUtil;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;

public class StationBusActivity extends Activity {

    String stationCode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.station_bus);

        Intent intent = getIntent();
        stationCode = intent.getExtras().get(ParamUtil.STATION_CODE).toString();
        intent.getExtras().get(ParamUtil.STATION_NAME);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.station_bus, menu);
        return true;
    }
}
