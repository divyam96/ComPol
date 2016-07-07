package org.janaagraha.cp_jccd;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.firebase.client.Firebase;

public class PostActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button PostButton = (Button) findViewById(R.id.button_postData);
        Firebase.setAndroidContext(this);

        assert PostButton != null;
        PostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
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

                FirebaseDatabase.getInstance().setPersistenceEnabled(true);

                if (isOnline()) {
                    DataRef.push().setValue(AllData);
                    Toast.makeText(getApplicationContext(), "Posted Successfully!", Toast.LENGTH_LONG).show();
                } else {
                    DataRef.push().setValue(AllData);
                    Toast.makeText(getApplicationContext(), "No Network Available", Toast.LENGTH_LONG).show();
                }
                */

                Intent intent_to_Menu = new Intent(v.getContext(),Menu.class);
                startActivity(intent_to_Menu);

            }
        });

    }
    /*
    public boolean isOnline() {

        Runtime runtime = Runtime.getRuntime();
        try {

            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);

        } catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }

        return false;
    }
*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home) { //app icon in action bar clicked; go back
            //do something
            TypeActivity.ActivityType=null;
            TypeActivity.otherButton.setSelected(false);
            TypeActivity.Topics.setLength(0);
            return super.onOptionsItemSelected(item);
        }

        return super.onOptionsItemSelected(item);
    }

}
