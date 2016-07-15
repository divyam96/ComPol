package org.janaagraha.cp_jccd;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
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
        qb.setTables(sqlTables);
        Cursor c = qb.query(db, sqlSelect, selection, null,
                null, null, null);

        c.moveToFirst();
        return c;

    }
    public void insertBeat(HashMap<String, String> queryValues) {
        SQLiteDatabase database = this.getWritableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables("BeatList");
        String [] sqlSelect = {"0 _id","Beat_Number","Stations"};
        Cursor checker = qb.query(database,sqlSelect,null,null,null,null,null);
        ArrayList<String> checkArrayStations = new ArrayList<>();
        ArrayList<String> checkArray = new ArrayList<>();
        for(checker.moveToFirst();!checker.isAfterLast();checker.moveToNext()){
            checkArray.add(checker.getString(1));
        }
        for(checker.moveToFirst();!checker.isAfterLast();checker.moveToNext()){
            checkArrayStations.add(checker.getString(2));
        }

        for(int i=1;i<=checker.getCount();i++){
           if((checkArray.get(i-1).equals(queryValues.get("BeatNumber").trim())) && !(checkArrayStations.get(i-1).contains(queryValues.get("stationsName")))) {
               String str = checkArrayStations.get(i - 1).concat("," + queryValues.get("stationsName"));
               database.execSQL("UPDATE BeatList SET Stations = \"" + str + "\"" + " WHERE rowid = " + i);
           }
        }

        database.close();
    }
}
