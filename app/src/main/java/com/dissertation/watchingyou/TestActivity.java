package com.dissertation.watchingyou;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Time;
import java.util.Timer;
import java.util.TimerTask;

public class TestActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    public static String FACEBOOK_COUNTER = "facebook_count";
    public static String WHATSAPP_COUNTER = "whatsapp_count";
    private TextView facebook_view;
    private TextView whatsapp_view;

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
                Intent intent = new Intent(TestActivity.this, ThemeSelector.class);
                startActivity(intent);
                return true;

            case R.id.about_us:
                intent= new Intent(TestActivity.this, AboutUs.class);
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
        setContentView(R.layout.activity_test);



        sharedPreferences = getSharedPreferences("Watching You", Context.MODE_PRIVATE);
        if (!checkUsageStatsAllowedOrNot()) {
            Intent usageAccessIntent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            usageAccessIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(usageAccessIntent);

            if (checkUsageStatsAllowedOrNot()) {
                startService(new Intent(TestActivity.this, MyServices.class));
            } else {
                Toast.makeText(getApplicationContext(), "Please Give Access", Toast.LENGTH_SHORT).show();
            }
        } else {
            startService(new Intent(TestActivity.this, MyServices.class));
        }
        facebook_view = findViewById(R.id.facebook_time);
        whatsapp_view = findViewById(R.id.whatsapp_time);

        TimerTask updateView = new TimerTask() {

            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        long facebook_time = sharedPreferences.getLong(FACEBOOK_COUNTER, 0);
                        //System.out.println("the timer in sharedpreference is: "+facebook_time);

                        long second = (facebook_time / 1000) % 60;
                        long minute = (facebook_time / (1000 * 60)) % 60;
                        long hour = (facebook_time / (1000 * 60 * 60));
                        String facebook_val = hour + " h " + minute + " m " + second + " s ";
                        facebook_view.setText(facebook_val);

                        //System.out.println("the facebook time is: "+facebook_val);

                        long whatsapp_time = sharedPreferences.getLong(WHATSAPP_COUNTER, 0);
                        second = (whatsapp_time / 1000) % 60;
                        minute = (whatsapp_time / (1000 * 60)) % 60;
                        hour = (whatsapp_time / (1000 * 60 * 60));
                        String whatsapp_val = hour + " h " + minute + " m " + second + " s ";
                        whatsapp_view.setText(whatsapp_val);

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
            startService(new Intent(TestActivity.this, MyServices.class));
        }
        super.onDestroy();
    }
}