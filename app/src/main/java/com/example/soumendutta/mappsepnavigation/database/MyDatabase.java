package com.example.soumendutta.mappsepnavigation.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by soumendutta on 13/10/16.
 */

public class MyDatabase extends SQLiteOpenHelper
{


    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "taplist";
    public static final String TABLE_NAME = "taplisttable";
    private static final String KEY_ID = "id";
    public static final String ACTIVITY_NAME = "name";
    public static final String ACTIVITY_DESCRIPTION = "description";
    public static final String ENTER_RADIOUS = "radious";
    public static final String UNITS = "units";
    public static final String FROM_DATE = "fromdate";
    public static final String TO_DATE = "todate";
    public static final String TOGG_BUTTON = "togglebutton";
    public static final String LAT= "lat";
    public static final String LONG = "long";
    public static final String MILES= "miles";
    public static final String KILOMETER = "kilometer";
    public static final String METER= "meter";
    public static final String PLAY_PAUSE_STATUS = "status";


    public MyDatabase(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);


        SQLiteDatabase db;
    }
    @Override
    public void onCreate(SQLiteDatabase db)
    {

        String CREATE_LOGIN_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + ENTER_RADIOUS + " TEXT," +
                ACTIVITY_NAME + " TEXT," + ACTIVITY_DESCRIPTION + " TEXT," + FROM_DATE + " TEXT," + TO_DATE + " TEXT," +
                TOGG_BUTTON + " TEXT," + LAT + " TEXT," + LONG + " TEXT," + PLAY_PAUSE_STATUS + " TEXT," + UNITS + " TEXT)" ;

        db.execSQL(CREATE_LOGIN_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME );

    }
    public long insertData(Longtap longtap)
    {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ENTER_RADIOUS,longtap.getRedious());
        contentValues.put(ACTIVITY_NAME,longtap.getName());
        contentValues.put(ACTIVITY_DESCRIPTION, longtap.getDec());
        contentValues.put(FROM_DATE, longtap.getFdate());
        contentValues.put(TO_DATE, longtap.getTdate());
        contentValues.put(TOGG_BUTTON, longtap.getTogglebutton());
        contentValues.put(LAT, longtap.getLat());
        contentValues.put(LONG, longtap.getLog());
        contentValues.put(PLAY_PAUSE_STATUS, longtap.getPlayPauseStatus());
        contentValues.put(UNITS, longtap.getUnits());

        long cnt = db.insert(TABLE_NAME,null,contentValues);
        db.close();
        return cnt;
    }

    public ArrayList<Longtap> getLoginData(){
        ArrayList<Longtap> data = new ArrayList<>();
        String query = "select * from " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);

        if(cursor.moveToFirst())
        {
            do
            {
                Longtap longtap = new Longtap();
                longtap.set_id(cursor.getInt(0));
                longtap.setRedious(cursor.getString(1));
                longtap.setName(cursor.getString(2));
                longtap.setDec(cursor.getString(3));
                longtap.setFdate(cursor.getString(4));
                longtap.setTdate(cursor.getString(5));
                longtap.setTogglebutton(cursor.getString(6));
                longtap.setLat(cursor.getString(7));
                longtap.setLog(cursor.getString(8));
                longtap.setPlayPauseStatus(cursor.getString(9));
                longtap.setUnits(cursor.getString(10));
                data.add(longtap);

            }
            while (cursor.moveToNext());


        }
        cursor.close();
        db.close();

        return data;
    }


    public boolean updateStatus(String lat, String status) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(LAT, lat);
        contentValues.put(PLAY_PAUSE_STATUS, status);

        // updating row
        return db.update(TABLE_NAME, contentValues, LAT + " = ?" , new String[] { lat })>0;
    }

    public boolean updateValues(String lat, Longtap values) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();;
        contentValues.put(ENTER_RADIOUS,values.getRedious());
        contentValues.put(ACTIVITY_NAME,values.getName());
        contentValues.put(ACTIVITY_DESCRIPTION, values.getDec());
        contentValues.put(FROM_DATE, values.getFdate());
        contentValues.put(TO_DATE, values.getTdate());
        contentValues.put(TOGG_BUTTON, values.getTogglebutton());
        contentValues.put(UNITS, values.getUnits());

        return db.update(TABLE_NAME, contentValues, LAT + " = ?" , new String[] { lat })>0;
    }

    public boolean deleteRow(String latitude) {
        SQLiteDatabase db = this.getWritableDatabase();

        return db.delete(TABLE_NAME, LAT + " = ?" , new String[] { latitude }) > 0;
    }

    public boolean updateColumn(String units) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();;
        contentValues.put(UNITS,units);

        return db.update(TABLE_NAME, contentValues, null, null) > 0;

    }

}
