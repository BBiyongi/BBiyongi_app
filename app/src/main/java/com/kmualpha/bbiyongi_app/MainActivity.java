package com.kmualpha.bbiyongi_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kmualpha.bbiyongi_app.notifications.ArrestActivity;
import com.kmualpha.bbiyongi_app.notifications.AttackActivity;
import com.kmualpha.bbiyongi_app.notifications.Notification;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    /*
    전역변수 선언 1. 필요한 권한
               2. 프리퍼런스에서 불러올 폭행 및 심정지 알림 목록
                  각 알림의 날짜를 따로 ArrayList로 관리하여 데이터 중복을 방지함
     */
    public static String[] permissionList = new String[] {Manifest.permission.SEND_SMS, Manifest.permission.WRITE_EXTERNAL_STORAGE};

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
         * 앱 실행 시 필요한 권한을 확인함
         */
        checkPermission();

        /*
         * 앱 실행 시 내장 메모리에 프리퍼런스로 저장된 알림 목록을 불러옴
         */
        Gson gson = new Gson();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();

        if (preferences.contains("attackList")) {
            String json = preferences.getString("attackList", "");
            attackList = gson.fromJson(json, new TypeToken<ArrayList<Notification>>() {
            }.getType());
            for (Notification notification : attackList) {
                attackDate.add(notification.getDate());
            }
        }
        if (preferences.contains("arrestList")) {
            String json = preferences.getString("arrestList", "");
            arrestList = gson.fromJson(json, new TypeToken<ArrayList<Notification>>() {
            }.getType());
            for (Notification notification : arrestList) {
                arrestDate.add(notification.getDate());
            }
        }
        Log.e("Preference", attackList.toString());
        Log.e("Preference", arrestList.toString());


        /*
         * firebase 연결하여 프리퍼런스에 없는 데이터만 추가하기
         */
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mDatabase = database.getReference();

        mDatabase.addChildEventListener(new ChildEventListener() {
            // 새로운 자식 노드가 추가되었을 때 호출: 데이터베이스에 새로운 자식이 추가되면 해당 자식 노드의 데이터 스냅샷과 이전 자식의 이름이 전달된다
            // firebase 데이터 받기
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.d("test child add", "current top Key: " + snapshot.getKey());  // 테스트용 - 최상위 key(발생 시기) 조회

                // 하위 key & value 가져오기
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    String childKey = childSnapshot.getKey();
                    String childValue = childSnapshot.getValue(String.class);
                    temp_map.put(childKey.trim(), childValue);

                    // detect가 assault(1)이나 cardiac arrest(2)가 아닌 경우
                    if ((childKey.equals("detect")) && (!(childValue.equals("1")) && !(childValue.equals("2")))) {
                        temp_map.clear();  // 초기화
                        break;
                    }
                }

                if (!temp_map.isEmpty()) {
                    Log.e("test", temp_map.toString());
                    // detect가 assault(1)일 경우
                    if (Objects.equals(temp_map.get("detect"), "1")) {
                        // preference에 없는 알림 DB에서 가져오기
                        Log.e("폭행", temp_map.get("detect"));
                        Notification data = new Notification("attack", R.drawable.siren, temp_map.get("time"), temp_map.get("address"), temp_map.get("fileUrl"), "cam_id", false, "");
                        if (!attackDate.contains(temp_map.get("time")))  {
                            Log.e("arraylist에 저장한다", data.getDate());
                            attackList.add(data);
                            attackDate.add(temp_map.get("time"));
                            String attackJson = gson.toJson(attackList);
                            editor.putString("attackList", attackJson);
                            editor.apply();
                            makePush(data);
                        }
                        temp_map.clear();  // 초기화
                    }
                    // detect가 cardiac arrest(2)일 경우
                    else if (Objects.equals(temp_map.get("detect"), "2")) {
                        // preference에 없는 알림 DB에서 가져오기
                        Log.e("심정지", temp_map.get("detect"));
                        Notification data = new Notification("arrest", R.drawable.siren, temp_map.get("time"), temp_map.get("address"), temp_map.get("fileUrl"), "cam_id", false, temp_map.get("AED"));
                        if (!arrestDate.contains(temp_map.get("time")) && !isDateThirtyDaysAgo(temp_map.get("time"))) {
                            Log.e("arraylist에 저장한다", data.getDate());
                            arrestList.add(data);
                            arrestDate.add(temp_map.get("time"));
                            String arrestJson = gson.toJson(arrestList);
                            editor.putString("arrestList", arrestJson);
                            editor.apply();
                            makePush(data);
                        }
                        temp_map.clear();  // 초기화
                    }
                }
            }

            // 자식 노드의 데이터가 변경되었을 때 호출: 변경된 자식 노드의 데이터 스냅샷과 이전 자식의 이름이 전달된다
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.d("test child change", "current top Key: " + snapshot.getKey());  // 테스트용 - 최상위 key(발생 시기) 조회
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

        /*
        액티비티 화면 전환
        '폭행감지' 버튼 클릭 시 폭행 알림 목록 화면으로 이동
        폭행 알림 ArrayList를 intent로 넘겨줌
         */
        TextView btn_attack = findViewById(R.id.btn_attack);
        btn_attack.setOnClickListener(v -> {
            Intent intent = new Intent(this, AttackActivity.class);
            String json = preferences.getString("attackList", "");
            attackList = gson.fromJson(json, new TypeToken<ArrayList<Notification>>() {
            }.getType());
            intent.putExtra("attackList", attackList);
            Log.e("attack activity", attackList.toString());
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
            String json = preferences.getString("arrestList", "");
            arrestList = gson.fromJson(json, new TypeToken<ArrayList<Notification>>() {
            }.getType());
            Log.e("arrest activity", arrestList.toString());
            intent.putExtra("arrestList", arrestList);
            startActivity(intent);
        });
        /*
        액티비티 화면 전환
        '실시간 동영상 링크' 버튼 클릭 시 실시간 CCTV 화면으로 이동
         */
        TextView live_video = findViewById(R.id.live_video);
        live_video.setVisibility(View.GONE);
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
    현재로부터 30일 지난 날짜의 데이터는 true 반환
     */
    public static boolean isDateThirtyDaysAgo(String dateString) {
        Calendar currentCalendar = Calendar.getInstance(); // 현재 날짜 가져오기
        Date currentDate = currentCalendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd_HHmm");
        Date inputDate;
        try {
            inputDate = sdf.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }

        // 입력된 날짜의 30일 후 계산
        Calendar inputCalendar = Calendar.getInstance();
        inputCalendar.setTime(inputDate);
        inputCalendar.add(Calendar.DAY_OF_MONTH, 30);
        Date thirtyDaysAfter = inputCalendar.getTime();

        return !currentDate.after(thirtyDaysAfter);
    }

    /*
     * 푸시 알림 생성 메소드
     * 새로 감지된 항목을 받아 푸시 알림을 생성함
     *
     */
    public void makePush(Notification noti) {

        NotificationManager notificationManager=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder= null;

        /**
         * Oreo 버전(API26 버전)이상에서는 알림시에 NotificationChannel 이라는 개념이 필수 구성요소가 되었다.
         */
        String channelID="channel_01"; //알림채널 식별자
        String channelName="MyChannel01"; //알림채널의 이름(별명)

        NotificationChannel channel= new NotificationChannel(channelID,channelName,NotificationManager.IMPORTANCE_DEFAULT);
        notificationManager.createNotificationChannel(channel);

        builder=new NotificationCompat.Builder(this, channelID);

        builder.setSmallIcon(R.drawable.siren);
        builder.setContentTitle("응급상황");
        builder.setContentText(Objects.equals(noti.getType(), "attack") ?"폭행이":"심정지가" + " 감지되었습니다");
        builder.setAutoCancel(true);

        Bitmap bm= BitmapFactory.decodeResource(getResources(),R.drawable.siren);
        builder.setLargeIcon(bm);//매개변수가 Bitmap을 줘야한다.

        /**
         * 푸쉬 알림을 누르면 앱의 MainActivity가 실행된다.
         */

        Log.d("push", noti.toString());

        PendingIntent pendingIntent;
        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(getApplicationContext(), MainActivity.class),
                PendingIntent.FLAG_MUTABLE);
        builder.setContentIntent(pendingIntent);

        android.app.Notification notification=builder.build();
        notificationManager.notify(111, notification);
    }

}