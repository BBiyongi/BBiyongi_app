package com.kmualpha.bbiyongi_app;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class AddressActivity extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

    }
}
