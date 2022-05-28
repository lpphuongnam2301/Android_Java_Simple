package com.example.alarmandroidproject;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    ListView lvTime;
    ArrayList<String> arrTime;
    ListTimeAdapter adapterTime;
    ActivityResultLauncher<Intent> resultLauncher;
    SharedPreferences sharedPreferences;
    static AlarmManager alarmManager;
    static ArrayList<PendingIntent> arrPendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        declareComponent();
        adapterTime = new ListTimeAdapter(MainActivity.this, R.layout.line_of_listview_time, arrTime, sharedPreferences);
        lvTime.setAdapter(adapterTime);
        getActivityResult();
    }

    public void getActivityResult()
    {
        resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            String newTime = data.getStringExtra("newTime");
                            arrTime.add(newTime);
                            addToSharePre(arrTime);
                            adapterTime.notifyDataSetChanged();
                            setAlarmTime(newTime);
                        }
                    }
                });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.addBtn)
        {
            Intent intent = new Intent(MainActivity.this, AddTimeActivity.class);
            resultLauncher.launch(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    public void addToSharePre(ArrayList<String> arr)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("arrListTime", formatListToString(arr));
        editor.commit();
    }
    public void declareComponent()
    {
        lvTime = (ListView) findViewById(R.id.listviewTime);
        sharedPreferences = getSharedPreferences("arr_list_storage", MODE_PRIVATE);
        arrTime = new ArrayList<>
                (Arrays.asList(sharedPreferences.getString("arrListTime", "22:22;")
                        .split(";")));
        arrPendingIntent = new ArrayList<>();
    }

    public static String formatListToString(ArrayList<String> arr)
    {
        String str = "";
        for(String s : arr)
        {
            str += s + ";";
        }
        return str;
    }

    public void setAlarmTime(String time)
    {
        String temp = time.split(":")[0] + time.split(":")[1];
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time.split(":")[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(time.split(":")[1]));
        Intent alarmIntent = new Intent(MainActivity.this, AlarmReceiver.class);
        alarmIntent.putExtra("time_clock", time);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                MainActivity.this, Integer.parseInt(temp)
                , alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT
        );
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY
                , pendingIntent);
    }
}