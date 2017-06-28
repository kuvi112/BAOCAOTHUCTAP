package com.example.ongnauvi.nhacviec;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;

import java.util.Calendar;

/**
 * Created by OngNauVi on 21/06/2017.
 */

public class AlarmReceiver extends BroadcastReceiver{

    AlarmManager alarmManager;
    PendingIntent pendingIntent;

    @Override
    public void onReceive(Context context, Intent intent) {

        int mReceivedID = Integer.parseInt(intent.getStringExtra(ChinhSuaCV.KEY_ID));

        // Nhận tiêu đề thông báo từ Cơ sở dữ liệu
        CongViecDatabase db = new CongViecDatabase(context);
        ClassCV CV = db.getCongViec(mReceivedID);
        String mTitle = CV.getTencv();

        // Tạo mục đích để mở ReminderEditActivity trên nhấp chuột thông báo
        Intent editIntent = new Intent(context, ChinhSuaCV.class);
        editIntent.putExtra(ChinhSuaCV.KEY_ID, Integer.toString(mReceivedID));
        PendingIntent mClick = PendingIntent.getActivity(context, mReceivedID, editIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Tạo thông báo
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                .setSmallIcon(R.drawable.ic_alarm_on_white_24dp)
                .setContentTitle("Bạn Có Công Việc Cần Làm!!!")
                .setTicker(mTitle)
                .setContentText(mTitle)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(mClick)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true);

        NotificationManager nManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nManager.notify(mReceivedID, mBuilder.build());

    }

    public void setAlarm(Context context, Calendar calendar, int ID) {
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        //
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(ChinhSuaCV.KEY_ID, Integer.toString(ID));
        pendingIntent = PendingIntent.getBroadcast(context, ID, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        //
        Calendar c = Calendar.getInstance();
        long currentTime = c.getTimeInMillis();
        long diffTime = calendar.getTimeInMillis() - currentTime;

        //
        alarmManager.set(AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + diffTime,
                pendingIntent);

        //
        ComponentName receiver = new ComponentName(context, BootReceiver.class);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    public void setRepeatAlarm(Context context, Calendar calendar, int ID, long RepeatTime) {
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        //
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(ChinhSuaCV.KEY_ID, Integer.toString(ID));
        pendingIntent = PendingIntent.getBroadcast(context, ID, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        //
        Calendar c = Calendar.getInstance();
        long currentTime = c.getTimeInMillis();
        long diffTime = calendar.getTimeInMillis() - currentTime;

        //
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + diffTime,
                RepeatTime , pendingIntent);

        //
        ComponentName receiver = new ComponentName(context, BootReceiver.class);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    public void cancelAlarm(Context context, int ID) {
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // Hủy Báo thức bằng ID
        pendingIntent = PendingIntent.getBroadcast(context, ID, new Intent(context, AlarmReceiver.class), 0);
        alarmManager.cancel(pendingIntent);

        // Vô hiệu hoá báo thức
        ComponentName receiver = new ComponentName(context, BootReceiver.class);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }
}
