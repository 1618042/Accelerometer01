package com.example.accelerometer;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class OpenHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Test01.db";
    private static final String TABLE_NAME = "Test01db";
    private static final String _ID = "_id";
    private static final String time = "time";
    private static final String x_axis = "x_axis";
    private static final String y_axis = "y_axis";
    private static final String z_axis = "z_axis";
    private static final String latitude = "latitude";
    private static final String longitude = "longitude";
    private static final String SQL_CREATE_ENTRIES = " CREATE TABLE "+ TABLE_NAME + " ("+ _ID + " INTEGER PRIMARY KEY, " + time+ " varchar255,"+ x_axis+ " Double, "+ y_axis+" Double, "+ z_axis +" Double," + latitude +" Double, "+ longitude +" Double )";
    private static final String SQL_DERETE_ENTRIES = " DROP TABLE IF EXISTS " + TABLE_NAME;

    OpenHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db){
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL(SQL_DERETE_ENTRIES);
        onCreate(db);
    }
}
