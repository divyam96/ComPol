package org.janaagraha.cp_jccd;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.client.Firebase;

public class TypeActivity extends AppCompatActivity {

    public static String ActivityType;
    public static StringBuilder Topics = new StringBuilder();
    public EditText OthersBox;
    public EditText OtherPlan;
    public static RadioButton otherButton;
    public static Spinner activityView;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    // final EditText editText = (EditText) findViewById(R.id.editText_other);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME,MODE_PRIVATE);
        ActivityType = prefs.getString("ActivityType",null);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final CheckBox ChainSnatching = (CheckBox) findViewById(R.id.checkBox);
        final CheckBox HouseBreakTheft = (CheckBox) findViewById(R.id.checkBox2);
        final CheckBox FinancialTheft = (CheckBox) findViewById(R.id.checkBox3);
        final CheckBox SocialAbuse = (CheckBox) findViewById(R.id.checkBox4);
        final CheckBox SeniorCitizenAbuse = (CheckBox) findViewById(R.id.checkBox5);
        final CheckBox PettyTheft = (CheckBox) findViewById(R.id.checkBox6);
        final CheckBox Other = (CheckBox) findViewById(R.id.checkBox7);
        otherButton = (RadioButton) findViewById(R.id.radioButton);
        OtherPlan = (EditText) findViewById(R.id.editText_checkOther);
        OthersBox = (EditText) findViewById(R.id.editText_other);
        activityView = (Spinner) findViewById(R.id.Spinner_ActivityType);
        ArrayAdapter adapter3 = ArrayAdapter.createFromResource(this,R.array.activityList, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activityView.setAdapter(adapter3);

        activityView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==4){
                    ActivityType = parent.getItemAtPosition(4).toString();
                    Intent intent_met_ASM = new Intent(view.getContext(),MetASMActivity.class);
                    startActivity(intent_met_ASM);
                }
                else{
                    ActivityType = parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        Button toScreen4 = (Button) findViewById(R.id.button);
        Firebase.setAndroidContext(this);
        assert toScreen4 != null;
        toScreen4.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(ChainSnatching.isChecked()){
                    Topics.append("ChainSnatching ");
                }
                if(HouseBreakTheft.isChecked()){
                    Topics.append("HouseBreakTheft ");
                }
                if(FinancialTheft.isChecked()){
                    Topics.append("FinancialTheft ");
                }
                if(SocialAbuse.isChecked()){
                    Topics.append("SocialAbuse ");
                }
                if(SeniorCitizenAbuse.isChecked()){
                    Topics.append("SeniorCitizenAbuse ");
                }
                if(PettyTheft.isChecked()){
                    Topics.append("PettyTheft ");
                }
                if(Other.isChecked()){
                    Topics.append(OtherPlan.getText().toString());
                }
                if(otherButton.isChecked()){
                    ActivityType = OthersBox.getText().toString();
                }

                Toast.makeText(getApplicationContext(), "You have selected " + ActivityType + " and " + Topics.toString(), Toast.LENGTH_LONG).show();
                SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME,MODE_PRIVATE).edit();
                editor.putString("ActivityType",ActivityType);
                editor.commit();
                Intent intent_to_screen4 = new Intent(v.getContext(),PostActivity.class);
                startActivity(intent_to_screen4);
            }
        });


    }
}
