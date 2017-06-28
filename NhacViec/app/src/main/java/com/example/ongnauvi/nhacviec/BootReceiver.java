package com.example.ongnauvi.nhacviec;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;
import java.util.List;

/**
 * Created by OngNauVi on 23/06/2017.
 */

public class BootReceiver extends BroadcastReceiver {
    private String mTitle;
    private String mgio;
    private String mNgay;
    private String mTrangThai;
    private String mLap;
    private String[] mDateSplit;
    private String[] mTimeSplit;
    private int mYear, mMonth, mHour, mMinute, mDay, mReceivedID;
    private long mRepeatTime;

    private Calendar mCalendar;
    private AlarmReceiver mAlarmReceiver;

    // Giá trị hằng số bằng mili giây
    private static final long milMinute = 60000L;
    private static final long milHour = 3600000L;
    private static final long milDay = 86400000L;
    private static final long milWeek = 604800000L;
    private static final long milMonth = 2592000000L;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {

            CongViecDatabase CV = new CongViecDatabase(context);
            mCalendar = Calendar.getInstance();
            mAlarmReceiver = new AlarmReceiver();

            List<ClassCV> LCV = CV.getAllCongViec();

            for (ClassCV rm : LCV) {
                mReceivedID = rm.getMacv();
                mLap = rm.getKieulaplai();
                mTrangThai = rm.getTrangthai();
                mNgay = rm.getNgay();
                mgio = rm.getGio();

                mDateSplit = mNgay.split("/");
                mTimeSplit = mgio.split(":");

                mDay = Integer.parseInt(mDateSplit[0]);
                mMonth = Integer.parseInt(mDateSplit[1]);
                mYear = Integer.parseInt(mDateSplit[2]);
                mHour = Integer.parseInt(mTimeSplit[0]);
                mMinute = Integer.parseInt(mTimeSplit[1]);

                mCalendar.set(Calendar.MONTH,--mMonth);
                mCalendar.set(Calendar.YEAR, mYear);
                mCalendar.set(Calendar.DAY_OF_MONTH, mDay);
                mCalendar.set(Calendar.HOUR_OF_DAY, mHour);
                mCalendar.set(Calendar.MINUTE, mMinute);
                mCalendar.set(Calendar.SECOND, 0);

                // Kiểm tra loại lặp lại
                if (mLap.equals("No Repeat")) {
                    mRepeatTime = 0;
                } else if (mLap.equals("Hour")) {
                    mRepeatTime = milHour;
                } else if (mLap.equals("Day")) {
                    mRepeatTime = milDay;
                } else if (mLap.equals("Week")) {
                    mRepeatTime = milWeek;
                } else if (mLap.equals("Month")) {
                    mRepeatTime = milMonth;
                }

                // Tạo một thông báo mới
                if (mTrangThai.equals("true")) {
                    if (mLap.equals("Hour")||mLap.equals("Day")||mLap.equals("Week")||mLap.equals("Month")) {
                        mAlarmReceiver.setRepeatAlarm(context, mCalendar, mReceivedID, mRepeatTime);
                    } else if (mLap.equals("No Repeat")) {
                        mAlarmReceiver.setAlarm(context, mCalendar, mReceivedID);
                    }
                }
            }
        }
    }
}
