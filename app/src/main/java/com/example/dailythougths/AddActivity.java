package com.example.dailythougths;

import android.app.DatePickerDialog;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;



import java.util.Calendar;
import java.util.Date;

public class AddActivity extends AppCompatActivity {


    //private static final String DATABASE_NAME = "calendarDB";
    private static final String TAG = "AddActivity";
    //private CalendarEntryDatabase calendarEntryDatabase;
    private TextView displayDate;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private EditText experiencesEditText;
    private RadioGroup moodSelect;
    private RadioButton moodOption;
    private Button addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        initViews();
    }


    private void initViews() {
        initDateView();
        experiencesEditText = findViewById(R.id.experiences);
        initMoodSelect();
        initButton();
    }

    private String getCurrentDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return day + getString(R.string.dateSeparator) + month + getString(R.string.dateSeparator) + year;
    }

    private void initDateView() {
        displayDate = findViewById(R.id.date);
        displayDate.setText(getCurrentDate());

        displayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(AddActivity.this, android.R.style.Theme_Black, dateSetListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month += 1;
                String date = day + getString(R.string.dateSeparator) + month + getString(R.string.dateSeparator) + year;
                displayDate.setText(date);
            }
        };
    }

    private void initButton() {
        addButton = findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToUpdateMainActivity();
            }
        });
    }

    private void initMoodSelect() {
        moodSelect = findViewById(R.id.mood_select);
        moodSelect.check(R.id.okay_radio_button);
    }

   /* private void initCalendarEntryDatabase(){
        calendarEntryDatabase = Room.databaseBuilder(getApplicationContext(),
                CalendarEntryDatabase.class, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build();
    }*/

    public void checkMoodButton(View v){
        int moodId = moodSelect.getCheckedRadioButtonId();
        moodOption = findViewById(moodId);
        Toast.makeText(this, "Selected Mood: " + moodOption.getText(), Toast.LENGTH_SHORT).show();
    }

    /*
    private void enterNewIntro(){
        //new thread to put data in the database.
        new Thread(new Runnable() {
            String experiences = experiencesEditText.getText().toString();
            String date = displayDate.getText().toString();
            MoodState state = determineMoodState();

            @Override
            public void run() {
                addCalendarEntryIntoDB(date, state, experiences);

            }
        }).start();
    } */


    private MoodState determineMoodState() {
        int moodId = moodSelect.getCheckedRadioButtonId();

        switch (moodId){
            case R.id.very_bad_radio_button:
                return MoodState.VeryBad;
            case R.id.bad_radio_button:
                return MoodState.Bad;
            case R.id.good_radio_button:
                return MoodState.Good;
            case R.id.very_good_radio_button:
                return MoodState.VeryGood;

        }
        return MoodState.Okay;
    }

   /* private void addCalendarEntryIntoDB(String date, MoodState state, String experiences){


        CalendarEntry calendarEntry =new CalendarEntry();
        calendarEntry.setDate(date);
        calendarEntry.setState(state.getValue());
        calendarEntry.setExperiences(experiences);
        calendarEntryDatabase.daoAccess().insertCalendarEntry(calendarEntry);
    } */


    private void switchToUpdateMainActivity(){

        String experiences = experiencesEditText.getText().toString();
        if (experiences.length() ==0) {experiences = getString(R.string.template_experiences);}
        String date = displayDate.getText().toString();
        if (date.length() ==0) {date = getString(R.string.template);}
        int state = determineMoodState().getValue();
        Log.d(TAG, "AddActivity: " + state + ", " + experiences + ", " + date);
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra(getString(R.string.experiences), experiences);
        i.putExtra(getString(R.string.date), date);
        i.putExtra(getString(R.string.mood_state), state);
        startActivity(i);
    }


}


