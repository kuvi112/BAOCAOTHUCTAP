package com.example.ongnauvi.nhacviec;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by OngNauVi on 21/06/2017.
 */

public class AmThanh extends Service {
    MediaPlayer mediaPlayer;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mediaPlayer = MediaPlayer.create(this,R.raw.loveyourself);
        mediaPlayer.start();
        return START_NOT_STICKY;
    }
}
