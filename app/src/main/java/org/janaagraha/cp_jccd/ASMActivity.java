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

import java.util.ArrayList;

public class ASMActivity extends AppCompatActivity {

    public static String OfficerName;
    public static String ASM;
    private Cursor asm;
    private Database_ASM db_ASM;
    public EditText editText_officer;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
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
