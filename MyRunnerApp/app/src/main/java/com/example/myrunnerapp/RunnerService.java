package com.example.myrunnerapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import java.util.Timer;
import java.util.TimerTask;

public class RunnerService extends Service implements SensorEventListener {
    boolean running;
    SensorManager mSensorManager;
    Sensor mStepCounter;
    LocationManager locationManager;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public Notification notification;
    public NotificationCompat.Builder notificationBuilder;
    public NotificationManager manager;
    int step;
    boolean service_notification;

    public RunnerService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sharedPreferences = getApplicationContext().getSharedPreferences("Runneruser", Context.MODE_PRIVATE);
        service_notification = sharedPreferences.getBoolean("service_notification", true);
        editor = sharedPreferences.edit();
        step = sharedPreferences.getInt("stepcount", 0);
        editor.putInt("distance_step", 0);
        editor.commit();
        if (service_notification) {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O)
                startMyForeground();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        counststep();
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startMyForeground() {
        String NOTIFICATION_CHANNEL_ID = "example.runner";
        String channelName = "Background service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.createNotificationChannel(chan);
        Intent resultIntent = new Intent(this, MainActivity.class);
        resultIntent.setAction(Intent.ACTION_MAIN);
        resultIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                resultIntent, 0);
        notificationBuilder = new NotificationCompat.Builder(RunnerService.this, NOTIFICATION_CHANNEL_ID);
        notification = notificationBuilder.setOngoing(true)
                .setContentTitle("Runner App")
                .setContentText("Steps : " + sharedPreferences.getInt("distance_step", 0))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                .setVibrate(new long[]{0L})
                .setContentIntent(pendingIntent)
                .build();
        notification.flags = Notification.FLAG_ONGOING_EVENT;
        startForeground(1, notification);

    }


    private void counststep() {
        mSensorManager = ((SensorManager) getSystemService(SENSOR_SERVICE));
        mStepCounter = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (mStepCounter != null)
            mSensorManager.registerListener((SensorEventListener) RunnerService.this, mStepCounter, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sharedPreferences.getInt("run",0) == 1) {
            int distancesteps = sharedPreferences.getInt("distance_step", 0);
            int startstep = sharedPreferences.getInt("stepcount", 0) - distancesteps;
            editor.putInt("stepcount", (int) sensorEvent.values[0]);
            editor.commit();
            changenotification(startstep);
        }else {
            editor.putInt("stepcount", (int) sensorEvent.values[0]);
        }
        editor.commit();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public void changenotification(int startstep) {
        int steps = sharedPreferences.getInt("stepcount", 0) - startstep;
        editor.putInt("distance_step", steps);
        editor.commit();
        if (service_notification) {
            notificationBuilder.setContentText("Steps : " + sharedPreferences.getInt("distance_step", 0));
            manager.notify(1, notificationBuilder.build());
        }
    }
}
