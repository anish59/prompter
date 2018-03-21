package bbt.com.prompter.helper;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import bbt.com.prompter.R;

/**
 * Created by anish on 08-03-2018.
 */

public class DummyNotification {
    public static final int NOTIFICATION_ID = 1;

    public static final String ACTION_1 = "action_1";
    public static final String ACTION_2 = "action_2";

    public static void displayNotification(Context context) {

        Intent action1Intent = new Intent(context, NotificationActionService.class)
                .setAction(ACTION_1);

        PendingIntent action1PendingIntent = PendingIntent.getService(context, 0,
                action1Intent, PendingIntent.FLAG_ONE_SHOT);

        Intent action2Intent = new Intent(context, NotificationActionService.class)
                .setAction(ACTION_2);

        PendingIntent action2PendingIntent = PendingIntent.getService(context, 1,
                action2Intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Sample Notification")
                        .setContentText("Notification text goes here")
                        .addAction(new NotificationCompat.Action(R.mipmap.ic_launcher,
                                "Action 1", action1PendingIntent))
                        .addAction(new NotificationCompat.Action(R.mipmap.ic_launcher,
                                "Action 2", action2PendingIntent));

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
    }

    public static class NotificationActionService extends IntentService {
        public NotificationActionService() {
            super(NotificationActionService.class.getSimpleName());
        }

        @Override
        protected void onHandleIntent(Intent intent) {
            String action = intent.getAction();
            Log.e("action", "Received notification action: " + action);
            if (ACTION_1.equals(action)) {
                Log.e("Action1", "Worked");
                // If you want to cancel the notification: NotificationManagerCompat.from(this).cancel(NOTIFICATION_ID);
            }else{
                Log.e("Action2", "Worked2");
            }
        }
    }
}
