package com.kmualpha.bbiyongi_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import androidx.preference.PreferenceManager;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kmualpha.bbiyongi_app.notifications.ArrestActivity;
import com.kmualpha.bbiyongi_app.notifications.AttackActivity;
import com.kmualpha.bbiyongi_app.notifications.Notification;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    public static String[] permissionList = new String[] {Manifest.permission.SEND_SMS};

    TextView btn_attack;
    TextView btn_arrest;
    TextView live_video;
    ImageView btn_setting;
    ArrayList<Notification> attackList = new ArrayList<>(); // 프리퍼런스에서 불러올 폭행 알림 목록
    ArrayList<Notification> arrestList = new ArrayList<>(); // 프리퍼런스에서 불러올 심정지 알림 목록


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 권한(문자메시지) 확인
        checkPermission();

        // 앱을 실행할 때마다 프리퍼런스 불러오기
        getNotifications();

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference();

        ref.addChildEventListener(new ChildEventListener() {
            // 새로운 자식 노드가 추가되었을 때 호출: 데이터베이스에 새로운 자식이 추가되면 해당 자식 노드의 데이터 스냅샷과 이전 자식의 이름이 전달된다
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.e("test", "test");
//                String value = snapshot.getValue(String.class);
//                Log.d("MainActivity", String.valueOf(value));
            }

            // 자식 노드의 데이터가 변경되었을 때 호출: 변경된 자식 노드의 데이터 스냅샷과 이전 자식의 이름이 전달된다
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String value = snapshot.getValue(String.class); // 변경된 값
                Log.d("MainActivity", String.valueOf(value)); // test code
            }

            // 자식 노드가 삭제되었을 때 호출: 삭제된 자식 노드의 데이터 스냅샷이 전달된다
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {}

            // 자식 노드가 이동되었을 때 호출: 이동된 자식 노드의 데이터 스냅샷과 이전 자식의 이름이 전달된다
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

            // 데이터베이스 읽기 작업이 취소되었을 때 호출: 예외가 발생한 경우에 호출되며, 에러 정보를 전달한다
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
//        try{
//            File path = new File("/data/data/com.kmualpha.bbiyongi_app/test/"); //로컬에 저장할 폴더의 위치
//            final File file = new File(path, "test_video"); //저장하는 파일의 이름
//            try {
//                if (!path.exists()) {
//                    path.mkdirs(); // 저장할 폴더가 없으면 생성
//                }
//                file.createNewFile();
//                //파일을 다운로드하는 Task 생성, 비동기식으로 진행
//                final FileDownloadTask fileDownloadTask = pathReference.getFile(file);
//                fileDownloadTask.addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                        //다운로드 성공 후 할 일
//                        Log.e("test", "download");
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception exception) {
//                        //다운로드 실패 후 할 일
//                        Log.e("test", "fail");
//                        Toast.makeText(getApplicationContext(), "다운로드 실패", Toast.LENGTH_SHORT).show();
//                    }
//                }).addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
//                    @Override
//                    public void onProgress(FileDownloadTask.TaskSnapshot taskSnapshot) { //진행상태 표시
//                    }
//                });
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        } catch(Exception e){
//            e.printStackTrace();
//        }

        // 액티비티 화면 전환 -> 폭행 알림 목록
        btn_attack = findViewById(R.id.btn_attack);
        btn_attack.setOnClickListener(v -> {
            Intent intent = new Intent(this, AttackActivity.class);
            // attack notifications 목록 intent 넘겨주기
            intent.putExtra("attackList", attackList);
            startActivity(intent);
        });
        // 액티비티 화면 전환 -> 심정지 알림 목록
        btn_arrest = findViewById(R.id.btn_arrest);
        btn_arrest.setOnClickListener(v -> {
            Intent intent = new Intent(this, ArrestActivity.class);
            // arrest notifications 목록 intent 넘겨주기
            intent.putExtra("arrestList", arrestList);
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

    private void getNotifications() {
        // 1. attackList 불러오기
        // "{type}List" 키로 저장된 JSON 형태의 문자열을 불러온 후, Gson을 사용하여 역직렬화하여 ArrayList로 변환
        Gson gson = new Gson();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (preferences.contains("attackList")) { // 프리퍼런스에 저장되어 있는 폭행 목록 불러오기
            String json = preferences.getString("attackList", "");
            attackList = gson.fromJson(json, new TypeToken<ArrayList<Notification>>() {
            }.getType());
        } else { // 프리퍼런스에 저장되어 있는 notification이 하나도 없을 때
            Notification attnoti1 = new Notification("attack", R.drawable.siren, Date.valueOf("2010-10-10"), "성북구 정릉동", "www.xxx", "CAM01", false);
            Notification attnoti2 = new Notification("attack", R.drawable.siren, Date.valueOf("2001-12-30"), "강북구 미아동", "www.xxx", "CAM01", false);
            Notification attnoti3 = new Notification("attack", R.drawable.siren, Date.valueOf("2023-05-11"), "관악구 신림동", "www.xxx", "CAM01", true);
            attackList.add(attnoti1);
            attackList.add(attnoti2);
            attackList.add(attnoti3);
        }
        // 2. arrestList 불러오기
        if (preferences.contains("arrestList")) { // 프리퍼런스에 저장되어 있는 심정지 목록 불러오기
            String json = preferences.getString("arrestList", "");
            arrestList = gson.fromJson(json, new TypeToken<ArrayList<Notification>>() {
            }.getType());
        } else {
            Notification arrnoti1 = new Notification("arrest", R.drawable.siren, Date.valueOf("2000-09-02"), "검단구 아라동", "www.xxx", "CAM01", false);
            Notification arrnoti2 = new Notification("arrest", R.drawable.siren, Date.valueOf("2005-02-01"), "마포구 동교동", "www.xxx", "CAM01", true);
            Notification arrnoti3 = new Notification("arrest", R.drawable.siren, Date.valueOf("2003-05-25"), "서대문구 연희동", "www.xxx", "CAM01", true);
            arrestList.add(arrnoti1);
            arrestList.add(arrnoti2);
            arrestList.add(arrnoti3);
        }
    }
}