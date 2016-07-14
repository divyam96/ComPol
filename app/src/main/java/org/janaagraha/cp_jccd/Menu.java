package org.janaagraha.cp_jccd;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

/**
 * Created by divyam on 3/7/16.
 */
public class Menu extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("FirebaseUI: ", "New activity" );
        setContentView(R.layout.welcome_activity);
    }


 public void PreEvent(View v){

     Intent intent_to_DatePickerActivity = new Intent(v.getContext(),DatePickerActivity.class);
     startActivity(intent_to_DatePickerActivity);



 }






 public void  SignOut(View v) {


     Log.d("FirebaseUI: ", "button clicked");
     if (v.getId() == R.id.sign_out) {
         AuthUI.getInstance()
                 .signOut(this)
                 .addOnCompleteListener(new OnCompleteListener<Void>() {
                     @Override
                     public void onComplete(@NonNull Task<Void> task) {
                         // user is now signed out
                         Log.d("FirebaseUI", "signInWithCredential:onComplete:" + task.isSuccessful());
                         Intent intent = new Intent(Menu.this, LaunchActivity.class);
                         intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                         intent.putExtra("EXIT", true);
                         startActivity(intent);

                     }
                 });
     }
 }

  public void launch_camera_activity(View v){


      startActivity(new Intent(Menu.this, CameraActivity.class));
    }


  public void finishing_activity(View v){


    startActivity(new Intent(Menu.this, finish.class));

  }





}
