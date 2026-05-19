package com.healthsync.android;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import androidx.core.app.NotificationCompat;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.healthsync.android.util.SessionManager;

public class HealthSyncFirebaseService extends FirebaseMessagingService {

    private static final String CHANNEL_ID = "healthsync_alerts";
    private static final String CHANNEL_NAME = "Alertas de salud";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String title = "HealthSync";
        String body = "";

        if (remoteMessage.getNotification() != null) {
            title = remoteMessage.getNotification().getTitle();
            body = remoteMessage.getNotification().getBody();
        } else if (remoteMessage.getData().size() > 0) {
            body = remoteMessage.getData().get("message");
        }

        mostrarNotificacion(title, body);
    }

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        // Guardar el token FCM en SessionManager
        SessionManager session = new SessionManager(this);
        session.saveFcmToken(token);
    }

    private void mostrarNotificacion(String title, String body) {
        NotificationManager manager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID, CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH);
        manager.createNotificationChannel(channel);

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setSmallIcon(android.R.drawable.ic_dialog_info)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setAutoCancel(true)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setContentIntent(pendingIntent);

        manager.notify((int) System.currentTimeMillis(), builder.build());
    }
}
