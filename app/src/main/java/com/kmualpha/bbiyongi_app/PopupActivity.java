package com.kmualpha.bbiyongi_app;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

        btn_get_pos = findViewById(R.id.btn_get_pos);
        btn_get_pos.setOnClickListener(v-> {
            StringBuffer msg = new StringBuffer();
            int cursor = edit_msg.getSelectionStart();
            msg.append(edit_msg.getText().toString());
            msg.insert(cursor, getResources().getString(R.string.msg_pos));
            edit_msg.setText(msg.toString());
        });
        btn_get_date = findViewById(R.id.btn_get_date);
        btn_get_date.setOnClickListener(v -> {
            StringBuffer msg = new StringBuffer();
            int cursor = edit_msg.getSelectionStart();
            msg.append(edit_msg.getText().toString());
            msg.insert(cursor, getResources().getString(R.string.msg_date));
            edit_msg.setText(msg.toString());
        });
        btn_get_link = findViewById(R.id.btn_get_link);
        btn_get_link.setOnClickListener(v -> {
            StringBuffer msg = new StringBuffer();
            int cursor = edit_msg.getSelectionStart();
            msg.append(edit_msg.getText().toString());
            msg.insert(cursor, getResources().getString(R.string.msg_link));
            edit_msg.setText(msg.toString());
        });
        btn_set_return = findViewById(R.id.btn_set_return);
        btn_set_return.setOnClickListener(v -> {
            String msg = edit_msg.getText().toString();
            if (msg.contains(getResources().getString(R.string.msg_pos))
                && msg.contains(getResources().getString(R.string.msg_date))
                && msg.contains(getResources().getString(R.string.msg_link))) {
                Toast.makeText(getApplicationContext(), "저장되었습니다.", Toast.LENGTH_SHORT).show();
                finish();
            }
            else {
                Toast.makeText(getApplicationContext(), "위치, 날짜, 링크 정보는 반드시 포함되어야 합니다.", Toast.LENGTH_SHORT).show();
            }
            // 간편신고 메시지 형태 저장 //
            /////////////////////////
        });

    }
}