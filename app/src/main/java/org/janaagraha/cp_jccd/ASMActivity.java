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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

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

public class ASMActivity extends AppCompatActivity {

    public static String OfficerName;
    public static String ASM;
    private Cursor asm;
    private Database_ASM db_ASM;
    public EditText editText_officer;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    HashMap<String, String> queryValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asm);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME,MODE_PRIVATE);
        OfficerName = prefs.getString("OfficerName",null);
        ASM = prefs.getString("ASM",null);
        editText_officer = (EditText)findViewById(R.id.editText_OfficerName);

        Spinner ASMspinner = (Spinner) findViewById(R.id.spinner_ASM);
        db_ASM = new Database_ASM(this);
        asm = db_ASM.getASM();
        asm.moveToFirst();
        ArrayList<String> asmArray = new ArrayList<>();
        for(asm.moveToFirst();!asm.isAfterLast();asm.moveToNext()){
            asmArray.add(asm.getString(1));
        }
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, asmArray);
        ASMspinner.setAdapter(adapter2);
        ASMspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ASM = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void refresh_ASMnames(View view){
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
        client.post("http://10.0.2.2/ASMnames/getasms.php", params, new AsyncHttpResponseHandler() {
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
                    System.out.println(obj.get("asmId"));
                    System.out.println(obj.get("BeatNumber"));
                    System.out.println(obj.get("StationName"));
                    System.out.println(obj.get("asmName"));
                    // DB QueryValues Object to insert into SQLite
                    queryValues = new HashMap<>();
                    queryValues.put("asmId", obj.get("asmId").toString());
                    queryValues.put("BeatNumber", obj.get("BeatNumber").toString());
                    queryValues.put("StationName", obj.get("StationName").toString());
                    queryValues.put("asmName", obj.get("asmName").toString());
                    //queryValues.put("identifier",obj.get("Station").toString().trim()+"_BEAT"+obj.get("BeatNumber").toString().trim());
                    // Insert User into SQLite DB
                    db_ASM.insertASM(queryValues);
                    HashMap<String, String> map = new HashMap<>();
                    // Add status for each User in Hashmap
                    map.put("Id", obj.get("asmId").toString());
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
        client.post("http://10.0.2.2/ASMnames/updatesyncsts.php", params, new AsyncHttpResponseHandler() {
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

    public void MoveToScreen3(View view){
        OfficerName = editText_officer.getText().toString();
        Toast.makeText(getApplicationContext(), "You have Selected ASM: " + ASM + " and Police officers: " + OfficerName,
                Toast.LENGTH_LONG).show();
        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME,MODE_PRIVATE).edit();
        editor.putString("ASM", ASM);
        editor.commit();
        editor.putString("OfficerName",OfficerName);
        editor.commit();
        Intent intent_to_screen3= new Intent(this, TypeActivity.class);
        startActivity(intent_to_screen3);
    }


}
