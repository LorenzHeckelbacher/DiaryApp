package com.example.dailythougths;

import android.app.DatePickerDialog;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNav;

    private static final String DATABASE_NAME = "calendarDB";
    private CalendarEntryDatabase calendarEntryDatabase;
    private static final String TAG = "MainActivity";

    private ArrayList<CalendarEntry> entries;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initCalendarEntryDatabase();
        handleExtras();
        getAllCalendarEntries();


        setupBottomNavigationBar();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CalendarFragment()).commit();
    }

    // bad code
    public ArrayList<CalendarEntry> getEntries() {
        return entries;
    }

    private void getAllCalendarEntries() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                CalendarEntry []calendarEntries = calendarEntryDatabase.daoAccess().loadAllEntries();
                entries = new ArrayList<>(Arrays.asList(calendarEntries));
                if (!entries.isEmpty()) {
                    setCalendarViews(entries);
                }
            }
        });
    }

    private void setCalendarViews(final ArrayList<CalendarEntry> calendarEntries) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
            Bundle b = new Bundle();
           // b.putSerializable(calendarEntries);
            }
        });
    }

    private void handleExtras() {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null){
            String experiences = extras.getString("experiences");
            String date = extras.getString("date");
            int stateValue = extras.getInt("state");
            enterNewIntro(date, experiences, stateValue);
        }
    }

    private void initCalendarEntryDatabase(){
        if (calendarEntryDatabase == null) {
            calendarEntryDatabase = Room.databaseBuilder(getApplicationContext(),
                    CalendarEntryDatabase.class, DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
    }


    private void setupBottomNavigationBar() {
        bottomNav = findViewById(R.id.bottom_navigation);

        BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                switch (item.getItemId()){
                    case R.id.nav_calendar:
                        selectedFragment = new CalendarFragment();
                        break;
                    case R.id.nav_mood_stats:
                        selectedFragment = new MoodFragment();
                        break;
                    case R.id.nav_add:
                        startAdd();
                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();

                return true;
            }
        };

        bottomNav.setOnNavigationItemSelectedListener(navListener);

    }

    private void startAdd(){
        Intent i = new Intent(this, AddActivity.class);
        startActivity(i);
    }

    private void insertCalendarEntryIntoDB(String date, int stateValue, String experiences){
        CalendarEntry calendarEntry =new CalendarEntry();
        calendarEntry.setDate(date);
        calendarEntry.setState(stateValue);
        calendarEntry.setExperiences(experiences);
        calendarEntryDatabase.daoAccess().insertCalendarEntry(calendarEntry);
    }


    private void enterNewIntro(final String date, final String experiences, final int stateValue){
        //new thread to put data in the database.
        new Thread(new Runnable() {
            @Override
            public void run() {
                insertCalendarEntryIntoDB(date, stateValue, experiences);

            }
        }).start();
    }
}
