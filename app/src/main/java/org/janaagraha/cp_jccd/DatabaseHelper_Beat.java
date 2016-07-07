package org.janaagraha.cp_jccd;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

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
}
