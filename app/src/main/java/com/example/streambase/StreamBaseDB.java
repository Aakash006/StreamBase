package com.example.streambase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;


public class StreamBaseDB extends SQLiteOpenHelper {
    private static final String TAG = "StreamBaseDB";

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "streambase.db";
    public static final String TABLE_NAME = "FAVORITES";


    public StreamBaseDB(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME + "(" +
                "id INTEGER PRIMARY KEY," +
                "name Text NOT NULL," +
                "img_id Text Not NULL," +
                "providers Text);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS FAVORITES;");
    }

    public void addRecord(int id, String mediaName, String imgId, ArrayList<String> providers) {
        Log.d(TAG, "addRecord: " + id);
        Log.d(TAG, "addRecord: " + mediaName);
        Log.d(TAG, "addRecord: " + imgId);
        Log.d(TAG, "addRecord: " + providers.toString());
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", id);
        contentValues.put("name", mediaName);
        contentValues.put("img_id", imgId);
        contentValues.put("providers", providers.toString().replace("[", "").replace("]", ""));
        sqLiteDatabase.insert(TABLE_NAME, "providers", contentValues);
    }

    public Cursor getFavourites() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT name, img_Id, providers FROM " + TABLE_NAME;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

}
