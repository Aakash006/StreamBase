package com.example.streambase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.streambase.model.TMDB;

import java.util.HashMap;
import java.util.List;


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
                "media_type Text NOT NULL," +
                "original_name Text," +
                "original_title Text," +
                "poster_path Text Not NULL," +
                "providers Text);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS FAVORITES;");
    }

    public void addRecord(TMDB media, List<String> providers) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", media.getId());
        contentValues.put("media_type", media.getMediaType());
        contentValues.put("original_name", media.getTvShowName());
        contentValues.put("original_title", media.getMovieName());
        contentValues.put("poster_path", media.getImageURL());
        contentValues.put("providers", providers.toString().replace("[", "").replace("]", ""));
        sqLiteDatabase.insert(TABLE_NAME, "providers", contentValues);
    }

    public HashMap<TMDB, String> getAllFavoriteMedia() {
        HashMap<TMDB, String> result = new HashMap<>();

        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME + ";", null);

        while (cursor.moveToNext()) {
            result.put(new TMDB(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4)), cursor.getString(5));
        }
        cursor.close();
        return result;
    }

    public boolean isFavourite(int id) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME
                + " WHERE id=" + id + ";",null);
        int rowsCount= cursor.getCount();
        cursor.close();
        return rowsCount > 0;
    }

    public boolean removeFavourite(int id) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.delete(TABLE_NAME, "id=" + id, null) > 0;
    }
}
