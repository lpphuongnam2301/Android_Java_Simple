package com.example.alarmandroidproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class AddTimeActivity extends AppCompatActivity implements View.OnClickListener{
    TimePicker timePicker;
    Button btnSave;
    ArrayList<String> arrTime;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addtime);
        declareComponent();

    }

    public void declareComponent()
    {
        timePicker = (TimePicker) findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);
        sharedPreferences = getSharedPreferences("arr_list_storage", MODE_PRIVATE);
        arrTime = new ArrayList<>
                (Arrays.asList(sharedPreferences.getString("arrListTime", "22:22")
                        .split(";")));
    }

    public String formatTimeToString(String hour, String minute)
    {
        if(Integer.parseInt(hour) < 10)
        {
            hour = "0" + hour;
        }
        if(Integer.parseInt(minute) < 10)
        {
            minute = "0" + minute;
        }
        return hour + ":" + minute;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btnSave)
        {
            boolean check = false;
            String newTime = formatTimeToString(String.valueOf(timePicker.getHour()), String.valueOf(timePicker.getMinute()));
            for(String str : arrTime)
            {
                if(str.equals(newTime))
                {
                    check = true;
                    break;
                }
            }
            if(check)
            {
                Toast.makeText(AddTimeActivity.this, "Trùng với giờ đã có.", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent();
                intent.putExtra("newTime", newTime);
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }
}