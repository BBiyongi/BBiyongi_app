package com.kmualpha.bbiyongi_app.notifications;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kmualpha.bbiyongi_app.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ArrestAdapter extends BaseAdapter {

    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    ArrayList<Notification> notifications;

    public ArrestAdapter(Context context, ArrayList<Notification> notis) {
        mContext = context;
        notifications = notis;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return notifications.size();
    }

    @Override
    public Notification getItem(int i) {
        return notifications.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View converView, ViewGroup parent) {
        View view = mLayoutInflater.inflate(R.layout.notification_arrest, null);

        ImageView img_thumbnail = (ImageView)view.findViewById(R.id.img_thumbnail);
        TextView record_date = (TextView)view.findViewById(R.id.record_date);
        LinearLayout notification = (LinearLayout)view.findViewById(R.id.notification);
        TextView btn_save = (TextView)view.findViewById(R.id.btn_go_save);

        Date date = notifications.get(i).getDate();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd(E) HH:mm:ss");
        img_thumbnail.setImageResource(notifications.get(i).getImg_url());
        String date_str = simpleDateFormat.format(date);
        record_date.setText("녹화 일시\n" + date_str);

        boolean isChecked = notifications.get(i).getChecked();
        if (!isChecked) {
            notification.setBackgroundColor(Color.parseColor("#33FF5252"));
        }
        btn_save.setOnClickListener(v1 -> {
            Toast.makeText(mContext.getApplicationContext(), date_str, Toast.LENGTH_SHORT).show();
        });

        return view;
    }
}
