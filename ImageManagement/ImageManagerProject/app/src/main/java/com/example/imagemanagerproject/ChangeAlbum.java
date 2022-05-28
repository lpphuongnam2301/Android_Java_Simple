package com.example.imagemanagerproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;

public class ChangeAlbum extends AppCompatActivity {
    Spinner spinner;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_album);
        spinner = (Spinner) findViewById(R.id.spinner);
        button = (Button) findViewById(R.id.buttonSpinner);

        ArrayList<String> items = MainActivity.database.getDataAlbum("SELECT * FROM Album");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        spinner.setAdapter(adapter);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String item = spinner.getSelectedItem().toString();
                Intent intent = getIntent();
                String time = intent.getStringExtra("time");
                String query = "UPDATE Image_Info SET album = '"+item+"' WHERE time_image = '"+time+"'";
                MainActivity.database.queryData(query);
                finish();
            }
        });
    }
}