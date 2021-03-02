package com.example.myrunnerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SettingActivity extends AppCompatActivity {
    private Switch night_modeSwitch, backdown_notifSwitch;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    NightMode nightMode = new NightMode();
    int night_mode;
    boolean service_notification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            sharedPreferences = getApplicationContext().getSharedPreferences("Runneruser", Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();
            night_mode = sharedPreferences.getInt("night_mode", 0);
            service_notification = sharedPreferences.getBoolean("service_notification", true);
            if (night_mode == 1) {
                setTheme(R.style.DarkThemeStylesSettingActivity);
            }
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_setting);
            inicialize();
            if (night_mode == 1)
                night_modeSwitch.setChecked(true);
            if (service_notification)
                backdown_notifSwitch.setChecked(true);
            else
                backdown_notifSwitch.setChecked(false);

            night_modeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        if (night_mode == 0) {
                            editor.putInt("night_mode", 1);
                            editor.commit();
                            nightMode.NightMode(R.color.white, SettingActivity.this);
                            startActivity(new Intent(SettingActivity.this, SettingActivity.class));
                        } else
                            Toast.makeText(SettingActivity.this, "Night mode is on", Toast.LENGTH_SHORT).show();
                    } else

                            if (night_mode == 1) {
                                editor.putInt("night_mode", 0);
                                editor.commit();
                                nightMode.NightMode(R.color.black, SettingActivity.this);
                                startActivity(new Intent(SettingActivity.this, SettingActivity.class));
                            } else
                                Toast.makeText(SettingActivity.this, "Night mode is off", Toast.LENGTH_SHORT).show();
                }
            });
            backdown_notifSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    try {
                        if (isChecked)
                            editor.putBoolean("service_notification", true);
                        else
                            editor.putBoolean("service_notification", false);
                        editor.commit();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_setting);
            bottomNavigationView.setSelectedItemId(R.id.setting_toolbarlist);
            bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    try {
                        switch (item.getItemId()) {
                            case R.id.setting_toolbarlist:
                                return true;
                            case R.id.home_toolbarlist:
                                Intent intentgraph = new Intent(SettingActivity.this, MainActivity.class);
                                startActivity(intentgraph);
                                overridePendingTransition(0, 0);
                                return true;

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(SettingActivity.this, MainActivity.class));
    }

    private void inicialize() {
        night_modeSwitch = findViewById(R.id.nigh_mode_switch);
        backdown_notifSwitch = findViewById(R.id.notification_switch);
    }
}