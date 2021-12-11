package com.mahdirahmani8.weblogapp;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.mahdirahmani8.weblogapp.DataBase.DataBase;

public class FirstSplash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_splash);

        DataBase db = new DataBase(FirstSplash.this);

        if (!db.isLogin()){
            Intent intent = new Intent(FirstSplash.this,Splash.class);
            startActivity(intent);
            finish();
        }else {
            showSplash();
//            Intent intent = new Intent(FirstSplash.this,MianActivity.class);
//            startActivity(intent);
//            finish();
        }

    }

    private void showSplash() {

        new Handler().postDelayed(() -> {

            Intent intent = new Intent(FirstSplash.this,MianActivity.class);
            ActivityOptions options =
                    ActivityOptions.makeCustomAnimation(FirstSplash.this, R.anim.fade_in, R.anim.fade_out);
            startActivity(intent,options.toBundle());
            finish();
        }, 3000);
    }
}