package com.kmualpha.bbiyongi_app;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import androidx.core.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.kmualpha.bbiyongi_app.R;
import com.kmualpha.bbiyongi_app.notifications.ArrestActivity;

import java.util.Map;

/*
 * Firebase Cloud Message 서비스 처리 클래스
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {
    public static final String FCM_PARAM = "picture";
    private static final String CHANNEL_NAME = "FCM";
    private static final String CHANNEL_DESC = "Firebase Cloud Messaging";
    private int numMessages = 0;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        RemoteMessage.Notification notification = remoteMessage.getNotification(); // 알림 제목
        Map<String, String> data = remoteMessage.getData(); // 알림 텍스
        Log.d("FROM", remoteMessage.getFrom());
        sendNotification(notification, data);
    }

    /*
     * 알림을 생성하고 보내는 역할
     * RemoteMessage.Notification 객체와 데이터 맵을 매개변수로 받음
     * FCN_PARAM을 Bundle에 넣고 ArrestActivity로 전달하는 intent를 생성함
     * PendingIntent를 생성하여 알림을 클릭했을 때 실행할 Intent를 설정함
     * NotificationCompat.Builder를 사용하여 알림을 구성함
     */
    private void sendNotification(RemoteMessage.Notification notification, Map<String, String> data) {
        Bundle bundle = new Bundle();
        bundle.putString(FCM_PARAM, data.get(FCM_PARAM));

        Intent intent = new Intent(this, ArrestActivity.class);
        intent.putExtras(bundle);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, getString(R.string.channel_id))
                .setContentTitle(notification.getTitle()) // 알림 제목
                .setContentText(notification.getBody()) // 알림 내용
                .setAutoCancel(true) // 알림을 클릭하면 자동으로 알림 취소
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)) // 알림 소리를 기본 알림 소리로 설정
                .setContentIntent(pendingIntent) // 알림을 클릭했을 때 실행할 Intent설정
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setDefaults(Notification.DEFAULT_VIBRATE) // 알림이 도착했을 때 기본 진동 패턴 설정
                .setSmallIcon(R.drawable.siren); // 알림 아이콘 설정

        /*
         * NotificationManager를 가져와서 알림을 관리함
         */
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        /*
         * Android 버전이 API 26 이상인 경우 NotificationChannel을 생성하여 알림 채널을 설정함
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    getString(R.string.channel_id), CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription(CHANNEL_DESC);
            channel.setShowBadge(true);
            channel.canShowBadge();
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500});

            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);
        }

        /*
         * NotificationManager를 사용하여 알림을 표시
         */
        assert notificationManager != null;
        notificationManager.notify(0, notificationBuilder.build());
    }
}