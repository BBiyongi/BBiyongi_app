package com.kmualpha.bbiyongi_app;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.preference.PreferenceManager;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.reflect.TypeToken;
import com.kmualpha.bbiyongi_app.notifications.Notification;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class AddressActivity extends Activity {

    EditText edit_address;
    EditText edit_camera;
    TextView btn_set_return;
    Switch toggle112;
    boolean contain112;

    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        edit_address = findViewById(R.id.edit_address);
        edit_address.setText(preferences.getString("emergency", ""));

        edit_camera = findViewById(R.id.edit_camera);
        edit_camera.setText(preferences.getString("camera", ""));

        toggle112 = findViewById(R.id.toggle112);
        toggle112.setChecked(Boolean.parseBoolean(preferences.getString("contain112", "false")));
        // 112 toggle switch 상태 불러오기
        toggle112.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // 스위치 상태 가져오기
            contain112 = isChecked;
        });

        // 설정 저장 -> 팝업 액티비티 종료
        btn_set_return = findViewById(R.id.btn_set_return);
        btn_set_return.setOnClickListener(v -> {
            SharedPreferences.Editor editor = preferences.edit(); // 완료 버튼을 누르면 변경사항 저장

            // 1. 입력한 전화번호가 있다면
            if ((edit_address.getText().length() > 0)) {
                String address = String.valueOf(edit_address.getText()); // 입력한 전화번호
                // 1-1 입력한 전화번호가 있는데 비정상적이라면
                if (!Pattern.matches("^01(?:0|1[6-9])(?:\\d{3}|\\d{4})\\d{4}$", address)) {
                    Toast.makeText(getApplicationContext(), "올바른 전화번호가 아닙니다", Toast.LENGTH_SHORT).show();
                }
                // 1-2 입력한 전화번호가 있는데 정상적이라면
                else {
                    editor.putString("emergency", address); // 비상연락망 저장
                    editor.putString("camera", edit_camera.getText().toString());
                    saveCamera(edit_camera.getText().toString());
                    editor.putString("contain112", contain112?"true":"false");
                    editor.apply(); // 변경사항 저장
                    Toast.makeText(getApplicationContext(), "저장되었습니다", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
            // 2. 입력한 전화번호가 없다면
            else {
                editor.putString("camera", edit_camera.getText().toString());
                saveCamera(edit_camera.getText().toString());
                editor.putString("contain112", contain112?"true":"false"); // toggle switch 저장
                editor.apply(); // toggle switch만 저장
                Toast.makeText(getApplicationContext(), "저장되었습니다", Toast.LENGTH_SHORT).show();
                finish();
            }

        });
    }
    public void saveCamera(String camera) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://alpha-92011-default-rtdb.firebaseio.com");
        DatabaseReference databaseReference = firebaseDatabase.getReference();

        databaseReference.child("cam1_address").setValue(camera);

    }
}
