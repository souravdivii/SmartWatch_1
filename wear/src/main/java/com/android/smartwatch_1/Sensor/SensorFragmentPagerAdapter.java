package com.android.smartwatch_1.Sensor;

import android.app.Fragment;
import android.app.FragmentManager;
import android.hardware.Sensor;
import android.support.wearable.view.FragmentGridPagerAdapter;

public class SensorFragmentPagerAdapter extends FragmentGridPagerAdapter {

    private int[] sensorTypes = {
            Sensor.TYPE_ACCELEROMETER,
            Sensor.TYPE_GYROSCOPE
    };


    public SensorFragmentPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public Fragment getFragment(int row, int column) {
        return SensorFragment.newInstance(sensorTypes[column]);
    }

    @Override
    public int getRowCount() {
        return 1; // fix to 1 row
    }

    @Override
    public int getColumnCount(int i) {
        return sensorTypes.length;
    }
}
