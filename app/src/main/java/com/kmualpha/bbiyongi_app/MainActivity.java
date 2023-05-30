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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.common.util.MapUtils;
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
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    /*
    전역변수 선언 1. 필요한 권한
               2. 프리퍼런스에서 불러올 폭행 및 심정지 알림 목록
                  각 알림의 날짜를 따로 ArrayList로 관리하여 데이터 중복을 방지함
     */
    public static String[] permissionList = new String[] {Manifest.permission.SEND_SMS};

    ArrayList<Notification> attackList = new ArrayList<>();
    ArrayList<Notification> arrestList = new ArrayList<>();
    ArrayList<String> attackDate = new ArrayList<>();
    ArrayList<String> arrestDate = new ArrayList<>();


    // firebase에 저장된 컬렉션 map
    HashMap<String,String> temp_map = new HashMap<String,String>();  // key & value 임시 저장용 map

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        앱 실행 시 1. 필요한 권한을 확인함
                 2. 내장 메모리에 프리퍼런스로 저장된 알림 목록을 불러옴
         */
        checkPermission();

        getNotifications();

        // firebase 연결
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mDatabase = database.getReference();

        mDatabase.addChildEventListener(new ChildEventListener() {
            // 새로운 자식 노드가 추가되었을 때 호출: 데이터베이스에 새로운 자식이 추가되면 해당 자식 노드의 데이터 스냅샷과 이전 자식의 이름이 전달된다
            // firebase 데이터 받기
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.e("test", "test");
                Log.d("test", "current top Key: " + snapshot.getKey());  // 테스트용 - 최상위 key(발생 시기) 조회

                // 하위 key & value 가져오기
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    String childKey = childSnapshot.getKey();
                    String childValue = childSnapshot.getValue(String.class);
                    temp_map.put(childKey, childValue);

                    // detect가 assault(1)이나 cardiac arrest(2)가 아닌 경우
                    if ((childKey.equals("detect")) && (!(childValue.equals("1")) && !(childValue.equals("2")))) {
                        temp_map.clear();  // 초기화
                        break;
                    }
                }

                if (!temp_map.isEmpty()) {
                    // detect가 assault(1)일 경우
                    if (temp_map.get("detect").equals("1")) {
                        // preference로 map 넘겨주기
                        Notification data = new Notification("attack", R.drawable.siren, temp_map.get("time"), temp_map.get("address"), "www.helloworld", "cam_id", false);
                        if (!attackDate.contains(temp_map.get("time"))) {
                            attackList.add(data);
                        }
                        Log.d("test", "assault_list: " + attackList);
                        temp_map.clear();  // 초기화
                    }
                    // detect가 cardiac arrest(2)일 경우
                    else if (temp_map.get("detect").equals("2")) {
                        // preference로 map 넘겨주기
                        Notification data = new Notification("arrest", R.drawable.siren, temp_map.get("time"), temp_map.get("address"), "www.hi", "cam_id", false);
                        // 불러온 알림이 이미 프리퍼런스에 저장되어 있는 알림이면 list에 추가하지 않는다
                        if (!arrestDate.contains(temp_map.get("time"))) {
                            arrestList.add(data);
                        }
                        Log.d("test", "assault_list: " + arrestList);
                        temp_map.clear();  // 초기화
                    }
                }

                // ArrayList를 JSON 형태로 변환하여 저장
                Gson gson = new Gson();
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = preferences.edit();
                String attackJson = gson.toJson(attackList);
                editor.putString("attackList", attackJson);
                String arrestJson = gson.toJson(arrestList);
                editor.putString("arrestList", arrestJson);
                editor.apply();
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
        // StorageReference pathReference = storageRef.child("avideo.mp4");
        Log.e("test", "pathReference");

        /*
        액티비티 화면 전환
        '폭행감지' 버튼 클릭 시 폭행 알림 목록 화면으로 이동
        폭행 알림 ArrayList를 intent로 넘겨줌
         */
        TextView btn_attack = findViewById(R.id.btn_attack);
        btn_attack.setOnClickListener(v -> {
            Intent intent = new Intent(this, AttackActivity.class);
            intent.putExtra("attackList", attackList);
            startActivity(intent);
        });
        /*
        액티비티 화면 전환
        '심정지' 버튼 클릭 시 심정지 알림 목록 화면으로 이동
        심정지 알림 ArrayList를 intent로 넘겨줌
         */
        TextView btn_arrest = findViewById(R.id.btn_arrest);
        btn_arrest.setOnClickListener(v -> {
            Intent intent = new Intent(this, ArrestActivity.class);
            intent.putExtra("arrestList", arrestList);
            startActivity(intent);
        });
        /*
        액티비티 화면 전환
        '실시간 동영상 링크' 버튼 클릭 시 실시간 CCTV 화면으로 이동
         */
        TextView live_video = findViewById(R.id.live_video);
        live_video.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), WebViewActivity.class);
            startActivity(intent);
        });
        /*
        액티비티 화면 전환
        설정 버튼 클릭 시 비상연락망 설정 화면으로 이동
         */
        ImageView btn_setting = findViewById(R.id.setting);
        btn_setting.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), AddressActivity.class);
            startActivity(intent);
        });
    }

    /*
    알림 목록 액티비티에서 알림 확인 후 finish()를 통해 다시 메인 액티비티로 돌아온다면
    전역변수 ArrayList 내 각 알림의 확인 여부(checked)를 갱신함
     */
    @Override
    protected void onResume() {
        super.onResume();
        getNotifications();
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

    /*
    프리퍼런스에 저장되어 있는 알림 type(폭행 또는 심정지)에 따라 나누어 알림 목록을 불러옴
    1. 프리퍼런스에 저장되어 있는 폭행 목록을 불러옴
    2. "{type}List" 키로 저장된 JSON 형태의 문자열을 불러온 후, Gson을 사용하여 역직렬화하여 ArrayList로 변환
    3. 각 알림의 날짜(연도, 월, 일, 시, 분, 초)를 date ArrayList에 별도로 관리하여 데이터 중복을 방지함
     */
    private void getNotifications() {
        Gson gson = new Gson();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (preferences.contains("attackList")) {
            String json = preferences.getString("attackList", "");
            attackList = gson.fromJson(json, new TypeToken<ArrayList<Notification>>() {
            }.getType());
            Log.e("array preference", "프리퍼런스에서 불러온 폭행 목록"+attackList.toString());
            for (Notification notification : attackList) {
                String date = notification.getDate();
                attackDate.add(date);
            }
        }
        if (preferences.contains("arrestList")) {
            String json = preferences.getString("arrestList", "");
            arrestList = gson.fromJson(json, new TypeToken<ArrayList<Notification>>() {
            }.getType());
            Log.e("array preference", "프리퍼런스에서 불러온 심정지 목록"+arrestList.toString());
            for (Notification notification : arrestList) {
                String date = notification.getDate();
                arrestDate.add(date);
            }
        }
    }
}