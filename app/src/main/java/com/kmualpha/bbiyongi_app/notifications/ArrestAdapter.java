package com.kmualpha.bbiyongi_app.notifications;

import android.content.Context;
import android.graphics.Color;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.kmualpha.bbiyongi_app.R;

import java.io.File;
import java.util.ArrayList;

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

        img_thumbnail.setImageResource(notifications.get(i).getImg_url());
        
        String date = notifications.get(i).getStringDate();
        record_date.setText("녹화 일시\n" + date);

        boolean isChecked = notifications.get(i).getChecked();
        if (!isChecked) {
            notification.setBackgroundColor(Color.parseColor("#33FF5252"));
        }
        btn_save.setOnClickListener(v1 -> {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            String videoPath = "gs://alpha-92011.appspot.com/" + notifications.get(i).getDate() + ".mp4";

            String localFilePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + notifications.get(i).getDate() + ".mp4";

            StorageReference storageRef = storage.getReferenceFromUrl(videoPath);

            File localFile = new File(localFilePath);

            storageRef.getFile(localFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Log.d("Download", "다운로드 완료");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("Download", "다운로드 실패: " + e.getMessage());
                        }
                    });

            Toast.makeText(mContext.getApplicationContext(), date, Toast.LENGTH_SHORT).show();
        });

        return view;
    }
}
