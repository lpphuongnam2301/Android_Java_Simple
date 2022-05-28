package com.example.imagemanagerproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.contract.ActivityResultContracts;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class Adapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<ImageClass> arrayList;
    private ArrayList<String> arrayListAlbum;
    private int activity; //phan biet activity truyen vao

    public Adapter(Context context, int layout, ArrayList<ImageClass> arrayList, int activity)
    {
        this.activity = activity;
        this.context = context;
        this.layout = layout;
        this.arrayList = arrayList;
    }
    public Adapter(Context context, int layout, ArrayList<String> arrayListAlbum)
    {
        this.activity = 2;
        this.context = context;
        this.layout = layout;
        this.arrayListAlbum = arrayListAlbum;
    }

    @Override
    public int getCount() {
        if(activity == 1) {
            return arrayList.size();
        } else {
            return arrayListAlbum.size();
        }
    }

    @Override
    public Object getItem(int i) {
        if(activity == 1) {
            return arrayList.get(i);
        } else {
            return arrayListAlbum.get(i);
        }
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(layout, null);
        if(activity == 1) {
            TextView txtTime = (TextView) view.findViewById(R.id.textViewTime);
            TextView txtDate = (TextView) view.findViewById(R.id.textViewDate);
            ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
            TextView album = (TextView) view.findViewById(R.id.textViewAlbum);
            txtDate.setText(arrayList.get(i).getTime().split(" ")[0]);
            txtTime.setText(arrayList.get(i).getTime().split(" ")[1]);
            album.setText("Album: "+arrayList.get(i).getAlbum());
            byte[] image = arrayList.get(i).getImage();
            Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
            imageView.setImageBitmap(bitmap);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ChangeAlbum.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("time", arrayList.get(i).getTime());
                    context.startActivity(intent);

                }
            });

        } else {
            TextView txtAlbum = (TextView) view.findViewById(R.id.textViewAlbum);
            txtAlbum.setText(arrayListAlbum.get(i));
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("album_name", arrayListAlbum.get(i));
                    context.startActivity(intent);
                }
            });
        }
        return view;
    }

}
