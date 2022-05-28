package com.example.imagemanagerproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddAlbum extends AppCompatActivity {

    EditText editText;
    Button buttonSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_album);
        editText = (EditText) findViewById(R.id.editText);
        buttonSave = (Button) findViewById(R.id.button);

        if(!editText.getText().equals("")) {
            buttonSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String str = editText.getText().toString();
                    Intent intent = new Intent();
                    intent.putExtra("new_Album", str);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });
        }
    }
}