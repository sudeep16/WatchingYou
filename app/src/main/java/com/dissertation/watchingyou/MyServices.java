package com.dissertation.watchingyou;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.dissertation.watchingyou.DashboardActivity.INSTAGRAM_COUNTER;
import static com.dissertation.watchingyou.DashboardActivity.SKYPE_COUNTER;
import static com.dissertation.watchingyou.DashboardActivity.YOUTUBE_COUNTER;
import static com.dissertation.watchingyou.NotificationCreator.CHANNEL_ID;
import static com.dissertation.watchingyou.DashboardActivity.FACEBOOK_COUNTER;
import static com.dissertation.watchingyou.DashboardActivity.WHATSAPP_COUNTER;

public class MyServices extends Service {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public MyServices() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //For Foregorund Notification
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Watching You")
                .setContentText("You are being monitored")
                .setSmallIcon(R.drawable.eyes)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);

        //Testing
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List <ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
        Log.d("topActivity", "CURRENT Activity ::" + taskInfo.get(0).topActivity.getClassName());
        ComponentName componentInfo = taskInfo.get(0).topActivity;
        componentInfo.getPackageName();


        //For BackgroundServices
        sharedPreferences = getSharedPreferences("Watching You", Context.MODE_PRIVATE);

        System.out.println("the sharedpreference is: "+sharedPreferences);
        editor = sharedPreferences.edit();
        TimerTask detectApp = new TimerTask() {

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void run() {
                sharedPreferences = getSharedPreferences("Watching You", MODE_PRIVATE);
                editor = sharedPreferences.edit();
                UsageStatsManager usageStatsManager = (UsageStatsManager) getSystemService(USAGE_STATS_SERVICE);
                long endTime = System.currentTimeMillis();
                long beginTime = endTime-(1000);
                List<UsageStats> usageStats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, beginTime, endTime);

                if (usageStats != null) {
                    for (UsageStats usageStat : usageStats){

                        if(usageStat.getPackageName().toLowerCase().contains("com.whatsapp")){
                            editor.putLong(WHATSAPP_COUNTER, usageStat.getTotalTimeInForeground());
                        }
                        if(usageStat.getPackageName().toLowerCase().contains("com.facebook.katana")){
                            //System.out.println("the total time used is "+usageStat.getTotalTimeInForeground());
                            editor.putLong(FACEBOOK_COUNTER, usageStat.getTotalTimeInForeground());
                        }
                        if(usageStat.getPackageName().toLowerCase().contains("com.google.android.youtube")){
                            //System.out.println("the total time used is "+usageStat.getTotalTimeInForeground());
                            editor.putLong(YOUTUBE_COUNTER, usageStat.getTotalTimeInForeground());
                        }
                        if(usageStat.getPackageName().toLowerCase().contains("com.instagram.android")){
                            //System.out.println("the total time used is "+usageStat.getTotalTimeInForeground());
                            editor.putLong(INSTAGRAM_COUNTER, usageStat.getTotalTimeInForeground());
                        }
                        if(usageStat.getPackageName().toLowerCase().contains("com.skype.raider")){
                            //System.out.println("the total time used is "+usageStat.getTotalTimeInForeground());
                            editor.putLong(SKYPE_COUNTER, usageStat.getTotalTimeInForeground());
                        }

                        editor.apply();
                    }
                }
            }
        };
        Timer detectAppTimer = new Timer();
        detectAppTimer.scheduleAtFixedRate(detectApp,0, 1000);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
