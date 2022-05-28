package com.example.imagemanagerproject;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.media.Image;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class Database extends SQLiteOpenHelper {


    public Database(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public void queryData(String sql)
    {
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql);
    }
    public void insertData(ImageClass imageClass)
    {
        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO Image_Info VALUES(null, ?, ?, ?)";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.bindString(1, imageClass.getTime());
        statement.bindString(2, imageClass.getAlbum());
        statement.bindBlob(3, imageClass.getImage());
        statement.executeInsert();
    }
    public ArrayList<String> getDataAlbum(String sql)
    {
        ArrayList<String> arrayList = new ArrayList<>();
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            arrayList.add(cursor.getString(1));
        }
        return arrayList;
    }
    public ArrayList<ImageClass> getData(String sql)
    {
        ArrayList<ImageClass> arrayList = new ArrayList<>();
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            arrayList.add(new ImageClass(
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getBlob(3)
            ));
        }
        return arrayList;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
