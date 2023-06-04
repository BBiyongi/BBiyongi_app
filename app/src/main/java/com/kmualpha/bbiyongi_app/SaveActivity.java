package com.kmualpha.bbiyongi_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.kmualpha.bbiyongi_app.notifications.Notification;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class SaveActivity extends AppCompatActivity {

    SharedPreferences preferences;
    Notification notification;
    VideoView video;
    TextView record_date;
    TextView btn_police;
    ImageView btn_back;
    TextView msg_box;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);

        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        // 알림 intent 받아와서 화면에 출력
        Intent intent = getIntent();
        notification = (Notification) intent.getSerializableExtra("notification");
        Log.d("push", notification.toString());

        video = findViewById(R.id.video);
        // 동영상 로드
        //Video Uri
        Uri videoUri= Uri.parse(notification.getLink());

        //비디오뷰의 재생, 일시정지 등을 할 수 있는 '컨트롤바'를 붙여주는 작업
        video.setMediaController(new MediaController(this));

        //VideoView가 보여줄 동영상의 경로 주소(Uri) 설정하기
        video.setVideoURI(videoUri);

        //동영상을 읽어오는데 시간이 걸리므로 비디오 로딩 준비가 끝났을 때 실행함
        video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                //비디오 시작
                video.start();
            }
        });

        record_date = findViewById(R.id.record_date);
        record_date.setText("녹화 일시 " + notification.getStringDate());

        // 심정지 알림일 때만 AED 위치 표시
        TextView aed = findViewById(R.id.aed);
        if (Objects.equals(notification.getType(), "arrest")) {
            String aedText = "가장 가까운 자동 제세동기는 " + notification.getAed() + "에 있습니다";
            SpannableString spannableString = new SpannableString(aedText);
            ForegroundColorSpan redColorSpan = new ForegroundColorSpan(Color.parseColor("#ff5252"));
            // 위치 텍스트에 대해 빨간색 스팬을 적용
            int startIndex = aedText.indexOf(notification.getAed());
            int endIndex = startIndex + notification.getAed().length();
            spannableString.setSpan(redColorSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            // TextView에 SpannableString 설정
            aed.setText(spannableString);
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
        String msg = message.replace(getString(R.string.msg_date), notification.getStringDate())
                .replace(getString(R.string.msg_link), notification.getLink())
                .replace(getString(R.string.msg_pos), notification.getPos())
                .replace(getString(R.string.msg_type), Objects.equals(notification.getType(), "arrest") ?"심정지가":"폭행이");
        msg_box.setText(msg);

        // 문자메시지로 신고하기
        btn_police = findViewById(R.id.btn_police);
        btn_police.setOnClickListener(v -> {
            String sms = msg_box.getText().toString();
            try{
                String address = preferences.getString("emergency", "+821085441920");
                SmsManager smsManager = SmsManager.getDefault();
                ArrayList<String> parts = smsManager.divideMessage(sms); // 메시지 분할
                smsManager.sendMultipartTextMessage(address, null, parts, null, null);
                Toast.makeText(getApplicationContext(), "전송을 완료하였습니다.", Toast.LENGTH_SHORT).show();
//                비상연락망에 112 또는 119가 포함되어있으면 112, 119로 문자메시지 신고
//                if (preferences.getBoolean("contain112", false)) {
//                    if (Objects.equals(notification.getType(), "attack")) {
//                        smsManager.sendMultipartTextMessage("112", null, parts, null, null);
//                    }
//                    else {
//                        smsManager.sendMultipartTextMessage("119", null, parts, null, null);
//                    }
//                }
            }
            catch (Exception e) {
                Toast.makeText(getApplicationContext(), "전송을 실패하였습니다. 다시 시도해 주세요", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 신고 메시지 저장 후 신고 화면으로 돌아오면 새로 저장된 메시지 form으로 setting
    @Override
    protected void onResume() {
        super.onResume();
        // 간편 신고 메시지 프리퍼런스 불러오기
        String message = preferences.getString("messageForm", getString(R.string.msg_default));
        String msg = message.replace(getString(R.string.msg_date), notification.getStringDate())
                .replace(getString(R.string.msg_link), notification.getLink())
                .replace(getString(R.string.msg_pos), notification.getPos())
                .replace(getString(R.string.msg_type), Objects.equals(notification.getType(), "arrest") ?"심정지가":"폭행이");
        msg_box.setText(msg);
    }
    @Override
    protected void onPause() {
        super.onPause();
        if(video!=null && video.isPlaying()) video.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(video!=null) video.stopPlayback();
    }
}
