package com.example.petcarenotifier.notifications;

import android.app.*;
import android.content.*;
import android.os.Build;
import androidx.core.app.NotificationCompat;

import com.example.petcarenotifier.ui.dashboard.DashboardActivity;

/**
 * BroadcastReceiver triggered by alarms to show pet care notifications.
 */
public class NotificationReceiver extends BroadcastReceiver {
    private static final String CHANNEL_ID = "reminders_channel";

    @Override
    public void onReceive(Context context, Intent intent) {
        String title = intent.getStringExtra("title");
        String message = intent.getStringExtra("message");

        // Ensure notification channel is created
        createNotificationChannel(context);

        // Intent when user taps the notification
        Intent tapIntent = new Intent(context, DashboardActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, 0, tapIntent, PendingIntent.FLAG_IMMUTABLE
        );

        // Build the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        // Show the notification
        NotificationManager manager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify((int) System.currentTimeMillis(), builder.build());
    }

    /**
     * Creates the notification channel (for Android O+).
     */
    private void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID, "Reminders", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Pet care reminders");

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
