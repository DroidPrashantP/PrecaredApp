package com.app.precared.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.app.precared.R;
import com.app.precared.utils.PrecaredSharePreferences;

public class SplashScreenActivity extends AppCompatActivity {

    // Animation
    private Animation animFadein;
    private ImageView imageView;
    private  PrecaredSharePreferences precaredSharePreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        precaredSharePreferences = new PrecaredSharePreferences(this);
        imageView = (ImageView) findViewById(R.id.imageView);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (precaredSharePreferences.isLoggedIn()) {
                    startActivity(new Intent(SplashScreenActivity.this, HomeActivity.class));
                    finish();
                }else {
                    startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
                    finish();
                }
            }
        },3000);
    }
}
