package org.janaagraha.cp_jccd;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.client.Firebase;

import java.util.ArrayList;

public class StationActivity extends AppCompatActivity {
    public static String Station;
    private Cursor stations;
    private DatabaseHelper db;
    public static final String MY_PREFS_NAME = "MyPrefsFile";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station);
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME,MODE_PRIVATE);
        Station = prefs.getString("Station",null);
        Spinner StationView = (Spinner) findViewById(R.id.Spinner_Station);

        db = new DatabaseHelper(this);
        stations = db.getStations();

        ArrayList<String> stationsArray = new ArrayList<>();
        for(stations.moveToFirst();!stations.isAfterLast();stations.moveToNext()){
            stationsArray.add(stations.getString(1));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, stationsArray);
        StationView.setAdapter(adapter);
        StationView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Station = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Firebase.setAndroidContext(this);
        Button toScreen1 = (Button) findViewById(R.id.button_main);
        assert toScreen1 != null;
        toScreen1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "You have Selected: " + Station,
                        Toast.LENGTH_LONG).show();
                SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME,MODE_PRIVATE).edit();
                editor.putString("Station",Station);
                editor.commit();
                Intent intent_to_screen1 = new Intent(view.getContext(), BeatActivity.class);
                startActivity(intent_to_screen1);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
