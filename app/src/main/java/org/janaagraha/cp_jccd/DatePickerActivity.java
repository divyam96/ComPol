package org.janaagraha.cp_jccd;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

public class DatePickerActivity extends AppCompatActivity {

    private TextView Output;
    private Button changeDate;

    private int year;
    private int month;
    private int day;
    public static String Date;
    public static final String MY_PREFS_NAME = "MyPrefsFile";

    static final int DATE_PICKER_ID = 1111;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_picker);
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME,MODE_PRIVATE);
        Date = prefs.getString("Date",null);

        Output = (TextView) findViewById(R.id.Output);
        changeDate = (Button) findViewById(R.id.changeDate);

        // Get current date by calender

        final Calendar c = Calendar.getInstance();
        year  = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day   = c.get(Calendar.DAY_OF_MONTH);

        // Show current date

        Output.setText(new StringBuilder()
                // Month is 0 based, just add 1
                .append(month + 1).append("-").append(day).append("-")
                .append(year).append(" "));


        Date = new StringBuilder().append(month + 1)
                .append("-").append(day).append("-").append(year)
                .append(" ").toString();
        Output.setText(Date);

        // Button listener to show date picker dialog

        changeDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATE_PICKER_ID);
            }
        });
    }


    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_PICKER_ID:

                // open datepicker dialog.
                // set date picker for current date
                // add pickerListener listner to date picker
                return new DatePickerDialog(this, pickerListener, year, month,day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        @Override
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {

            year  = selectedYear;
            month = selectedMonth;
            day   = selectedDay;

            // Show selected date
            Date = new StringBuilder().append(month + 1)
                    .append("-").append(day).append("-").append(year)
                    .append(" ").toString();
            Output.setText(Date);



        }
    };

    public  void MoveToStationActivity(View v){
        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME,MODE_PRIVATE).edit();
        Log.i("date is", Date);
        editor.putString("Date",Date);
        editor.commit();
        Log.i("jkgkh", "bkjgk");
        Intent intent_to_StationActivity = new Intent(v.getContext(),StationActivity.class);
        startActivity(intent_to_StationActivity);
    }
}