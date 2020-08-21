package com.example.ouicoding.Service;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.example.ouicoding.R;
import com.example.ouicoding.View.MainActivity;

import gr.net.maroulis.library.EasySplashScreen;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EasySplashScreen config= new EasySplashScreen(SplashScreen.this)
                .withFullScreen()
                .withTargetActivity(MainActivity.class)
                .withSplashTimeOut(3000)
                .withBackgroundColor(Color.parseColor("#ffffff"))
                .withLogo(R.drawable.logo);

        config.getLogo().setMaxHeight(200);
        config.getLogo().setMaxWidth(200);
        View EasySplashScreen = config.create();
        setContentView(EasySplashScreen);
    }
}