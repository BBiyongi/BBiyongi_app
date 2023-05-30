package com.kmualpha.bbiyongi_app;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.preference.PreferenceManager;

import com.kmualpha.bbiyongi_app.notifications.Notification;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class PopupActivity extends Activity {

    EditText edit_msg;
    TextView btn_set_return;
    TextView btn_get_pos;
    TextView btn_get_date;
    TextView btn_get_link;
    SharedPreferences preferences;
    Notification notification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_setting);

        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        // 알림 intent 받아오기
        Intent intent = getIntent();
        notification = (Notification) intent.getSerializableExtra("notification");

        // 간편 신고 메시지 form 불러오기
        edit_msg = findViewById(R.id.edit_msg);
        String messageForm = preferences.getString("messageForm", getString(R.string.msg_default));
        String msg = messageForm.replace(getString(R.string.msg_date), notification.getDate())
                .replace(getString(R.string.msg_link), notification.getLink())
                .replace(getString(R.string.msg_pos), notification.getPos())
                .replace(getString(R.string.msg_type), Objects.equals(notification.getType(), "arrest") ?"심정지가":"폭행이");
        edit_msg.setText(msg);

        // 신고 메시지 필수 요소 간편 입력
        // 1. 위치 입력
        btn_get_pos = findViewById(R.id.btn_get_pos);
        btn_get_pos.setOnClickListener(v-> {
            StringBuffer sb = new StringBuffer();
            int cursor = edit_msg.getSelectionStart();
            sb.append(edit_msg.getText().toString());
            sb.insert(cursor, notification.getPos());
            edit_msg.setText(sb.toString());
        });
        // 2. 날짜 입력
        btn_get_date = findViewById(R.id.btn_get_date);
        btn_get_date.setOnClickListener(v -> {
            StringBuffer sb = new StringBuffer();
            int cursor = edit_msg.getSelectionStart();
            sb.append(edit_msg.getText().toString());
            sb.insert(cursor, notification.getDate());
            edit_msg.setText(sb.toString());
        });
        // 3. 동영상 링크 입력
        btn_get_link = findViewById(R.id.btn_get_link);
        btn_get_link.setOnClickListener(v -> {
            StringBuffer sb = new StringBuffer();
            int cursor = edit_msg.getSelectionStart();
            sb.append(edit_msg.getText().toString());
            sb.insert(cursor, notification.getLink());
            edit_msg.setText(sb.toString());
        });
        // 설정 저장
        btn_set_return = findViewById(R.id.btn_set_return);
        btn_set_return.setOnClickListener(v -> {
            String sb = edit_msg.getText().toString();
            // 필수 요소를 모두 포함하였을 때 저장
            if (sb.contains(notification.getPos())
                    && sb.contains(notification.getDate())
                    && sb.contains(notification.getLink())) {
                setting(sb);
                finish();
            }
            else {
                Toast.makeText(getApplicationContext(), "위치, 날짜, 링크 정보는 반드시 포함되어야 합니다.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setting(String msg) {
        Toast.makeText(getApplicationContext(), "저장되었습니다.", Toast.LENGTH_SHORT).show();
        // 신고 메시지 형태 프리퍼런스 저장
        String messageForm = msg.replace(notification.getDate(), getString(R.string.msg_date))
                .replace(notification.getPos(), getString(R.string.msg_pos))
                .replace(notification.getLink(), getString(R.string.msg_link))
                .replace("심정지가", getString(R.string.msg_type))
                .replace("폭행이", getString(R.string.msg_type));
        SharedPreferences.Editor editor = preferences.edit(); // 완료 버튼을 누르면 변경사항 저장
        editor.putString("messageForm", messageForm); // messageForm 저장
        editor.apply();
    }
}