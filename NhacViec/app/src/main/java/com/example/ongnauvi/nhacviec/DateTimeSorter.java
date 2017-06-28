package com.example.ongnauvi.nhacviec;

/**
 * Created by OngNauVi on 23/06/2017.
 */

public class DateTimeSorter {
    public int mIndex;
    public String mDateTime;




    public DateTimeSorter(int index, String DateTime){
        mIndex = index;
        mDateTime = DateTime;
    }

    public DateTimeSorter(){}


    public int getIndex() {
        return mIndex;
    }

    public void setIndex(int index) {
        mIndex = index;
    }

    public String getDateTime() {
        return mDateTime;
    }

    public void setDateTime(String dateTime) {
        mDateTime = dateTime;
    }
}
