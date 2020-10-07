package com.dissertation.watchingyou;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class AboutUs extends AppCompatActivity {

    private TextView aboutus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        aboutus = findViewById(R.id.aboutus);

        aboutus.setText("The name of the application is name as Watching You. The application runs offline in the background as a system service keeping in track of the application that is related to social media platform. There are only users in the applications that uses the mobile devices. The users are not separated from admin and customers. ");

    }
}