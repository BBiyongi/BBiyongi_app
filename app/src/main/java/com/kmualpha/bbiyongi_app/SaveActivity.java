package com.kmualpha.bbiyongi_app;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

public class SaveActivity extends AppCompatActivity {

    VideoView video;
    TextView record_date2;
    TextView btn_police;
    ImageView btn_back_notifications;
    TextView msg_box;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);

        Intent intent = getIntent();
        String date = intent.getStringExtra("date");
        record_date2 = findViewById(R.id.record_date2);
        record_date2.setText("녹화 일시 " + date);

        btn_back_notifications = findViewById(R.id.btn_back_notifications);
        btn_back_notifications.setOnClickListener(v -> finish());

        msg_box = findViewById(R.id.msg_box);
        msg_box.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), PopupActivity.class);
            startActivity(i);
        });

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
