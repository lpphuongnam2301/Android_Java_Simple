package com.example.imagemanagerproject;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class AlbumActivity extends AppCompatActivity {
    ListView listView;
    ArrayList<String> arrayList;
    Adapter adapter;
    ActivityResultLauncher<Intent> resultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        listView = (ListView) findViewById(R.id.listview_album);
        MainActivity.database.queryData("CREATE TABLE IF NOT EXISTS Album(id INTEGER PRIMARY KEY AUTOINCREMENT, album VARCHAR(50))");
        arrayList = MainActivity.database.getDataAlbum("SELECT * FROM Album");
        adapter = new Adapter(this, R.layout.line_of_album, arrayList);
        listView.setAdapter(adapter);

        getIntentResult();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_album_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.addAlbum)
        {
            Intent intent = new Intent(AlbumActivity.this, AddAlbum.class);
            resultLauncher.launch(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    public void getIntentResult()
    {
        resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            String newAlbum = data.getStringExtra("new_Album");
                            String query = "Insert into Album values (null, '"+newAlbum+"')";
                            //String query = "delete from Album where id = 4";
                            MainActivity.database.queryData(query);
                            arrayList.add(newAlbum);
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
    }
}