package bbt.com.prompter.helper;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 * Created by anish on 12-09-2017.
 */

public class AppAlarmHelper extends BroadcastReceiver {
    private static final String ACTION_1 = "Shut Off";
    private static AlarmManager alarmManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action;
        action = intent.getAction();

    }


    public void setAlarm(Context context, int alarmID, boolean isRepeating, long timeInMilliSec) {
        Calendar calendar = Calendar.getInstance();
        cancelAlarm(context, alarmID);

        Intent intent = new Intent(context, AppAlarmHelper.class);
        intent.putExtra(IntentConstants.INTENT_ALARM_ID, alarmID);

        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, alarmID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmMgr = getAlarmManager(context);
        /*
        long millis = minutes * 60 * 1000;
        * */
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
