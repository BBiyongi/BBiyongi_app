package com.kmualpha.bbiyongi_app.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.kmualpha.bbiyongi_app.R;
import com.kmualpha.bbiyongi_app.SaveActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class AttackActivity extends AppCompatActivity {

    ImageView btn_back;
    ArrayList<Notification> notificationArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attacks);

        // 액티비티 화면 전환 -> 뒤로가기
        btn_back = (ImageView) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(v -> finish());

        // 알림 목록 불러오기
        try {
            this.InitializeData();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // 알림 목록 불러와서 화면에 표시
        ListView list_view = (ListView) findViewById(R.id.list_view);
        final AttackAdapter myAdapter = new AttackAdapter(this,notificationArrayList);

        list_view.setAdapter(myAdapter);

        // 각 알림 intent 전달하여 Save Activity 실행
        list_view.setOnItemClickListener((parent, v, position, id) -> {
            Date date = myAdapter.getItem(position).getDate();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd(E) HH:mm:ss");
            String date_str = simpleDateFormat.format(date);
            String type = myAdapter.getItem(position).getType();
            Intent intent = new Intent(getApplicationContext(), SaveActivity.class);
            intent.putExtra("date", date_str);
            intent.putExtra("type", type);
            startActivity(intent);
        });
    }

    public void InitializeData() throws ParseException {
        // 알림 목록 intent 받아와서 불러오기
        Intent intent = getIntent();
        notificationArrayList = (ArrayList<Notification>) intent.getSerializableExtra("attackList");

        // date 필드 기준으로 정렬
        Collections.sort(notificationArrayList, new Comparator<Notification>() {
            @Override
            public int compare(Notification n1, Notification n2) {
                Date date1 = n1.getDate();
                Date date2 = n2.getDate();
                return date1.compareTo(date2);
            }
        });
    }
}
