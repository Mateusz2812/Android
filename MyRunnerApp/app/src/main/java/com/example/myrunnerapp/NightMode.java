package com.example.myrunnerapp;

import android.content.Context;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

public class NightMode {
    public void NightMode(int color, Context context){
        DrawableCompat.setTint(
                DrawableCompat.wrap(AppCompatResources.getDrawable(context, R.drawable.ic_section)),
                ContextCompat.getColor(context, color)
        );
        DrawableCompat.setTint(
                DrawableCompat.wrap(AppCompatResources.getDrawable(context, R.drawable.ic_steps1)),
                ContextCompat.getColor(context, color)
        );
        DrawableCompat.setTint(
                DrawableCompat.wrap(AppCompatResources.getDrawable(context, R.drawable.ic_time)),
                ContextCompat.getColor(context, color)
        );
        DrawableCompat.setTint(
                DrawableCompat.wrap(AppCompatResources.getDrawable(context, R.drawable.ic_baseline_speed_24)),
                ContextCompat.getColor(context,color)
        );
    }
}
