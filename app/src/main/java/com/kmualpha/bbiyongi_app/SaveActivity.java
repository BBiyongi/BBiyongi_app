package com.kmualpha.bbiyongi_app;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class SaveActivity extends AppCompatActivity {

    VideoView video;
    TextView record_date;
    TextView live_video;
    TextView btn_police;
    ImageView btn_back_notifications;
    TextView msg_box;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);

        Intent intent = getIntent();
        String date = intent.getStringExtra("date");
        String type = intent.getStringExtra("type");
        record_date = findViewById(R.id.record_date);
        record_date.setText("녹화 일시 " + date);
        if (Objects.equals(type, "arrest")) {
            live_video = findViewById(R.id.live_video);
            live_video.setText("실시간 동영상");
            live_video.setOnClickListener(v -> {

            });
        }

        // 뒤로가기
        btn_back_notifications = findViewById(R.id.btn_back_notifications);
        btn_back_notifications.setOnClickListener(v -> finish());

        // 액티비티 화면 전환 -> 신고 메시지 형태 설정 팝업
        msg_box = findViewById(R.id.msg_box);
        msg_box.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), PopupActivity.class);
            startActivity(i);
        });

        // 문자메시지로 신고하기
        btn_police = findViewById(R.id.btn_police);
        btn_police.setOnClickListener(v -> {
            String sms = msg_box.getText().toString();
            try{
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage("+821085441920", null, sms, null, null);
                Toast.makeText(getApplicationContext(), "전송을 완료하였습니다.", Toast.LENGTH_SHORT).show();
            }
            catch (Exception e) {
                Toast.makeText(getApplicationContext(), "전송을 실패하였습니다. 다시 시도해 주세요", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
