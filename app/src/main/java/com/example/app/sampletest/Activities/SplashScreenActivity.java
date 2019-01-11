/*
 * Created by Prabhakaran on 10/1/2019
 */
package com.example.app.sampletest.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.example.app.sampletest.CommonUtils.SharedPrefManager;
import com.example.app.sampletest.CommonUtils.Utilities;
import com.example.app.sampletest.R;

public class SplashScreenActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        sharedPrefManager = new SharedPrefManager(this);
        int SPLASH_TIME_OUT = 3000;
        if(Utilities.isNetworkAvailable(this)) {
            new Handler().postDelayed(() -> {
                Intent i;
                if (sharedPrefManager.getISLogged_IN()) {
                    i = new Intent(SplashScreenActivity.this, VideoListActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    i = new Intent(SplashScreenActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }, SPLASH_TIME_OUT);
        }else {
            Toast.makeText(this, "Check your Network Connection!", Toast.LENGTH_SHORT).show();
        }
    }
}
