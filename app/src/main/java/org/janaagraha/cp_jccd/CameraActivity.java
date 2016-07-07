package org.janaagraha.cp_jccd;

/**
 * Created by divyam on 23/6/16.
 */

import android.content.Context;
import android.content.Intent;
 import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
        import android.net.Uri;
        import android.os.Bundle;
        import android.provider.MediaStore;
        import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
        import android.util.Log;
        import android.view.View;
        import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
        import com.google.android.gms.tasks.OnFailureListener;
        import com.google.android.gms.tasks.OnSuccessListener;
        import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
        import com.google.firebase.storage.UploadTask;

        import java.io.ByteArrayOutputStream;
import java.util.List;



/**
 * Created by divyam on 15/6/16.
 */
public class CameraActivity extends AppCompatActivity{


    public static final String MY_PREFS_NAME = "MyPrefsFile";

    FirebaseStorage storage = FirebaseStorage.getInstance();

    Bitmap mBitmapToSave;

    private static final int REQUEST_CODE_CAPTURE_IMAGE = 1;
    int count_images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("FirebaseUI: ", "New activity" );
        setContentView(R.layout.camera_activity);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
       count_images= prefs.getInt("count_images", 0);
    }


    public void Done(View v) {

        Log.d("FirebaseUI: ", "button clicked" );

        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putInt("count_images", count_images);
        editor.commit();

        startActivity(new Intent(CameraActivity.this, Menu.class));

    }




    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_CAPTURE_IMAGE:
                // Called after a photo has been taken.
                if (resultCode == Activity.RESULT_OK) {
                    // Store the image data as a bitmap for writing later.
                    mBitmapToSave = (Bitmap) data.getExtras().get("data");

                }

                break;

        }
    }



    private void saveToStorage(){

        Log.i("UploadPics: ", "storing initiating");

        StorageReference storageRef = storage.getReferenceFromUrl("gs://compol-b0515.appspot.com");

        StorageReference TestImagesRef = storageRef.child("Data/test" + count_images + ".jpg");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        Log.i("UploadPics: ", "bucket reffered");

        mBitmapToSave.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        Log.i("UploadPics: ", "compression done");

        byte[] data = baos.toByteArray();

        UploadTask uploadTask = TestImagesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                Context context = getApplicationContext();
                count_images++;
                CharSequence text = "sucessfully uploaded image no "+ count_images;
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

            }
        });

        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                final double progress = 100.0 * (taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());

               final ProgressBar mProgress = (ProgressBar) findViewById(R.id.progressBar);


                System.out.println("Upload is " + progress + "% done");

                if (mProgress != null) {
                    mProgress.setProgress((int)progress);
                }


            }
        }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                Context context = getApplicationContext();
                CharSequence text = "Upload is paused";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });


    }


    public void UploadPics(View v) {


        Log.i("UploadPics: ", "entered func");

        Button p1_button = (Button)findViewById(R.id.upload_pics);

            if (mBitmapToSave == null) {
                // This activity has no UI of its own. Just start the camera.
                startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE),
                        REQUEST_CODE_CAPTURE_IMAGE);
                p1_button.setText("Upload");
                return;

            }


            saveToStorage();
            mBitmapToSave = null;
        p1_button.setText("Take Pic");


    }



    StorageReference mStorageRef;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);


        StorageReference storageRef = storage.getReferenceFromUrl("gs://compol-b0515.appspot.com");

        StorageReference TestImagesRef = storageRef.child("Data/test" + count_images + ".jpg");

        // If there's an upload in progress, save the reference so you can query it later
        if (TestImagesRef != null) {
            outState.putString("reference", TestImagesRef.toString());
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // If there was an upload in progress, get its reference and create a new StorageReference
        final String stringRef = savedInstanceState.getString("reference");
        if (stringRef == null) {
            return;
        }
        mStorageRef = FirebaseStorage.getInstance().getReferenceFromUrl(stringRef);

        // Find all UploadTasks under this StorageReference (in this example, there should be one)
        List<UploadTask> tasks = mStorageRef.getActiveUploadTasks();
        if (tasks.size() > 0) {
            // Get the task monitoring the upload
            UploadTask task = tasks.get(0);

            // Add new listeners to the task using an Activity scope
            task.addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                public void onSuccess(UploadTask.TaskSnapshot state) {
                    Log.i("reumedUpload", "success"); //call a user defined function to handle the event.
                }
            });
        }
    }








}