package com.dissertation.watchingyou;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.sql.Time;
import java.util.Timer;
import java.util.TimerTask;

public class SplashScreen extends AppCompatActivity {
    ProgressBar progressBar;
    int proStatus = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        proBar();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (proStatus == 100) {
                    Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(SplashScreen.this, "The Splash Is Not Working", Toast.LENGTH_LONG).show();
                }
            }
        }, 2000);
    }


    //Counter for Progress Bar till it reaches 100
    public final void proBar() {
        progressBar = findViewById(R.id.proBar);
        final Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                proStatus++;
                progressBar.setProgress(proStatus);

                if (proStatus == 100)
                    timer.cancel();
            }
        };
        timer.schedule(timerTask, 0, 10);
    }
}
