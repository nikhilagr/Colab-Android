package com.nikhildagrawal.worktrack;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashActivity extends AppCompatActivity {

    ImageView mSplashImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mSplashImage = findViewById(R.id.img_splash);
        Animation anim = AnimationUtils.loadAnimation(this,R.anim.splashanim);
        mSplashImage.setAnimation(anim);
    }
}
