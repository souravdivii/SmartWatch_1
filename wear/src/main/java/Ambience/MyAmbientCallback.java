package Ambience;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.wear.ambient.AmbientModeSupport;

public class MyAmbientCallback extends AmbientModeSupport.AmbientCallback{

    MyCallback myCallback = null;
    private Context context;

    public MyAmbientCallback(Context context, MyCallback myCallback) {
        this.context = context;
        this.myCallback = myCallback;
    }


    @Override
    public void onEnterAmbient(Bundle ambientDetails) {
        // Handle entering ambient mode
        myCallback.updateMyText("Enter Ambient");
        /*stateTextView.getPaint().setAntiAlias(true);*/
    }

    @Override
    public void onExitAmbient() {
        // Handle exiting ambient mode
        myCallback.updateMyText("Exit Ambient");
    }

    @Override
    public void onUpdateAmbient() {
        // Update the content
    }


}
