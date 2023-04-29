package com.kmualpha.bbiyongi_app.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.kmualpha.bbiyongi_app.R;
import com.kmualpha.bbiyongi_app.SaveActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NotificationsActivity extends AppCompatActivity {

    ImageView btn_back_main;
    ArrayList<Notification> notificationArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        btn_back_main = (ImageView) findViewById(R.id.btn_back_main);
        btn_back_main.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        try {
            this.InitializeData();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        ListView list_view = (ListView) findViewById(R.id.list_view);
        final Adapter myAdapter = new Adapter(this,notificationArrayList);

        list_view.setAdapter(myAdapter);

        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id){
                Date date = myAdapter.getItem(position).getDate();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd(E) HH:mm:ss");
                String date_str = simpleDateFormat.format(date);
                Intent intent = new Intent(getApplicationContext(), SaveActivity.class);
                intent.putExtra("date", date_str);
                startActivity(intent);
            }
        });
    }

    public void InitializeData() throws ParseException {
        notificationArrayList = new ArrayList<Notification>();

        String[] arr_date =new String[]{"2000-09-02 08:10:55", "2001-02-01 13:40:15", "2002-03-31 10:15:15"};

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (String i : arr_date) {
            Date date = simpleDateFormat.parse(i);
            notificationArrayList.add(new Notification(R.drawable.siren, date));
        }
//        notificationArrayList.add(new Notification(R.drawable.siren, new Date(String.valueOf(format.parse("2019-09-02 08:10:55")))));
//        notificationArrayList.add(new Notification(R.drawable.siren, new Date(String.valueOf(format.parse("2001-02-01 13:40:15")))));
//        notificationArrayList.add(new Notification(R.drawable.siren, new Date(String.valueOf(format.parse("2002:03:31 10:15:15")))));
    }
}
