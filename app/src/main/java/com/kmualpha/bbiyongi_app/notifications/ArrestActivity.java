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
import java.util.Date;

public class ArrestActivity extends AppCompatActivity {

    ImageView btn_back_main;
    ArrayList<Notification> notificationArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arrests);

        // 뒤로가기
        btn_back_main = (ImageView) findViewById(R.id.btn_back_main);
        btn_back_main.setOnClickListener(v -> finish());

        // 알림 목록 불러오기
        try {
            this.InitializeData();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // 알림 목록 불러와서 화면에 표시
        ListView list_view = (ListView) findViewById(R.id.list_view);
        final ArrestAdapter myAdapter = new ArrestAdapter(this,notificationArrayList);

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
        notificationArrayList = new ArrayList<Notification>();

        // DB 불러오기
        String[] arr_date =new String[]{"2000-09-02 08:10:55", "2001-02-01 13:40:15", "2002-03-31 10:15:15"};
        Arrays.sort(arr_date);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (String i : arr_date) {
            Date date = simpleDateFormat.parse(i);
            if (Arrays.binarySearch(arr_date, i) == 0)
                notificationArrayList.add(new Notification("arrest", R.drawable.siren, date, "www.xxx", "CAM00", false));
            else
                notificationArrayList.add(new Notification("arrest", R.drawable.siren, date, "www.xxx", "CAM00", true));
        }
//        notificationArrayList.add(new Notification(R.drawable.siren, new Date(String.valueOf(format.parse("2019-09-02 08:10:55")))));
//        notificationArrayList.add(new Notification(R.drawable.siren, new Date(String.valueOf(format.parse("2001-02-01 13:40:15")))));
//        notificationArrayList.add(new Notification(R.drawable.siren, new Date(String.valueOf(format.parse("2002:03:31 10:15:15")))));
    }
}
