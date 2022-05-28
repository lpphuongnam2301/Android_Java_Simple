package com.example.alarmandroidproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.util.Calendar;

public class AlarmClock extends AppCompatActivity {
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_clock);

        textView = (TextView) findViewById(R.id.textView);
        textView.setText(getIntent().getStringExtra("timetime"));
    }
}