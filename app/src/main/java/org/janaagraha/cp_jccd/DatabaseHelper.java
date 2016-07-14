package org.janaagraha.cp_jccd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.HashMap;

public class DatabaseHelper extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "cpStationList.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public Cursor getStations() {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String [] sqlSelect = {"0 _id", "Police_Station_Name"};
        String sqlTables = "policestationscp";

        qb.setTables(sqlTables);
        Cursor c = qb.query(db, sqlSelect, null, null,
                null, null, null);

        c.moveToFirst();
        return c;

    }
    public void insertStation(HashMap<String, String> queryValues) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        //values.put("userId", queryValues.get("userId"));
        values.put("Police_Station_Name", queryValues.get("stationsName"));
        values.put("No_of_Beats",queryValues.get("BeatNumber"));
        database.insert("policestationscp", null, values);
        database.close();
    }


}