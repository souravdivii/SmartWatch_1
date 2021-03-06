package com.android.smartwatch_1.Sensor;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.smartwatch_1.R;

import java.util.Date;


public class SensorFragment extends Fragment implements SensorEventListener {

    private static final float SHAKE_THRESHOLD = 1.1f;
    private static final int SHAKE_WAIT_TIME_MS = 250;
    private static final float ROTATION_THRESHOLD = 2.0f;
    private static final int ROTATION_WAIT_TIME_MS = 100;

    private View mView;
    private TextView mTextTitle;
    private TextView mTextValues;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private int mSensorType;
    private long mShakeTime = 0;
    private long mRotationTime = 0;

    float appliedAcceleration = 0;
    float currentAcceleration = 0;
    float velocity = 0;
    Date lastUpdatedate;
    double calibration = Double.NaN;

    public SensorFragment() {
        // Required empty public constructor
    }


    public static SensorFragment newInstance(int sensorType) {

        SensorFragment fragment = new SensorFragment();
        Bundle args = new Bundle();
        args.putInt("sensorType", sensorType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();

        if (getArguments() != null) {
            mSensorType = args.getInt("sensorType");
        }

        lastUpdatedate = new Date(System.currentTimeMillis());
        mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(mSensorType);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_sensor, container, false);

        mTextTitle = (TextView) mView.findViewById(R.id.text_title);
        mTextTitle.setText(mSensor.getStringType());
        mTextValues = (TextView) mView.findViewById(R.id.text_values);

        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // If sensor is unreliable, then just return
        if (event.accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE) {
            return;
        }

        // FIXME: 18-05-2022 
       /* mTextValues.setText(
                "x = " + Float.toString(event.values[0]) + "\n" +
                        "y = " + Float.toString(event.values[1]) + "\n" +
                        "z = " + Float.toString(event.values[2]) + "\n"
        );*/

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            detectShake(event);
        } else if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            detectRotation(event);
        } else if (event.sensor.getType() == Sensor.TYPE_PRESSURE) {
            detectPressure(event);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void detectShake(SensorEvent event) {

        double x = event.values[0];
        double y = event.values[1];
        double z = event.values[2];
        double a = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2));
        if (Double.isNaN(calibration))
            calibration = a;
        else {
            Date timeNow = new Date(System.currentTimeMillis());
            long timeDelta = timeNow.getTime()-lastUpdatedate.getTime();
            lastUpdatedate.setTime(timeNow.getTime());
            float deltaVelocity = appliedAcceleration * (timeDelta/1000);
            appliedAcceleration = currentAcceleration;
            velocity += deltaVelocity;
            final double mph = (Math.round(100*velocity / 1.6 * 3.6))/100;
            Log.i("SensorTestActivity","SPEEDDDDD=== "+mph+"     "+velocity);
            currentAcceleration = (float)a;
            mTextValues.setText(currentAcceleration + "");
        }

        /* long now = System.currentTimeMillis();

        if ((now - mShakeTime) > SHAKE_WAIT_TIME_MS) {
            mShakeTime = now;

            float gX = event.values[0] / SensorManager.GRAVITY_EARTH;
            float gY = event.values[1] / SensorManager.GRAVITY_EARTH;
            float gZ = event.values[2] / SensorManager.GRAVITY_EARTH;

            // gForce will be close to 1 when there is no movement
            float gForce = (float) Math.sqrt(gX * gX + gY * gY + gZ * gZ);


            // Change background color if gForce exceeds threshold;
            // otherwise, reset the color
            if (gForce > SHAKE_THRESHOLD) {
                mView.setBackgroundColor(Color.rgb(0, 100, 0));

                double a = Math.sqrt(Math.pow(gX, 2) + Math.pow(gY, 2) + Math.pow(gZ, 2));
                if (Double.isNaN(calibration))
                    calibration = a;
                else {
                    Date timeNow = new Date(System.currentTimeMillis());
                    long timeDelta = timeNow.getTime() - lastUpdatedate.getTime();
                    lastUpdatedate.setTime(timeNow.getTime());
                    float deltaVelocity = appliedAcceleration * (timeDelta / 1000);
                    appliedAcceleration = currentAcceleration;
                    velocity += deltaVelocity;
                    final double mph = (Math.round(100 * velocity / 1.6 * 3.6)) / 100;
                    Log.i("SensorTestActivity", "SPEEDDDDD=== " + mph + "     " + velocity);
                    currentAcceleration = (float) a;
                    mTextValues.setText(currentAcceleration + "");
                }
            } else {
                mView.setBackgroundColor(Color.BLACK);
            }
        }*/
    }

    private void detectRotation(SensorEvent event) {
        long now = System.currentTimeMillis();

        if ((now - mRotationTime) > ROTATION_WAIT_TIME_MS) {
            mRotationTime = now;

            // Change background color if rate of rotation around any
            // axis and in any direction exceeds threshold;
            // otherwise, reset the color
            if (Math.abs(event.values[0]) > ROTATION_THRESHOLD ||
                    Math.abs(event.values[1]) > ROTATION_THRESHOLD ||
                    Math.abs(event.values[2]) > ROTATION_THRESHOLD) {
                mView.setBackgroundColor(Color.rgb(0, 100, 0));
            } else {
                mView.setBackgroundColor(Color.BLACK);
            }
        }
    }

    private void detectPressure(SensorEvent event) {

    }
}