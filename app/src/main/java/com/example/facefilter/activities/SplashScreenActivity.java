package com.example.facefilter.activities;

import android.os.Bundle;
import android.os.Handler;

import com.example.facefilter.R;
import com.example.facefilter.base.BaseActivity;

import androidx.annotation.Nullable;

public class SplashScreenActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startNewActivity(MainActivity.class);
                SplashScreenActivity.this.finish();
            }
        }, 3000);

    }
}
