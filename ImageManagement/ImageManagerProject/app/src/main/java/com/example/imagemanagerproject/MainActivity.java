package com.example.imagemanagerproject;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.arch.core.executor.DefaultTaskExecutor;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    final int CAMERA_PERM_CODE = 123;
    ImageView imageView;
    ListView listView;
    ActivityResultLauncher<Intent> resultLauncher;
    public static Database database;
    ArrayList<ImageClass> arrayList;
    Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listview_image);
        imageView = (ImageView) findViewById(R.id.imageView);
        database = new Database(this, "Image_Manager", null, 1);
        database.queryData("CREATE TABLE IF NOT EXISTS Image_Info(id INTEGER PRIMARY KEY AUTOINCREMENT, time_image VARCHAR(50), album VARCHAR(40), image BLOB)");

        if(getIntent().getStringExtra("album_name") == null)
        {
            readData();
        } else {
            readDataAlbum(getIntent().getStringExtra("album_name"));
        }
        getIntentResult();
    }
    public void readDataAlbum(String album)
    {
        arrayList = database.getData("SELECT * FROM Image_Info WHERE album = '"+album+"' ORDER BY time_image DESC");
        adapter = new Adapter(this, R.layout.line_of_image, arrayList, 1);
        listView.setAdapter(adapter);
    }
    public void readData()
    {
        arrayList = database.getData("SELECT * FROM Image_Info ORDER BY time_image DESC");
        adapter = new Adapter(this, R.layout.line_of_image, arrayList, 1);
        listView.setAdapter(adapter);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.openCamera)
        {
            askCameraPermission();
            openCamera();
        }
        if(item.getItemId() == R.id.openAlbum)
        {
            Intent intent = new Intent(this, AlbumActivity.class);
            startActivity(intent);
        }
        if(item.getItemId() == R.id.reset)
        {
            readData();
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
                            Bundle bundle = result.getData().getExtras();
                            Bitmap bitmap = (Bitmap) bundle.get("data");
                            byte[] image = convertBitmapToByteArr(bitmap);

                            Calendar calendar = Calendar.getInstance();
                            ImageClass imageClass = new ImageClass(
                                    getCurrentDateTime(),
                                    "Camera",
                                    image);
                            database.insertData(imageClass);
                            arrayList.add(imageClass);
                            readData();
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
    }
    public String getCurrentDateTime()
    {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }
    public byte[] convertBitmapToByteArr(Bitmap bitmap)
    {
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArray);
        return byteArray.toByteArray();
    }
    public void openCamera()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        resultLauncher.launch(intent);
    }

    public void askCameraPermission()
    {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, CAMERA_PERM_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_PERM_CODE) {
            if (grantResults.length < 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                Toast.makeText(this, "Can cap quyen su dung camera", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}