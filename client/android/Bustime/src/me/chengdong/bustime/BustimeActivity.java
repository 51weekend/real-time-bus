package me.chengdong.bustime;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class BustimeActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bustime);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_bustime, menu);
        return true;
    }
}
