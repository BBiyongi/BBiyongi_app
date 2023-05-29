package com.kmualpha.bbiyongi_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.kmualpha.bbiyongi_app.notifications.Notification;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class SaveActivity extends AppCompatActivity {

    VideoView video;
    TextView record_date;
    TextView btn_police;
    ImageView btn_back;
    TextView msg_box;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        // 알림 intent 받아와서 화면에 출력
        Intent intent = getIntent();
        Notification notification = (Notification) intent.getSerializableExtra("notification");
        Date date = notification.getDate();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분 ss초");
        String dateString = dateFormat.format(date);
        String type = notification.getType();
        record_date = findViewById(R.id.record_date);
        record_date.setText("녹화 일시 " + dateString);

        // 심정지 알림일 때만 AED 위치 표시
        TextView aed = findViewById(R.id.aed);
        if (Objects.equals(type, "arrest")) {
            aed.setText("가장 가까운 자동 제세동기는 " + notification.getPos() + "에 있습니다");
        } else {
            aed.setVisibility(View.GONE);
        }

        // 액티비티 화면 전환 -> 뒤로가기
        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(v -> finish());

        // 액티비티 화면 전환 -> 신고 메시지 형태 설정 팝업
        msg_box = findViewById(R.id.msg_box);
        msg_box.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), PopupActivity.class);
            i.putExtra("notification", notification);
            startActivity(i);
        });

        // 간편 신고 메시지 프리퍼런스 불러오기
        String message = preferences.getString("messageForm", getString(R.string.msg_default));
        String msg = message.replace(getString(R.string.msg_date), dateString)
                .replace(getString(R.string.msg_link), notification.getLink())
                .replace(getString(R.string.msg_pos), notification.getPos())
                .replace(getString(R.string.msg_type), Objects.equals(type, "arrest") ?"심정지가":"폭행이");
        msg_box.setText(msg);

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
