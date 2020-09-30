package com.dissertation.watchingyou.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.dissertation.watchingyou.MyServices;
import com.dissertation.watchingyou.R;

public class HomeFragment extends Fragment {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    public static String FACEBOOK_COUNTER = "Facebook Counter";
    public static String WHATSAPP_COUNTER = "Whatsapp Counter";
    private TextView facebook_view;
    private TextView whatsapp_view;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        sharedPreferences = getActivity().getSharedPreferences("Watching You", Context.MODE_PRIVATE);
//        if(!checkUsageStatsAllowedOrNot()){
//            Intent usageAccessIntent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
//            usageAccessIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(usageAccessIntent);
//
//            if(checkUsageStatsAllowedOrNot()){
//                getActivity().startService(new Intent(HomeFragment.this, MyServices.class));
//            }
//        }





        return root;
    }
}