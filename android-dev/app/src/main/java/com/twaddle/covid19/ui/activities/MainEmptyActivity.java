package com.twaddle.covid19.ui.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.twaddle.covid19.utils.PrefManager;

public class MainEmptyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent activityIntent;

        if (new PrefManager(getApplicationContext()).isFirstTimeLaunch()) {
//TODO: change this to welcome activity
            activityIntent = new Intent(this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        } else {
            activityIntent = new Intent(this, MainActivity.class);
        }

        startActivity(activityIntent);
        finish();
    }
}
