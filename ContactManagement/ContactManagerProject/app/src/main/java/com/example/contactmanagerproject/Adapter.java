package com.example.contactmanagerproject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class Adapter extends BaseAdapter {
    Context context;
    int layout;
    ArrayList<ContactClass> arrayList;
    public Adapter(Context context, int layout, ArrayList<ContactClass> arrayList)
    {
        this.context = context;
        this.layout = layout;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(layout, null);

        TextView textViewName = (TextView) view.findViewById(R.id.textViewName);
        TextView textViewPhone = (TextView) view.findViewById(R.id.textViewPhone);
        TextView deleteBtn = (TextView) view.findViewById(R.id.deleteBtn);
        textViewName.setText(arrayList.get(i).getName());
        textViewPhone.setText(arrayList.get(i).getNumber());

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteContact(view.getContext(), arrayList.get(i).getNumber(), arrayList.get(i).getName());
                arrayList.remove(i);
                notifyDataSetChanged();
            }
        });

        return view;
    }

    @SuppressLint("Range")
    public void deleteContact(Context context, String phone, String name) {
        Uri contactUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phone));
        Cursor cur = context.getContentResolver().query(contactUri, null, null, null, null);
        try {
            if (cur.moveToFirst())
            {
                do
                {
                    if (cur.getString(cur.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME)).equalsIgnoreCase(name))
                    {
                        String lookupKey = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
                        Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_LOOKUP_URI, lookupKey);
                        context.getContentResolver().delete(uri, null, null);
                    }

                } while (cur.moveToNext());
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
