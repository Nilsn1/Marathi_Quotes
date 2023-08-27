package com.nilscreation.marathiquotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {

    ImageView applogo;
    TextView appname, subtext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        applogo = findViewById(R.id.applogo);
        appname = findViewById(R.id.appname);
        subtext = findViewById(R.id.subtext);

        applogo.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in));
        appname.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in));
        subtext.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in));

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 1500);
    }
}