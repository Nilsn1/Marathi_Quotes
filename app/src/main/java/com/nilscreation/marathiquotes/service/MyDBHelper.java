package com.nilscreation.marathiquotes.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.nilscreation.marathiquotes.model.QuoteModel;

import java.util.ArrayList;

public class MyDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "MyDatabase";
    private static final int DATABASE_VERSION = 1;
    private static final String MY_TABLE = "Table1";
    private static final String KEY_URL = "url";
    private static final String KEY_CATEGORY = "category";
    private static final String KEY_TITLE = "title";
    private static final String KEY_TEXT = "text";

    Context context;

    public MyDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String query = "CREATE TABLE " + MY_TABLE +
                " (" + KEY_TITLE + " TEXT) ";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldversion, int newversion) {

        db.execSQL("DROP TABLE IF EXISTS " + MY_TABLE);
        onCreate(db);
    }

    public void addData(String title) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, title);
        db.insert(MY_TABLE, null, values);

        db.close();
    }

    public ArrayList<QuoteModel> readData() {

        SQLiteDatabase db = this.getReadableDatabase();

        //Cursor cursor = db.rawQuery("SELECT * FROM " + MY_TABLE + " ORDER BY " + KEY_TITLE + " DESC", null);
        Cursor cursor = db.rawQuery("SELECT * FROM " + MY_TABLE, null);

        ArrayList<QuoteModel> facts = new ArrayList<>();

        while (cursor.moveToNext()) {

            facts.add(new QuoteModel(cursor.getString(0), cursor.getString(1)));
        }
        return facts;
    }

    public void deleteData(String title) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(MY_TABLE, KEY_TITLE + " = ? ", new String[]{title});
    }

    public void deleteandAdd(String title) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(MY_TABLE, KEY_TITLE + " = ? ", new String[]{title});
        addData(title);
    }
}
