package org.janaagraha.cp_jccd;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;



public class finish extends AppCompatActivity {

    public static final String MY_PREFS_NAME = "MyPrefsFile";
    public static final String MY_PREFS_NAME2 = "MyGpsFile";
    public static String NumberOfStudents;
    public static String NumberOfCitizens;
    public static String Comments;
    public EditText editText;
    public EditText editText1;
    public EditText editText2;
    public String DateSet;
    public String Station;
    public String Beat;
    public String ASM;
    public String OfficerName;
    public String ActivityType;
    public String Plan;
    public String PointsDiscussed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("FirebaseUI: ", "New activity");
        setContentView(R.layout.finishing_activity);
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME,MODE_PRIVATE);
        NumberOfStudents = prefs.getString("NumberOfStudents", null);
        NumberOfCitizens = prefs.getString("NumberOfCitizens", null);
        Comments = prefs.getString("Comments",null);
        editText = (EditText) findViewById(R.id.editText2);
        editText1 = (EditText) findViewById(R.id.editText);
        editText2 = (EditText) findViewById(R.id.editText11);

    }

    public void finish_event(View v){


        NumberOfStudents = editText.getText().toString();
        NumberOfCitizens = editText2.getText().toString();
        Comments = editText1.getText().toString();

        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME,MODE_PRIVATE).edit();
        editor.putString("NumberOfStudents", NumberOfStudents);
        editor.commit();
        editor.putString("NumberOfCitizens", NumberOfCitizens);
        editor.commit();
        editor.putString("Comments", Comments);
        editor.commit();


        editor.putInt("count_images", 0);
        editor.commit();


        Intent popIntent = new Intent(v.getContext(),FinalPopUp.class);
        startActivity(popIntent);

    }



    protected void StopGps(){


        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME2, MODE_PRIVATE).edit();
        editor.putBoolean("GpsFlag", false);
        editor.commit();

        startActivity(new Intent(this, GpsTrackerActivity.class));
    }

}
