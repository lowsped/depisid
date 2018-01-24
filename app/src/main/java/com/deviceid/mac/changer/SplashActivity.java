package com.deviceid.mac.changer;

import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.deviceid.mac.changer.HomeActivity;

public class SplashActivity extends AppCompatActivity implements AnimationListener {
    int count = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (VERSION.SDK_INT < 16) {
            getWindow().setFlags(1024, 1024);
        }
        setContentView((int) R.layout.activity_splash);
        ImageView imageView = (ImageView) findViewById(R.id.icon_ic);
        Animation mAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_in);
        mAnim.setAnimationListener(this);
        imageView.clearAnimation();
        imageView.setAnimation(mAnim);
        imageView.startAnimation(mAnim);
        new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(4000);
                    SplashActivity.this.startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                    SplashActivity.this.finish();
                    SplashActivity.this.overridePendingTransition(0, 0);
                } catch (Exception e) {
                }
            }
        }).start();
    }

    public void onAnimationStart(Animation animation) {
    }

    public void onAnimationEnd(Animation animation) {
        Animation mAnim;
        if (this.count == 0) {
            ImageView imageView = (ImageView) findViewById(R.id.icon_ic);
            mAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_out);
            mAnim.setAnimationListener(this);
            imageView.clearAnimation();
            imageView.setAnimation(mAnim);
            imageView.startAnimation(mAnim);
        } else if (this.count == 1) {
            TextView textView = (TextView) findViewById(R.id.app_name);
            textView.setVisibility(0);
            mAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
            mAnim.setAnimationListener(this);
            textView.clearAnimation();
            textView.setAnimation(mAnim);
        } else {
            animation.setAnimationListener(null);
        }
        this.count++;
    }

    public void onAnimationRepeat(Animation animation) {
    }
}
