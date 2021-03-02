package com.example.myrunnerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private TextView DistanceTxt, StepTxt, SpeedTxt;
    private Chronometer chronometer;
    private Button StartBtn, RestartBtn, PauzeBtn;
    private LinearLayout RestartLayout;
    private boolean runtime;
    private int running;
    long pauze = 0;
    long elapsedMillis;
    private Timer timer;
    NightMode nightMode = new NightMode();
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    RunnerService runnerService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPreferences = getApplicationContext().getSharedPreferences("Runneruser", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        int night_mode = sharedPreferences.getInt("night_mode", 0);
        if (night_mode == 1) {
            setTheme(R.style.DarkTheme);
            nightMode.NightMode(R.color.white, MainActivity.this);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        runnerService = new RunnerService();
        inicialize();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED) {
            requestPermissions(new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, 0);
        }
        try {
            BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_main);
            bottomNavigationView.setSelectedItemId(R.id.home_toolbarlist);
            bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    try {
                        switch (item.getItemId()) {
                            case R.id.setting_toolbarlist:
                                Intent intentgraph = new Intent(MainActivity.this, SettingActivity.class);
                                startActivity(intentgraph);
                                overridePendingTransition(0, 0);
                                return true;
                            case R.id.home_toolbarlist:
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

        running = sharedPreferences.getInt("run", 0);
        try {
            if (running == 1) {
                RestartBtn.setVisibility(View.GONE);
                resumeapp();
                chronometer.start();

            } else if (running == 2) {
                RestartBtn.setVisibility(View.VISIBLE);
                PauzeBtn.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.blue));
                PauzeBtn.setText("RESUME");
                resumeapp();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            runtime = sharedPreferences.getBoolean("runtime", false);
            StartBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!runtime) {
                        RestartLayout.setVisibility(View.VISIBLE);
                        RestartBtn.setVisibility(View.GONE);
                        StartBtn.setVisibility(View.GONE);
                        chronometer.setBase(SystemClock.elapsedRealtime() - pauze);
                        chronometer.start();
                        runtime = true;
                        editor.putInt("step_distance", 0);
                        editor.putBoolean("runtime", true);
                        editor.putInt("run", 1);
                        editor.commit();
                        startService(new Intent(MainActivity.this, RunnerService.class));
                        starttimer();
                    }

                }
            });
            PauzeBtn.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("ResourceAsColor")
                @Override
                public void onClick(View view) {
                        if (!runtime) {
                            long elapsedMillis = sharedPreferences.getLong("sec_clock", 0);
                            if (running == 2) {
                                chronometer.setBase(SystemClock.elapsedRealtime() - elapsedMillis);
                            } else {
                                chronometer.setBase(SystemClock.elapsedRealtime() - pauze);
                            }
                            chronometer.start();
                            PauzeBtn.setText("STOP");
                            RestartBtn.setVisibility(View.GONE);
                            PauzeBtn.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.red));
                            runtime = true;
                            editor.putBoolean("runtime", true);
                            editor.putInt("run", 1);
                            starttimer();
                        } else {
                            chronometer.stop();
                            pauze = SystemClock.elapsedRealtime() - chronometer.getBase();
                            PauzeBtn.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.blue));
                            PauzeBtn.setText("RESUME");
                            RestartBtn.setVisibility(View.VISIBLE);
                            runtime = false;
                            editor.putBoolean("runtime", false);
                            editor.putInt("run", 2);
                            if (timer != null) {
                                timer.cancel();
                                timer.purge();
                                timer = null;
                            }
                        }
                        editor.commit();
                }
            });
            RestartBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!runtime) {
                        PauzeBtn.setText("STOP");
                        PauzeBtn.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.red));
                        RestartLayout.setVisibility(View.GONE);
                        StartBtn.setVisibility(View.VISIBLE);
                        chronometer.setBase(SystemClock.elapsedRealtime());
                        pauze = 0;
                        StepTxt.setText("" + 0);
                        DistanceTxt.setText("" + 0 + " m");
                        SpeedTxt.setText("" + 0.0 + "  km/h");
                        editor.putInt("distance_step", 0);
                        editor.putBoolean("runtime", false);
                        editor.putInt("run", 0);
                        editor.commit();
                        stopService(new Intent(MainActivity.this, RunnerService.class));
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(MainActivity.this, MainActivity.class));
    }

    private void starttimer() {
        try {
            timer = new Timer();
            TimerTask t = new TimerTask() {
                @Override
                public void run() {
                    if (runtime) {
                        elapsedMillis = SystemClock.elapsedRealtime() - chronometer.getBase();
                        editor.putLong("sec_clock", elapsedMillis);
                        editor.commit();
                    }
                    showdata(elapsedMillis);
                    editor.commit();
                }

            };
            timer.scheduleAtFixedRate(t, 0, 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showdata(long elapsedMillis) {
        double sec = ((int) elapsedMillis + 1) / 1000;
        int steps = sharedPreferences.getInt("distance_step", 0);
        StepTxt.setText("" + steps);
        DistanceTxt.setText("" + (int) (steps * 0.762) + " m");
        double speed = steps * 0.762 / 1000 * 3600 / (sec + 0.1);
        SpeedTxt.setText("" + String.format(" %.1f", speed) + "  km/h");
    }

    private void resumeapp() {
        RestartLayout.setVisibility(View.VISIBLE);
        StartBtn.setVisibility(View.GONE);
        elapsedMillis = sharedPreferences.getLong("sec_clock", 0);
        showdata(elapsedMillis);
        chronometer.setBase(SystemClock.elapsedRealtime() - elapsedMillis);
    }

    private void inicialize() {
        StartBtn = findViewById(R.id.startBtn);
        RestartBtn = findViewById(R.id.restartBtn);
        PauzeBtn = findViewById(R.id.pauzeBtn);
        RestartLayout = findViewById(R.id.restartLayout);
        chronometer = findViewById(R.id.Chronometer);
        StepTxt = findViewById(R.id.StepsText);
        DistanceTxt = findViewById(R.id.DistanceText);
        SpeedTxt = findViewById(R.id.SpeedText);
    }
}