package com.hub.anyspot.views.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.hub.anyspot.R;

/**
 * Created by LYS on 2017-11-27.
 */

public class A00_SplashActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a00_activity_splash);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
//                SharedPreferences preferences = getSharedPreferences("user_info", 0);
//                boolean auto_login = preferences.getBoolean("auto_login", false);
                Intent intent;
//                if (auto_login == true) {
//                    // 로딩 후 메인 화면으로 넘김
//                    intent = new Intent(getBaseContext(), C00_MainActivity.class);
//                } else {
                    intent = new Intent(A00_SplashActivity.this, B10_LoginActivity.class);
//                }
                startActivity(intent);
                overridePendingTransition(R.anim.enter_no_anim, R.anim.exit_no_anim);
                finish();
            }
        }, 2000);

    }
}
