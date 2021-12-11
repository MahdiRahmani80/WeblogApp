package com.mahdirahmani8.weblogapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.mahdirahmani8.weblogapp.Fragments.CreateFragment;
import com.mahdirahmani8.weblogapp.Fragments.HomeFragment;
import com.mahdirahmani8.weblogapp.Fragments.ProfileFragment;
import com.mahdirahmani8.weblogapp.Fragments.SaveFragment;

public class MianActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, NavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView bnv;
    NavigationView leftNavigationView;
    FrameLayout frameLayout;
    int from = 0;
    boolean isLandSpace = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mian);

        cast();

        if (savedInstanceState != null) {
            isLandSpace = savedInstanceState.getBoolean("IS_LAND");
            if (!isLandSpace) {
                bnv.setOnNavigationItemSelectedListener(this);
            }
        } else {
            bnv.setOnNavigationItemSelectedListener(this);
        }


    }

    private void cast() {
        bnv = findViewById(R.id.bnv_min);
        leftNavigationView = findViewById(R.id.nv_min);
        frameLayout = findViewById(R.id.frame_container);

         getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new HomeFragment()).commit();

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int navID = item.getItemId();

        switch (navID) {
            case R.id.feed:

                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new HomeFragment()).commit();
                break;

            case R.id.create:
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new CreateFragment()).commit();
                break;

//            case R.id.micro_weblogging:
//                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new MiniBlogFragment()).commit();
//                break;

            case R.id.pin:
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new SaveFragment()).commit();
                break;

            case R.id.account:
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new ProfileFragment()).commit();
                break;
        }

        return true;
    }


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("IS_LAND", !isLandSpace);
    }

}