package com.dissertation.watchingyou;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public boolean startService(View v) {
        Intent serviceIntent = new Intent(this, MyServices.class);
        startService(serviceIntent);

        Intent intent = new Intent(MainActivity.this, TestActivity.class);
        startActivity(intent);

        return true;
    }
}