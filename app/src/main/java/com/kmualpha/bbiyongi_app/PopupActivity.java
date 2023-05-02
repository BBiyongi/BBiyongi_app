package com.kmualpha.bbiyongi_app;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kmualpha.bbiyongi_app.R;

public class PopupActivity extends Activity {

    EditText edit_msg;
    TextView btn_set_return;
    TextView btn_get_pos;
    TextView btn_get_date;
    TextView btn_get_link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_setting);

        edit_msg = findViewById(R.id.edit_msg);

        btn_set_return = findViewById(R.id.btn_set_return);

    }
}