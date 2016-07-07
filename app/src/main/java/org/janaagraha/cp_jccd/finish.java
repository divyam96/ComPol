package org.janaagraha.cp_jccd;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.firebase.client.Firebase;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by divyam on 3/7/16.
 */

public class finish extends AppCompatActivity {

    public static final String MY_PREFS_NAME = "MyPrefsFile";
    public static final String MY_PREFS_NAME2 = "MyGpsFile";
    public static String NumberOfAttendees;
    public static String Comments;
    public EditText editText;
    public EditText editText1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("FirebaseUI: ", "New activity");
        setContentView(R.layout.finishing_activity);

        editText = (EditText) findViewById(R.id.editText2);
        editText1 = (EditText) findViewById(R.id.editText);


    }

public void finish_event(View v){

    NumberOfAttendees = editText.getText().toString();
    Comments = editText1.getText().toString();
    Firebase ref = new Firebase(Config.FIREBASE_URL);


    Firebase DataRef = ref.child("Report");

    Map<String, String> AllData = new HashMap<>();
    AllData.put("PoliceStation", StationActivity.Station);
    AllData.put("Beat", BeatActivity.Beat);
    AllData.put("ASM", ASMActivity.ASM);
    AllData.put("PoliceOfficer", ASMActivity.OfficerName);
    AllData.put("ActivityType", TypeActivity.ActivityType);
    AllData.put("TopicsDiscussed", TypeActivity.Topics.toString());
    AllData.put("PointsOfDiscussionWithASM",MetASMActivity.PointsDiscussed);
    AllData.put("Number of Attendees",NumberOfAttendees);
    AllData.put("Comments",Comments);

    FirebaseDatabase.getInstance().setPersistenceEnabled(true);

    DataRef.push().setValue(AllData);

    SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
    editor.putInt("count_images", 0);
    editor.commit();

    StopGps();


}



protected void StopGps(){


    SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME2, MODE_PRIVATE).edit();
    editor.putBoolean("GpsFlag", false);
    editor.commit();

    startActivity(new Intent(this, GpsTrackerActivity.class));
}

}
