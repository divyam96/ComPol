package org.janaagraha.cp_jccd;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

public class MetASMActivity extends AppCompatActivity {
    public static String PointsDiscussed;
    public EditText editText;
    public static final String MY_PREFS_NAME = "MyPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_met_asm);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        editText = (EditText) findViewById(R.id.editText3);
    }

    public void DoneWriting(View view){
        PointsDiscussed = editText.getText().toString();
        Intent backTToTypeActivity = new Intent(view.getContext(),PostActivity.class);
        startActivity(backTToTypeActivity);
    }

}
