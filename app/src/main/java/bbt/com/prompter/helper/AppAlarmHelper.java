package bbt.com.prompter.helper;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import java.util.Date;

import bbt.com.prompter.model.ContactTable;

/**
 * Created by anish on 12-09-2017.
 */

public class AppAlarmHelper extends BroadcastReceiver {
    private static AlarmManager alarmManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action;
        action = intent.getAction();

        if (action != null) {
            setActionListeners(action);
        }

        int fetchingId = intent.getIntExtra(AppConstants.CONTACT_ID, 0);
        ContactTable contactDetail = ContactTable.getContact(fetchingId, context);
        if (contactDetail == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationHelper.sendSimpleNotificationOreo(context, contactDetail.getName(), contactDetail.getTemplate());
        } else {
            NotificationHelper.sendSimpleNotificationNormal(context, contactDetail.getName(), contactDetail.getTemplate());
        }
    }

    public void setActionListeners(String action) {
        Log.e("Action: ", "Received notification action: " + action);
        if (AppConstants.ACTION_SIM1.equals(action)) {
            Log.e("Sim", "One");
        } else if (AppConstants.ACTION_SIM2.equals(action)) {
            Log.e("Sim", "Two");
        }
    }


    public void setAlarm(Context context, int contactID, boolean isRepeating, long timeInMilliSec) {
        cancelAlarm(context, contactID);
        Date date = new Date(timeInMilliSec);
        Log.e("date: ", "" + date);
        /*if (date.before(Calendar.getInstance().getTime())) {
            Log.e("TimeError: ", "entered time is in past, plz check it buddy");
            return;
        }*/
        Intent intent = new Intent(context, AppAlarmHelper.class);
        intent.putExtra(AppConstants.INTENT_ALARM_ID, contactID); //using alarmId same as contactId
        intent.putExtra(AppConstants.CONTACT_ID, contactID);

        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, contactID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmMgr = getAlarmManager(context);


        if (isRepeating) {
            alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, timeInMilliSec, AlarmManager.INTERVAL_DAY, alarmIntent);
        } else {
            alarmMgr.set(AlarmManager.RTC_WAKEUP, timeInMilliSec, alarmIntent);
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
