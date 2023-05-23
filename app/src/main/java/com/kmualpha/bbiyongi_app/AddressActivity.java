package com.kmualpha.bbiyongi_app;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;

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

        // 설정 저장 -> 팝업 액티비티 종료
        btn_set_return = findViewById(R.id.btn_set_return);
        btn_set_return.setOnClickListener(v -> {
            if ((edit_address.getText().length() > 0)) {
                // 입력한 전화번호가 있는데 비정상적이라면
                if (!Pattern.matches("^01(?:0|1[6-9])(?:\\d{3}|\\d{4})\\d{4}$", edit_address.getText())) {
                    Toast.makeText(getApplicationContext(), "올바른 전화번호가 아닙니다", Toast.LENGTH_SHORT).show();
                }
                // 입력한 전화번호가 있는데 정상적이라면
                else {
                    // 비상연락망 저장 //
                    /////////////////
                }
            }
            // 입력한 전화번호가 없다/
            // toggle switch 저장 //
            ///////////////////////
            finish();
        });
    }
}
