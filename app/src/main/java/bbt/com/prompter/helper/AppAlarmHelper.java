package bbt.com.prompter.helper;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import bbt.com.prompter.model.ContactTable;

/**
 * Created by anish on 12-09-2017.
 */

public class AppAlarmHelper extends BroadcastReceiver {
    private static final String ACTION_1 = "Shut Off";
    private static AlarmManager alarmManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action;
//        action = intent.getAction();

        if (intent == null) {
            return;
        }
        int fetchingId = intent.getIntExtra(IntentConstants.CONTACT_ID, 0);
        ContactTable contactDetail = ContactTable.getContact(fetchingId, context);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationHelper.sendSimpleNotificationOreo(context, contactDetail.getName(), contactDetail.getTemplate());
        } else {
            NotificationHelper.sendSimpleNotificationNormal(context, contactDetail.getName(), contactDetail.getTemplate());
        }
    }


    public void setAlarm(Context context, int contactID, boolean isRepeating, long timeInMilliSec) {
        cancelAlarm(context, contactID);

        Intent intent = new Intent(context, AppAlarmHelper.class);
        intent.putExtra(IntentConstants.INTENT_ALARM_ID, contactID); //using alarmId same as contactId
        intent.putExtra(IntentConstants.CONTACT_ID, contactID);

        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, contactID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmMgr = getAlarmManager(context);

        if (isRepeating) {
            alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, timeInMilliSec, AlarmManager.INTERVAL_DAY, alarmIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, timeInMilliSec, alarmIntent);
        }
        initBootAlarm(context);
    }

    public void cancelAlarm(Context context, int requestCode) {

        Intent intent = new Intent(context, AppAlarmHelper.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0);

        AlarmManager alarmManager = getAlarmManager(context);
        alarmManager.cancel(pendingIntent);

        initBootAlarm(context);
    }

    private void initBootAlarm(Context context) {
        try {
            ComponentName receiver = new ComponentName(context, AppAlarmHelper.class);
            PackageManager pm = context.getPackageManager();
            pm.setComponentEnabledSetting(receiver,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private AlarmManager getAlarmManager(Context context) {

        if (alarmManager == null) {
            alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        }
        return alarmManager;
    }


}
