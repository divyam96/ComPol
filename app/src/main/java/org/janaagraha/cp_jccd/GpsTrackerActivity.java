package org.janaagraha.cp_jccd;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.UUID;

public class GpsTrackerActivity extends ActionBarActivity {
    private static final String TAG = "GpsTrackerActivity";

    // use the websmithing defaultUploadWebsite for testing and then check your
    // location with your browser here: https://www.websmithing.com/gpstracker/displaymap.php
    private String defaultUploadWebsite;

    private static EditText txtUserName;
    private static EditText txtWebsite;
    private static Button trackingButton;

    private boolean currentlyTracking;
    private RadioGroup intervalRadioGroup;
    private int intervalInMinutes = 1;
    private AlarmManager alarmManager;
    private Intent gpsTrackerIntent;
    private PendingIntent pendingIntent;

    public static final String MY_PREFS_NAME = "MyGpsFile";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gpstracker);

        defaultUploadWebsite = getString(R.string.default_upload_website);

        txtWebsite = (EditText)findViewById(R.id.txtWebsite);
        txtUserName = (EditText)findViewById(R.id.txtUserName);
        intervalRadioGroup = (RadioGroup)findViewById(R.id.intervalRadioGroup);
        trackingButton = (Button)findViewById(R.id.trackingButton);
        txtUserName.setImeOptions(EditorInfo.IME_ACTION_DONE);

        SharedPreferences sharedPreferences = this.getSharedPreferences("com.websmithing.gpstracker.prefs", Context.MODE_PRIVATE);
        currentlyTracking = sharedPreferences.getBoolean("currentlyTracking", false);

        boolean firstTimeLoadindApp = sharedPreferences.getBoolean("firstTimeLoadindApp", true);
        if (firstTimeLoadindApp) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("firstTimeLoadindApp", false);
            editor.putString("appID",  UUID.randomUUID().toString());
            editor.apply();
        }


        intervalRadioGroup.setOnCheckedChangeListener(
                new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int i) {
                        saveInterval();
                    }
                });

        trackingButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                trackLocation(view);
            }
        });
    }

    private void saveInterval() {
        if (currentlyTracking) {
            Toast.makeText(getApplicationContext(), R.string.user_needs_to_restart_tracking, Toast.LENGTH_LONG).show();
        }

        SharedPreferences sharedPreferences = this.getSharedPreferences("com.websmithing.gpstracker.prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        switch (intervalRadioGroup.getCheckedRadioButtonId()) {
            case R.id.i1:
                editor.putInt("intervalInMinutes", 1);
                break;
            case R.id.i5:
                editor.putInt("intervalInMinutes", 5);
                break;
            case R.id.i15:
                editor.putInt("intervalInMinutes", 15);
                break;
        }

        editor.apply();
    }

    private void startAlarmManager() {
        Log.d(TAG, "startAlarmManager");

        Context context = getBaseContext();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    17);
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    18);


        }

        alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        gpsTrackerIntent = new Intent(context, GpsTrackerAlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(context, 0, gpsTrackerIntent, 0);

        SharedPreferences sharedPreferences = this.getSharedPreferences("com.websmithing.gpstracker.prefs", Context.MODE_PRIVATE);
        intervalInMinutes = sharedPreferences.getInt("intervalInMinutes", 1);

        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime(),
                intervalInMinutes * 60000, // 60000 = 1 minute
                pendingIntent);
    }

    private void cancelAlarmManager() {
        Log.d(TAG, "cancelAlarmManager");

        Context context = getBaseContext();
        Intent gpsTrackerIntent = new Intent(context, GpsTrackerAlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, gpsTrackerIntent, 0);
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 17 : {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                       Log.i("yay", "fine loc granted");
                    // permission was granted, yay! Do the task you need to do.

                } else {
                           Log.i("permission not granted", "error");
                    // permission denied, boo! Disable the functionality that depends on this permission.
                }
                return;

            }



            case 18 : {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i("yay", "coarse loc granted");
                    // permission was granted, yay! Do the task you need to do.

                } else {
                    Log.i("permission not granted", "error");
                    // permission denied, boo! Disable the functionality that depends on this permission.
                }
                return;
            }
        }
    }



    // called when trackingButton is tapped
    protected void trackLocation(View v) {
        SharedPreferences sharedPreferences = this.getSharedPreferences("com.websmithing.gpstracker.prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (!saveUserSettings()) {
            return;
        }

        if (!checkIfGooglePlayEnabled()) {
            return;
        }

        if (currentlyTracking) {
            cancelAlarmManager();

            currentlyTracking = false;
            editor.putBoolean("currentlyTracking", false);
            editor.putString("sessionID", "");
        } else {
            startAlarmManager();

            currentlyTracking = true;
            editor.putBoolean("currentlyTracking", true);
            editor.putFloat("totalDistanceInMeters", 0f);
            editor.putBoolean("firstTimeGettingPosition", true);
            editor.putString("sessionID",  UUID.randomUUID().toString());
        }

        editor.apply();
        setTrackingButtonState();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d("GpsActivity: ", "in delay func" );
                if(currentlyTracking) {
                    Log.i("Gps:", "flag set");
                    SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                    editor.putBoolean("GpsFlag", true);
                    editor.commit();
                    firebase_login();

                }

                else if(currentlyTracking==false)
                {
                    Log.i("finishGps", "signing out");
                    SignOut();
                }
            }
        }, 1500);


    }

    private boolean saveUserSettings() {
        if (textFieldsAreEmptyOrHaveSpaces()) {
            return false;
        }

        SharedPreferences sharedPreferences = this.getSharedPreferences("com.websmithing.gpstracker.prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        switch (intervalRadioGroup.getCheckedRadioButtonId()) {
            case R.id.i1:
                editor.putInt("intervalInMinutes", 1);
                break;
            case R.id.i5:
                editor.putInt("intervalInMinutes", 5);
                break;
            case R.id.i15:
                editor.putInt("intervalInMinutes", 15);
                break;
        }

        editor.putString("userName", txtUserName.getText().toString().trim());
        editor.putString("defaultUploadWebsite", txtWebsite.getText().toString().trim());

        editor.apply();

        return true;
    }

    private boolean textFieldsAreEmptyOrHaveSpaces() {
        String tempUserName = txtUserName.getText().toString().trim();
        String tempWebsite = txtWebsite.getText().toString().trim();

        if (tempWebsite.length() == 0 || hasSpaces(tempWebsite) || tempUserName.length() == 0 || hasSpaces(tempUserName)) {
            Toast.makeText(this, R.string.textfields_empty_or_spaces, Toast.LENGTH_LONG).show();
            return true;
        }

        return false;
    }

    private boolean hasSpaces(String str) {
        return ((str.split(" ").length > 1) ? true : false);
    }

    private void displayUserSettings() {
        SharedPreferences sharedPreferences = this.getSharedPreferences("com.websmithing.gpstracker.prefs", Context.MODE_PRIVATE);
        intervalInMinutes = sharedPreferences.getInt("intervalInMinutes", 1);

        switch (intervalInMinutes) {
            case 1:
                intervalRadioGroup.check(R.id.i1);
                break;
            case 5:
                intervalRadioGroup.check(R.id.i5);
                break;
            case 15:
                intervalRadioGroup.check(R.id.i15);
                break;
        }

        txtWebsite.setText( defaultUploadWebsite);
        txtUserName.setText(sharedPreferences.getString("userName", ""));
    }

    private boolean checkIfGooglePlayEnabled() {
        if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(this) == ConnectionResult.SUCCESS) {
            return true;
        } else {
            Log.e(TAG, "unable to connect to google play services.");
            Toast.makeText(getApplicationContext(), R.string.google_play_services_unavailable, Toast.LENGTH_LONG).show();
            return false;
        }
    }

    private void setTrackingButtonState() {
        if (currentlyTracking) {
            trackingButton.setBackgroundResource(R.drawable.green_tracking_button);
            trackingButton.setTextColor(Color.BLACK);
            trackingButton.setText(R.string.tracking_is_on);
        } else {
            trackingButton.setBackgroundResource(R.drawable.red_tracking_button);
            trackingButton.setTextColor(Color.WHITE);
            trackingButton.setText(R.string.tracking_is_off);
        }
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume");
        super.onResume(); 

        displayUserSettings();
        setTrackingButtonState();


        Log.d("GpsActivity: ", "i'm done" );
    }




    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.d("FirebaseUI: ", "i'm in" );
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                Log.d("FirebaseUI: ", "logged in" );
                // user is signed in!
                FirebaseUser cur_user = FirebaseAuth.getInstance().getCurrentUser();

                String uiid = cur_user.getUid();

                Log.d("FirebaseUI: ", uiid );
                startActivity(new Intent(this, Menu.class));
                //finish();
            } else {
                Log.d("FirebaseUI: ", "hello" );

                // user is not signed in. Maybe just wait for the user to press
                // "sign in" again, or show a message
            }
        }

        else {
            Log.d("FirebaseUI: ", "not going in" );

        }

    }

    private void firebase_login(){
        setContentView(R.layout.loading_screen);
        Log.d("FirebaseUI: ", "start" );

        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            // already signed in
            FirebaseUser cur_user = auth.getCurrentUser();
            String uiid = cur_user.getUid();
            Log.d("FirebaseUI: ", uiid );
            startActivity(new Intent(this, Menu.class));
        }

        else {
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setProviders(
                                    AuthUI.GOOGLE_PROVIDER,
                                    AuthUI.FACEBOOK_PROVIDER)
                            .build(),
                    100);
        }





    }

    protected  void SignOut(){
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // user is now signed out
                        Log.d("FirebaseUI", "signInWithCredential:onComplete:" + task.isSuccessful());
                        Intent intent = new Intent(GpsTrackerActivity.this, LaunchActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("EXIT", true);
                        startActivity(intent);

                    }
                });
    }
    @Override
    protected void onStop() {
        super.onStop();
    }



}
