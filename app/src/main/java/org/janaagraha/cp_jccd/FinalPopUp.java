package org.janaagraha.cp_jccd;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.firebase.client.Firebase;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by TUSHAR PC on 14-07-2016.
 */
public class FinalPopUp extends finish{
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    public String DateSet;
    public String Station;
    public String Beat;
    public String ASM;
    public String OfficerName;
    public String ActivityType;
    public String Plan;
    public String FinalDateSet;
    public String FinalStation;
    public String FinalBeat;
    public String FinalASM;
    public String FinalOfficerName;
    public String FinalActivityType;
    public String FinalPlan;
    public EditText DateText;
    public EditText StationText;
    public EditText BeatText;
    public EditText OfficerText;
    public EditText ASMText;
    public EditText TypeText;
    public EditText TopicsText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.poplayout);


        DateText = (EditText) findViewById(R.id.editText4);
        StationText = (EditText) findViewById(R.id.editText5);
        BeatText = (EditText) findViewById(R.id.editText6);
        OfficerText = (EditText) findViewById(R.id.editText7);
        ASMText = (EditText) findViewById(R.id.editText8);
        TypeText = (EditText) findViewById(R.id.editText9);
        TopicsText = (EditText) findViewById(R.id.editText10);
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME,MODE_PRIVATE);
        Plan= prefs.getString("TopicsDiscussed", "");
        Station= prefs.getString("Station", "");
        Beat= prefs.getString("Beat", "");
        ASM= prefs.getString("ASM", "");
        OfficerName= prefs.getString("OfficerName", "");
        ActivityType=prefs.getString("ActivityType", "");
        DateSet = prefs.getString("Date","");

        DateText.setText(DateSet);
        StationText.setText(Station);
        BeatText.setText(Beat);
        OfficerText.setText(OfficerName);
        ASMText.setText(ASM);
        TypeText.setText(ActivityType);
        TopicsText.setText(Plan);
    }

    public void Post(View v){
        FinalDateSet=DateText.getText().toString();
        FinalStation=StationText.getText().toString();
        FinalBeat=BeatText.getText().toString();
        FinalOfficerName=OfficerText.getText().toString();
        FinalASM=ASMText.getText().toString();
        FinalActivityType=TypeText.getText().toString();
        FinalPlan=TopicsText.getText().toString();
        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME,MODE_PRIVATE).edit();
        editor.putString("Date",FinalDateSet);
        editor.commit();
        editor.putString("Station", FinalStation);
        editor.commit();
        editor.putString("Beat", FinalBeat);
        editor.commit();
        editor.putString("OfficerName", FinalOfficerName);
        editor.commit();
        editor.putString("ASM", FinalASM);
        editor.commit();
        editor.putString("ActivityType", FinalActivityType);
        editor.commit();
        editor.putString("TopicsDiscussed",FinalPlan);

        editor.putInt("count_images", 0);
        editor.commit();


        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME,MODE_PRIVATE);
        Firebase.setAndroidContext(v.getContext());

        Firebase ref = new Firebase(Config.FIREBASE_URL);
        Firebase DataRef = ref.child("Report");

        Map<String, String> AllData = new HashMap<>();

        AllData.put("Date",prefs.getString("Date",""));
        AllData.put("PoliceStation",prefs.getString("Station", "") );
        AllData.put("Beat",prefs.getString("Beat", "") );
        AllData.put("ASM",prefs.getString("ASM", "") );
        AllData.put("PoliceOfficer",prefs.getString("OfficerName", "") );
        AllData.put("ActivityType",prefs.getString("ActivityType", "") );
        AllData.put("TopicsDiscussed",prefs.getString("TopicsDiscussed", "") );
        AllData.put("PointsOfDiscussionWithASM",prefs.getString("PointsDiscussed", ""));
        AllData.put("Number of Attendees",prefs.getString("NumberOfAttendees", ""));
        AllData.put("Comments", prefs.getString("Comments", ""));

        DataRef.push().setValue(AllData);

        StopGps();
    }

    public void Wait(View v){
        Intent stay = new Intent(v.getContext(),FinalPopUp.class);
        startActivity(stay);
    }
}
