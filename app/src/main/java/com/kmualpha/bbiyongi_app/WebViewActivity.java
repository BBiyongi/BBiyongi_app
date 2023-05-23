package com.kmualpha.bbiyongi_app;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class WebViewActivity extends AppCompatActivity {
    ImageView btn_back;
    WebView live_video;
    WebSettings webSettings;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live);

        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(v -> finish());

        live_video = findViewById(R.id.live);
        webSettings = live_video.getSettings();
        webSettings.setJavaScriptEnabled(true);
        live_video.loadData("<html><head><style type='text/css'>body{margin_auto auto;text-align:center;}"
                + "img{width:100%25;} div{overflow: hidden;} </style><head>"
                + "<body><div><img src='http://11.11.11.111:1111/'/></div></body></html>",
                "text/html", "UTF-8");

    }
}
