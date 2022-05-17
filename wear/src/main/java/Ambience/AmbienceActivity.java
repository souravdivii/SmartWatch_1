package Ambience;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.wear.ambient.AmbientModeSupport;

import com.android.smartwatch_1.R;

public class AmbienceActivity extends AppCompatActivity implements
        AmbientModeSupport.AmbientCallbackProvider,
        MyCallback {

    AmbientModeSupport.AmbientController ambientController;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ambience);

        ambientController = AmbientModeSupport.attach(this);
        textView = findViewById(R.id.textView);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


    }

    @Override
    public AmbientModeSupport.AmbientCallback getAmbientCallback() {
        return new MyAmbientCallback(this, AmbienceActivity.this);
    }

    @Override
    public void updateMyText(String myString) {
        textView.setText("Checking...");
    }
}