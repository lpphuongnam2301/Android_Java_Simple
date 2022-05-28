package com.example.contactmanagerproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    ArrayList<ContactClass> arrayListContact;
    Adapter adapter;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listView);
        arrayListContact = new ArrayList<>();
        checkPermission();
    }

    public void exportCSV() {
        try {
        File path = MainActivity.this.getFilesDir();
        File file = new File(path, "export.csv");
        FileOutputStream stream = new FileOutputStream(file);
        for (ContactClass contactClass : arrayListContact)
        {
            String line = contactClass.getName() + "," + contactClass.getNumber() + "\n";
            stream.write(line.getBytes(StandardCharsets.UTF_8));
        }

        } catch (Exception e)
        {}
    }

    public void importCSV()
    {
        try
        {
        File path = MainActivity.this.getFilesDir();
        File file = new File(path, "import.csv");
        int length = (int) file.length();
        byte[] bytes = new byte[length];

        FileInputStream in = new FileInputStream(file);

        in.read(bytes);
        String contents = new String(bytes);
        Scanner scanner = new Scanner(contents);
        while(scanner.hasNextLine())
        {
            String line = scanner.nextLine();
            ContactClass contactClass = new ContactClass();
            contactClass.setName(line.split(",")[0]);
            contactClass.setNumber(line.split(",")[1]);
            arrayListContact.add(contactClass);
        }
        } catch (Exception e)
        {

        }

    }
    public void checkPermission()
    {
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_CONTACTS}, 100);
        } else if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_CONTACTS}, 100);
        } else {
            getContactList();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.synContact)
        {
            synContact();
            adapter.notifyDataSetChanged();
        }
        if (item.getItemId() == R.id.importCSV)
        {
            importCSV();
            adapter.notifyDataSetChanged();
        }
        if (item.getItemId() == R.id.exportCSV)
        {
            exportCSV();
            adapter.notifyDataSetChanged();
        }

        return super.onOptionsItemSelected(item);
    }
    public void synContact()
    {
        for (int i=0; i<arrayListContact.size()-1; i++)
        {
           for (int j=i+1; j<arrayListContact.size(); j++)
           {
               if(arrayListContact.get(i).getNumber().equals(arrayListContact.get(j).getNumber()))
               {
                   arrayListContact.get(i).setName(arrayListContact.get(i).getName() + "+" + arrayListContact.get(j).getName());
                   arrayListContact.remove(j);
                   j--;//tra ve j de k bi luot bo 1 gia tri do vua remove
               }
           }
        }
    }
    private void getContactList()
    {
        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        String sort = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC";
        Cursor cursor = getContentResolver().query(
                uri, null, null, null, sort
        );
        if(cursor.getCount() > 0)
        {
            while (cursor.moveToNext())
            {
                @SuppressLint("Range") String id = cursor.getString(cursor.getColumnIndex(
                        ContactsContract.Contacts._ID
                ));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME
                ));

                Uri uriPhone = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
                String selection = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " =?";

                Cursor phoneCursor = getContentResolver().query(
                        uriPhone, null, selection, new String[]{id}, null
                );

                if(phoneCursor.moveToNext())
                {
                    @SuppressLint("Range") String number = phoneCursor.getString(phoneCursor.getColumnIndex(
                            ContactsContract.CommonDataKinds.Phone.NUMBER
                    ));

                    ContactClass contactClass = new ContactClass();
                    contactClass.setName(name);
                    contactClass.setNumber(number);
                    arrayListContact.add(contactClass);
                    phoneCursor.close();
                }
            }
            cursor.close();
            adapter = new Adapter(this, R.layout.line_of_list, arrayListContact);
            listView.setAdapter(adapter);
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
            getContactList();
        } else {
            Toast.makeText(this, "Can cap quyen truy cap va chinh sua danh ba", Toast.LENGTH_SHORT).show();
        }
    }
}