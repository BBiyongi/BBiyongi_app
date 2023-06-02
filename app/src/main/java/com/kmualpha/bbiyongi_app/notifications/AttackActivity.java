package com.kmualpha.bbiyongi_app.notifications;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.google.gson.Gson;
import com.kmualpha.bbiyongi_app.R;
import com.kmualpha.bbiyongi_app.SaveActivity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class AttackActivity extends AppCompatActivity {

    ImageView btn_back;
    ListView list_view;
    ArrayList<Notification> notificationArrayList = new ArrayList<>();

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
        list_view = (ListView) findViewById(R.id.list_view);
        final AttackAdapter myAdapter = new AttackAdapter(this,notificationArrayList);

        list_view.setAdapter(myAdapter);

        // 각 알림 intent 전달하여 Save Activity 실행
        list_view.setOnItemClickListener((parent, v, position, id) -> {
            Intent intent = new Intent(getApplicationContext(), SaveActivity.class);
            intent.putExtra("notification", myAdapter.getItem(position));

            // 아직 확인하지 않은 알림이라면 클릭했을 때 checked 갱신
            boolean checked = myAdapter.getItem(position).getChecked();
            if (!checked) {
                myAdapter.getItem(position).checked = true;
                Notification noti = notificationArrayList.get(position);
                noti.checked = true;
                notificationArrayList.set(position, noti);
                // 프리퍼런스에 반영
                Gson gson = new Gson();
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = preferences.edit();
                String attackJson = gson.toJson(notificationArrayList);
                editor.putString("attackList", attackJson);
                editor.apply();
            }
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 알림 클릭 후 다시 목록 화면으로 돌아오면 checked 여부 갱신된 list로 설정
        try {
            this.InitializeData();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        final AttackAdapter myAdapter = new AttackAdapter(this, notificationArrayList);
        list_view.setAdapter(myAdapter);
    }


    public void InitializeData() throws ParseException {
        // 알림 목록 intent 받아와서 불러오기
        Intent intent = getIntent();
        notificationArrayList = (ArrayList<Notification>) intent.getSerializableExtra("attackList");
        if (notificationArrayList == null) notificationArrayList = new ArrayList<>();

        // date 필드를 기준으로 정렬
        Collections.sort(notificationArrayList, new Comparator<Notification>() {
            @Override
            public int compare(Notification n1, Notification n2) {
                String date1 = n1.getDate();
                String date2 = n2.getDate();
                return date2.compareTo(date1);
            }
        });
    }
}
