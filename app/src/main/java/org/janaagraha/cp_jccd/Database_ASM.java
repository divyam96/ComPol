package org.janaagraha.cp_jccd;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class Database_ASM extends SQLiteAssetHelper {
    private static final String DATABASE_NAME = "ASMnames.db";
    private static final int DATABASE_VERSION = 1;
    private StationActivity mainObjeact;
    private BeatActivity screen1Object;
    private String beatString;
    private String stationString;

    public Database_ASM(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public Cursor getASM() {
        beatString = screen1Object.Beat;
        stationString = mainObjeact.Station;

        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String [] sqlSelect = {"0 _id", "NAMES"};
        String sqlTables = "ASMtable";
        String selection = "ID LIKE '%"+stationString.trim()+"_"+beatString.trim()+"%'";
        // String[] selectionArgs = {stationString,beatString};

        qb.setTables(sqlTables);
        Cursor c = qb.query(db, sqlSelect, selection, null,
                null, null, null);

        c.moveToFirst();
        return c;

    }
}
