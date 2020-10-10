package com.dissertation.watchingyou;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class DashboardActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    public static String FACEBOOK_COUNTER = "facebook_count";
    public static String WHATSAPP_COUNTER = "whatsapp_count";
    public static String INSTAGRAM_COUNTER = "instagram_count";
    public static String SKYPE_COUNTER = "skype_count";
    public static String YOUTUBE_COUNTER = "youtube_count";
    private TextView facebook_view;
    private TextView whatsapp_view;
    private TextView youtube_view;
    private TextView skype_view;
    private TextView instagram_view;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.theme:
                Intent intent = new Intent(DashboardActivity.this, ThemeSelector.class);
                startActivity(intent);
                return true;

            case R.id.about_us:
                intent= new Intent(DashboardActivity.this, AboutUs.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);



        sharedPreferences = getSharedPreferences("Watching You", Context.MODE_PRIVATE);
        if (!checkUsageStatsAllowedOrNot()) {
            Intent usageAccessIntent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            usageAccessIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(usageAccessIntent);

            if (checkUsageStatsAllowedOrNot()) {
                startService(new Intent(DashboardActivity.this, MyServices.class));
            } else {
                Toast.makeText(getApplicationContext(), "Please Give Access", Toast.LENGTH_SHORT).show();
            }
        } else {
            startService(new Intent(DashboardActivity.this, MyServices.class));
        }
        facebook_view = findViewById(R.id.facebook_time);
        whatsapp_view = findViewById(R.id.whatsapp_time);
        youtube_view = findViewById(R.id.youtube_time);
        skype_view = findViewById(R.id.skype_time);
        instagram_view = findViewById(R.id.instagram_time);

        TimerTask updateView = new TimerTask() {

            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        //Facebook
                        long facebook_time = sharedPreferences.getLong(FACEBOOK_COUNTER, 0);
                        //System.out.println("the timer in sharedpreference is: "+facebook_time);

                        long second = (facebook_time / 1000) % 60;
                        long minute = (facebook_time / (1000 * 60)) % 60;
                        long hour = (facebook_time / (1000 * 60 * 60));
                        String facebook_val = hour + " h " + minute + " m " + second + " s ";
                        facebook_view.setText(facebook_val);

                        //System.out.println("the facebook time is: "+facebook_val);
                        //Whatsapp
                        long whatsapp_time = sharedPreferences.getLong(WHATSAPP_COUNTER, 0);
                        second = (whatsapp_time / 1000) % 60;
                        minute = (whatsapp_time / (1000 * 60)) % 60;
                        hour = (whatsapp_time / (1000 * 60 * 60));
                        String whatsapp_val = hour + " h " + minute + " m " + second + " s ";
                        whatsapp_view.setText(whatsapp_val);

                        //Instagram
                        long instagram_time = sharedPreferences.getLong(INSTAGRAM_COUNTER, 0);
                        second = (instagram_time / 1000) % 60;
                        minute = (instagram_time / (1000 * 60)) % 60;
                        hour = (instagram_time / (1000 * 60 * 60));
                        String instagram_val = hour + " h " + minute + " m " + second + " s ";
                        instagram_view.setText(instagram_val);

                        //Skype
                        long skype_time = sharedPreferences.getLong(SKYPE_COUNTER, 0);
                        second = (skype_time / 1000) % 60;
                        minute = (skype_time / (1000 * 60)) % 60;
                        hour = (skype_time / (1000 * 60 * 60));
                        String skype_val = hour + " h " + minute + " m " + second + " s ";
                        skype_view.setText(skype_val);

                        //Youtube
                        long youtube_time = sharedPreferences.getLong(YOUTUBE_COUNTER, 0);
                        second = (youtube_time / 1000) % 60;
                        minute = (youtube_time / (1000 * 60)) % 60;
                        hour = (youtube_time / (1000 * 60 * 60));
                        String youtube_val = hour + " h " + minute + " m " + second + " s ";
                        youtube_view.setText(youtube_val);

                    }
                });
            }
        };
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(updateView, 0, 1000);

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public boolean checkUsageStatsAllowedOrNot(){
        try {
            PackageManager packageManager = getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(getPackageName(), 0);
            AppOpsManager appOpsManager = (AppOpsManager) getSystemService(APP_OPS_SERVICE);
            int mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, applicationInfo.uid, applicationInfo.packageName);


            return (mode==AppOpsManager.MODE_ALLOWED);
        }
        catch (Exception e){
            Toast.makeText(getApplicationContext(), "Error Not Found", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onDestroy() {
        if(checkUsageStatsAllowedOrNot()){
            startService(new Intent(DashboardActivity.this, MyServices.class));
        }
        super.onDestroy();
    }

    public void timerFunc(View view) {
        startActivity(new Intent(DashboardActivity.this, TimerActivity.class));
        finish();
    }
}