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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;

public class BeatActivity extends AppCompatActivity {

    public static String Beat;
    private DatabaseHelper_Beat db_beat;
    private Cursor beats;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    HashMap<String, String> queryValues;

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

    public void refresh_BeatAreaList(View view){
        syncSQLiteMySQLDB();
    }

    // Method to Sync MySQL to SQLite DB
    public void syncSQLiteMySQLDB() {
        // Create AsycHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        // Http Request Params Object
        RequestParams params = new RequestParams();
        // Show ProgressBar
        // prgDialog.show();
        // Make Http call to getusers.php
        client.post("http://10.0.2.2/BeatAreaList/getbeats.php", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody)   {
                // Hide ProgressBar
                // prgDialog.hide();
                // Update SQLite DB with response sent by getusers.php
                try {
                    String response = new String(responseBody, "UTF-8");
                    updateSQLite(response);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            }

            // When error occured
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                // TODO Auto-generated method stub
                // Hide ProgressBar
                //prgDialog.hide();
                if (statusCode == 404) {
                    Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                } else if (statusCode == 500) {
                    Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet]",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void updateSQLite(String response){
        ArrayList<HashMap<String, String>> usersynclist;
        usersynclist = new ArrayList<>();
        // Create GSON object
        Gson gson = new GsonBuilder().create();
        try {
            // Extract JSON array from the response
            JSONArray arr = new JSONArray(response);
            System.out.println(arr.length());
            // If no of array elements is not zero
            if(arr.length() != 0){
                // Loop through each array element, get JSON object which has userid and username
                for (int i = 0; i < arr.length(); i++) {
                    // Get JSON object
                    JSONObject obj = (JSONObject) arr.get(i);
                    System.out.println(obj.get("BeatNumber"));
                    System.out.println(obj.get("stationsName"));
                    // DB QueryValues Object to insert into SQLite
                    queryValues = new HashMap<>();
                    queryValues.put("beatId", obj.get("beatId").toString());
                    queryValues.put("BeatNumber",obj.get("BeatNumber").toString());
                    // Add userName extracted from Object
                    queryValues.put("stationsName", obj.get("stationsName").toString());
                    // queryValues.put("BeatNumber",obj.get("BeatNumber").toString());
                    // Insert User into SQLite DB
                   // db_beat.insertBeat(queryValues);
                    HashMap<String, String> map = new HashMap<>();
                    // Add status for each User in Hashmap
                    map.put("Id", obj.get("beatId").toString());
                    map.put("status", "1");
                    usersynclist.add(map);
                }
                // Inform Remote MySQL DB about the completion of Sync activity by passing Sync status of Users
                updateMySQLSyncSts(gson.toJson(usersynclist));
                // Reload the Main Activity
                reloadActivity();
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // Method to inform remote MySQL DB about completion of Sync activity
    public void updateMySQLSyncSts(String json) {
        System.out.println(json);
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("syncsts", json);
        // Make Http call to updatesyncsts.php with JSON parameter which has Sync statuses of Users
        client.post("http://10.0.2.2/BeatAreaList/updatesyncsts.php", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Toast.makeText(getApplicationContext(), "MySQL DB has been informed about Sync activity", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getApplicationContext(), "Error Occured", Toast.LENGTH_LONG).show();
            }

        });
    }

    // Reload MainActivity
    public void reloadActivity() {
        Intent objIntent = new Intent(getApplicationContext(), StationActivity.class);
        startActivity(objIntent);
    }

}
