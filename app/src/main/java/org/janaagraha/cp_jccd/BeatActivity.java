package org.janaagraha.cp_jccd;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.client.Firebase;

import java.util.ArrayList;

public class BeatActivity extends AppCompatActivity {

    public static String Beat;
    private DatabaseHelper_Beat db_beat;
    private Cursor beats;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME,MODE_PRIVATE);
        Beat = prefs.getString("Beat",null);


        Spinner BeatView = (Spinner) findViewById(R.id.Spinner_Beat);
        db_beat = new DatabaseHelper_Beat(this);
        beats = db_beat.getBeats();
        beats.moveToFirst();
        ArrayList<String> beatArray = new ArrayList<String>();
        for(beats.moveToFirst();!beats.isAfterLast();beats.moveToNext()){
            beatArray.add(beats.getString(1));
        }
        /*
        SimpleCursorAdapter adapter1 = new SimpleCursorAdapter(this,
                android.R.layout.simple_spinner_item, beats,
                new String[] {"Beat_Number"},
                new int[] {android.R.id.text1});
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        */
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, beatArray);
        BeatView.setAdapter(adapter1);
        BeatView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Beat = parent.getItemAtPosition(position).toString();
                SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME,MODE_PRIVATE).edit();
                editor.putString("Beat",Beat);
                editor.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Firebase.setAndroidContext(this);
        Button toScreen2 = (Button) findViewById(R.id.button_screen1);
        assert toScreen2 != null;
        toScreen2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "You have Selected: " + Beat,
                        Toast.LENGTH_LONG).show();
                Intent intent_to_screen2 = new Intent(view.getContext(), ASMActivity.class);
                startActivity(intent_to_screen2);
            }
        });

    }

}
