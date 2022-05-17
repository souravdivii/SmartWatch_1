package com.android.smartwatch_1.Sensor;

import android.os.Bundle;
import android.support.wearable.view.DotsPageIndicator;
import android.support.wearable.view.GridViewPager;
import android.support.wearable.view.WatchViewStub;

import androidx.appcompat.app.AppCompatActivity;

import com.android.smartwatch_1.R;

public class SensorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        WatchViewStub stub = findViewById(R.id.watch_view_stub);

        stub.setOnLayoutInflatedListener(stub1 -> {
            final GridViewPager pager = findViewById(R.id.pager);
            pager.setAdapter(new SensorFragmentPagerAdapter(getFragmentManager()));

            DotsPageIndicator indicator = findViewById(R.id.page_indicator);
            indicator.setPager(pager);
        });

    }
}