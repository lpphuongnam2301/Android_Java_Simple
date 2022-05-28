package com.example.alarmandroidproject;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ListTimeAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<String> arrTime;
    SharedPreferences sharedPreferences;

    public ListTimeAdapter(Context context, int layout, ArrayList<String> arrTime, SharedPreferences sharedPreferences)
    {
        this.context = context;
        this.layout = layout;
        this.arrTime = arrTime;
        this.sharedPreferences = sharedPreferences;
    }

    @Override
    public int getCount() {
        return arrTime.size();
    }

    @Override
    public Object getItem(int i) {
        return arrTime.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(layout, null);

        TextView txtTime = (TextView) view.findViewById(R.id.txtTime);
        TextView btnDelete = (TextView) view.findViewById(R.id.btnDelete);


        txtTime.setText(arrTime.get(i));
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrTime.remove(i);
                notifyDataSetChanged();
                addToSharePre(arrTime);
            }
        });

        return view;
    }

    public void addToSharePre(ArrayList<String> arr)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("arrListTime", MainActivity.formatListToString(arr));
        editor.commit();
    }

}
