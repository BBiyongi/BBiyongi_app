package com.kmualpha.bbiyongi_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.kmualpha.bbiyongi_app.notifications.ArrestActivity;
import com.kmualpha.bbiyongi_app.notifications.AttackActivity;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    public static String[] permissionList = new String[] {Manifest.permission.SEND_SMS};

    TextView btn_attack;
    TextView btn_arrest;
    TextView live_video;
    ImageView btn_setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 권한(문자메시지) 확인
        checkPermission();

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference();

        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.e("test", "test");
//                String value = snapshot.getValue(String.class);
//                Log.d("MainActivity", String.valueOf(value));
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String value = snapshot.getValue(String.class);
                Log.d("MainActivity", String.valueOf(value)); // test code
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {}

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("The read failed: " + error.getCode());
            }
        });

        FirebaseStorage storage = FirebaseStorage.getInstance("gs://alpha-92011.appspot.com/");
        Log.e("test", "storage");
        // 생성된 FirebaseStorage를 참조하는 storage 생성
        StorageReference storageRef = storage.getReference();
        Log.e("test", "storageRef");
        // Storage 내부의 images 폴더 안의 image.jpg 파일명을 가리키는 참조 생성
//        StorageReference pathReference = storageRef.child("avideo.mp4");
        Log.e("test", "pathReference");
        // 액티비티 화면 전환 -> 폭행 알림 목록
        btn_attack = findViewById(R.id.btn_attack);
        btn_attack.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), AttackActivity.class);
            startActivity(intent);
        });
        // 액티비티 화면 전환 -> 심정지 알림 목록
        btn_arrest = findViewById(R.id.btn_arrest);
        btn_arrest.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), ArrestActivity.class);
            startActivity(intent);
        });
        // 액티비티 화면 전환 -> 실시간 CCTV
        live_video = findViewById(R.id.live_video);
        live_video.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), WebViewActivity.class);
            startActivity(intent);
        });
        // 액티비티 화면 전환 -> 비상연락망 설정 팝업
        btn_setting = findViewById(R.id.setting);
        btn_setting.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), AddressActivity.class);
            startActivity(intent);
        });
    }




    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String permission : permissionList) {
                if (checkCallingOrSelfPermission(permission) == PackageManager.PERMISSION_DENIED)
                    requestPermissions(permissionList, 0);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}