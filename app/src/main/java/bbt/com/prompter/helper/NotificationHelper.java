package bbt.com.prompter.helper;

import android.Manifest;
import android.app.Activity;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.util.Log;
import android.widget.Toast;

import java.util.List;
import java.util.Random;

import bbt.com.prompter.MainActivity;
import bbt.com.prompter.R;

/**
 * Created by anish on 23-01-2018.
 */

public class NotificationHelper {


    private static String carrierName = "";
    private static int simOneSubId, simTwoSubId;


    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void sendSimpleNotificationAboveOreo(Context context, String title, String content, String number) {

        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(AppConstants.INTENT_NAME, title);//title contains name here
        intent.putExtra(AppConstants.INTENT_MSG, content);
        intent.putExtra(AppConstants.INTENT_CONTACT, number);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);


        Random random = new Random();
        int notifyID = random.nextInt(9999 - 1000) + 1000;
        String CHANNEL_ID = "my_channel_01";// The id of the channel.
        CharSequence name = "My Notification";// The user-visible name of the channel.
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);

        mChannel.setSound(null, null);

        Notification.Builder mBuilder;
        mBuilder = new Notification.Builder(context);

//        setActions(context, mBuilder);
// Create a notification and set the notification channel.

        setActions(context, mBuilder);


        Notification notification = mBuilder
                .setContentTitle(title)
                .setAutoCancel(true)
                .setContentText(content)
                .setContentIntent(pendingIntent)
                .setSmallIcon(getNotificationIcon())
                .setChannelId(CHANNEL_ID)
                .build();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager mNotificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.createNotificationChannel(mChannel);

            mNotificationManager.notify(notifyID, notification);
        }

    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    public static void sendSimpleNotificationNormal(Context context, String title, String content, String number) {


        Intent intent1 = new Intent(context, MainActivity.class);
        intent1.putExtra(AppConstants.INTENT_NAME, title);//title contains name here
        intent1.putExtra(AppConstants.INTENT_MSG, content);
        intent1.putExtra(AppConstants.INTENT_CONTACT, number);
        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder mBuilder;
        mBuilder = new Notification.Builder(context);

        NotificationCompat.BigTextStyle s = new NotificationCompat.BigTextStyle();
        s.setBigContentTitle(title);
        s.setSummaryText(content);

        //Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        setActions(context, mBuilder);
        Notification notification;
        notification = mBuilder
                .setSmallIcon(getNotificationIcon())
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                .setTicker(title)

                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setContentTitle(title)
                .setAutoCancel(true)
                .setContentText(content)
                .setStyle(new Notification.BigTextStyle().bigText(content))
                .build();


        Random random = new Random();
        int m = random.nextInt(9999 - 1000) + 1000;

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(m, notification);


    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    public static void setActions(Context context, Notification.Builder mBuilder) {
        String simOneName = "", simTwoName = "";


        SubscriptionManager subscriptionManager = (SubscriptionManager) context.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(context, "It won't work unless you grant the permission! :/", Toast.LENGTH_SHORT).show();
            return;
        }
        List<SubscriptionInfo> subscriptionInfoList = subscriptionManager.getActiveSubscriptionInfoList();

        if (subscriptionInfoList != null && subscriptionInfoList.size() > 0) {
            for (int i = 0; i < subscriptionInfoList.size(); i++) {
                if (simOneName.equals("")) {
                    simOneName = subscriptionInfoList.get(i).getCarrierName().toString();
                    simOneSubId = subscriptionInfoList.get(i).getSubscriptionId();
                } else {
                    simTwoName = subscriptionInfoList.get(i).getCarrierName().toString();
                    simTwoSubId = subscriptionInfoList.get(i).getSubscriptionId();
                    break;// so two names are already acquired now we need to break the loop and proceed
                }
            }

        }

        {

            if ((simOneName != null && simOneName.equalsIgnoreCase(""))
                    && (simTwoName != null && simTwoName.equalsIgnoreCase(""))) {
                UiHelper.showToast("No Sim Configuration Found", (Activity) context, true);
                return;
            }


            if (simOneName != null) {
                Intent SIM_ONE = new Intent(context, AppAlarmHelper.class);
                SIM_ONE.putExtra(AppConstants.INTENT_ACTION_SIM_1, true);
                SIM_ONE.setAction(AppConstants.ACTION_SIM1);
                PendingIntent pISimOne = PendingIntent.getBroadcast(context, 12, SIM_ONE, PendingIntent.FLAG_UPDATE_CURRENT);
                mBuilder.addAction(new Notification.Action(0, simOneName, pISimOne));
            }

            if (simTwoName != null) {
                Intent SIM_TWO = new Intent(context, AppAlarmHelper.class);
                SIM_TWO.putExtra(AppConstants.INTENT_ACTION_SIM_1, true);
                SIM_TWO.setAction(AppConstants.ACTION_SIM2);
                PendingIntent pISimTwo = PendingIntent.getBroadcast(context, 13, SIM_TWO, PendingIntent.FLAG_UPDATE_CURRENT);
                mBuilder.addAction(new Notification.Action(0, simTwoName, pISimTwo));
            }

        }
    }

    private static int getNotificationIcon() {
        boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.mipmap.ic_launcher : R.mipmap.ic_launcher;
    }


    public static class NotificationActionService extends IntentService { // class not in use

        public NotificationActionService() {
            super(NotificationActionService.class.getSimpleName());
        }

        @Override
        protected void onHandleIntent(Intent intent) {
            String action = intent.getAction();
            System.out.println("Received notification action: " + action);
            Log.e("Action: ", "Received notification action: " + action);
            if (AppConstants.ACTION_SIM1.equals(action)) {
                Toast.makeText(this, "Sim 1 Selected", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Sim 2 Selected", Toast.LENGTH_SHORT).show();
            }
        }
    }


}