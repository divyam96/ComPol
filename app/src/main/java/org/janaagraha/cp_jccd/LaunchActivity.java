package org.janaagraha.cp_jccd;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LaunchActivity extends AppCompatActivity {

    boolean GpsFlag;
    public static final String MY_PREFS_NAME = "MyGpsFile";
    private long enqueue;
    private DownloadManager dm;
    ProgressDialog prgDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent().getBooleanExtra("EXIT", false))
        {   Log.i("launch","ending");
            finish();
            System.exit(0);
        }


        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        GpsFlag= prefs.getBoolean("GpsFlag", false);

      if(GpsFlag==false){
          Log.i("launch", "initial"+GpsFlag);
           setContentView(R.layout.launch_activity);

      }

     else
      {
          Log.i("launch", "alternative");
          firebase_login();
      }


    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.d("FirebaseUI: ", "i'm in");
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
            Log.d("FirebaseUI: ", "not going in");

        }

    }

private void firebase_login(){
    setContentView(R.layout.loading_screen);
    Log.d("FirebaseUI: ", "start");

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
                                AuthUI.EMAIL_PROVIDER,
                                AuthUI.GOOGLE_PROVIDER,
                                AuthUI.FACEBOOK_PROVIDER)
                        .build(),
                100);
    }





}


private void startGps(){

    startActivity(new Intent(this, GpsTrackerActivity.class));


}

public void start(View v){

    startGps();

}

}