package com.android.smartwatch_1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class AmbienceActivity extends AppCompatActivity {

    private TextView textViewOutput;
    private EditText editTextThreshold;
    private ConstraintLayout constraintLayoutMain;

    private float floatThreshold = 1;

    private SensorManager sensorManager;
    private Sensor sensorLight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ambience);

        textViewOutput = findViewById(R.id.textView);
        editTextThreshold = findViewById(R.id.editTextThreshold);
        constraintLayoutMain = findViewById(R.id.myLayout);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorLight = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        SensorEventListener sensorEventListenerLight = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                float floatSensorValue = event.values[0]; // lux

                if (floatSensorValue < floatThreshold){
                    textViewOutput.setText("It is Dark");
                    constraintLayoutMain.setBackgroundColor(Color.parseColor("#B5B5B5"));
                }
                else {
                    textViewOutput.setText("It is Bright");
                    constraintLayoutMain.setBackgroundColor(Color.parseColor("#FFFFFF"));
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        };
        sensorManager.registerListener(sensorEventListenerLight, sensorLight, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void buttonSetThreshold(View view){
        if (editTextThreshold.getText().toString().isEmpty()){
            return;
        }
        floatThreshold = Float.valueOf(editTextThreshold.getText().toString());
    }
}