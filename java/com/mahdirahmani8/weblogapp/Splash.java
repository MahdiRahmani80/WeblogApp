package com.mahdirahmani8.weblogapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Splash extends AppCompatActivity {

    Button btn_login, btn_sign_in;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Cast();
        onClick();


    }

    private void onClick() {
        btn_login.setOnClickListener(v -> {
            Intent intent = new Intent(Splash.this, SignIn.class);
            startActivity(intent);
            finish();
        });

        btn_sign_in.setOnClickListener(v -> {
            Intent intent = new Intent(Splash.this,SignUp.class);
            startActivity(intent);
            finish();
        });
    }

    private void Cast() {
        btn_login = (Button) findViewById(R.id.login_btn);
        btn_sign_in = (Button) findViewById(R.id.btn_create_account);
    }
}