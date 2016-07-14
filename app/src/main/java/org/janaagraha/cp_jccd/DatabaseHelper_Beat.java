package org.janaagraha.cp_jccd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.HashMap;

public class DatabaseHelper_Beat extends SQLiteAssetHelper {
    private static final String DATABASE_NAME = "BeatAreaList.db";
    private static final int DATABASE_VERSION = 1;
    private StationActivity mainObject;
    private String stationString;

    public DatabaseHelper_Beat(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public Cursor getBeats() {
        stationString = mainObject.Station;
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        String [] sqlSelect = {"0 _id", "Beat_Number"};
        String sqlTables = "BeatList";
        String selection = "Stations LIKE '%"+stationString.trim()+"%'";
        // String[] selectionArgs = {MainActivity.Station};

        qb.setTables(sqlTables);
        Cursor c = qb.query(db, sqlSelect, selection, null,
                null, null, null);

        c.moveToFirst();
        return c;

    }
    public void insertBeat(HashMap<String, String> queryValues) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("BeatNumber", queryValues.get("BeatNumber"));
        values.put("Stations", queryValues.get("stationsName"));
        //database.insert("BeatList", null, values);
        values.get("Stations");
        Cursor beats = getBeats();
        for(beats.moveToFirst();!beats.isAfterLast();beats.moveToNext()){
            if(beats.getString(0).equals(values.get("BeatNumber"))){
                database.update("BeatList",values,beats.getString(1)+","+values.get("Stations"),null);
            }
        }

        //database.update("BeatList",values,"")
        database.close();
    }
}
