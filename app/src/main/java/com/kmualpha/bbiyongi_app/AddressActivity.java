package com.kmualpha.bbiyongi_app;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

public class AddressActivity extends Activity {

    EditText edit_address;
    TextView btn_set_return;
    Switch toggle112;

    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        edit_address = findViewById(R.id.edit_address);

        toggle112 = findViewById(R.id.toggle112);

        btn_set_return = findViewById(R.id.btn_set_return);
        btn_set_return.setOnClickListener(v -> {
            // 비상연락망 저장 //
            /////////////////
            finish();
        });
    }
}
