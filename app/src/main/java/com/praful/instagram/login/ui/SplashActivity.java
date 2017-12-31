package com.praful.instagram.login.ui;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import com.praful.instagram.login.R;


public class SplashActivity extends AppCompatActivity {

    ImageView imgSplashLogo;
    Animation animationFadeIn;

    Activity mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mContext = this;
        init();
        animationFadeIn = AnimationUtils.loadAnimation(mContext, R.anim.fade_in);
        setSplashAnimation();
    }

    private void init() {
        imgSplashLogo = findViewById(R.id.img_splash_logo);
    }

    private void setSplashAnimation() {
        imgSplashLogo.startAnimation(animationFadeIn);
        animationFadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                Intent intentMain = new Intent(mContext, MainActivity.class);
                startActivity(intentMain);
                finish();
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
