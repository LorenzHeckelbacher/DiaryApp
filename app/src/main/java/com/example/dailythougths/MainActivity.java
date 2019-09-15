package com.example.dailythougths;

import android.app.DatePickerDialog;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DiaryDataChangedListener {

    BottomNavigationView bottomNav;

    private static final String DATABASE_NAME = "calendarDB";
    private CalendarEntryDatabase calendarEntryDatabase;
    private static final String TAG = "MainActivity";

    private ArrayList<CalendarEntry> entries;
    private EntryItemAdapter entryItemAdapter;
    private Bundle addActivityExtras;

    private Fragment selectedFragment;
    private CalendarEntry selectedEntry;

    private int timePeriodType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "Inside of onCreate");
        initCalendarEntryDatabase();
        prepareEntryList();
        handleExtras();
        getAllCalendarEntries();
        Log.d(TAG, "Entries loaded: " + entries.size());
        setupBottomNavigationBar();
        timePeriodType =0;


        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CalendarFragment()).commit();
    }

    private void prepareEntryList() {
        if (entries == null) {
            entries = new ArrayList<>();
        }
    }

    private void getAllCalendarEntries() {
        Log.d(TAG, "Inside of getAllCalendarEntries");
        new Thread(new Runnable() {
            @Override
            public void run() {
                entries = new ArrayList<>();
                entries = new ArrayList<>(Arrays.asList(calendarEntryDatabase.daoAccess().loadAllEntries()));
                for (CalendarEntry e : entries){
                    Log.d(TAG, ""+ e.getEntryId());
                }
                Log.d(TAG,"Lenght of entry list: " + entries.size());
                if (!entries.isEmpty()) {
                    setCalendarViews();
                }
            }
        }).start();
    }

    private void setCalendarViews() {
        Log.d(TAG, "setCalViews");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                selectedFragment = new CalendarFragment();
                setFragmentArguments(entries);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            //Bundle b = new Bundle();
           // b.putSerializable(calendarEntries);
            }
        });
    }

    private void handleExtras() {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        Log.d(TAG, "Inside handle Extras");
        if (extras != null){
            addActivityExtras = extras;
            enterNewIntro();
        }
    }

    //the database is initialised
    private void initCalendarEntryDatabase(){
        if (calendarEntryDatabase == null) {
            calendarEntryDatabase = Room.databaseBuilder(getApplicationContext(),
                    CalendarEntryDatabase.class, DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
    }

    private void setFragmentArguments(ArrayList<CalendarEntry> calendarEntries) {
        Bundle args = new Bundle();
        String key = getString(R.string.entry_list);
        args.putSerializable(key, calendarEntries);
        args.putInt(getString(R.string.period_type), timePeriodType);
        selectedFragment.setArguments(args);
    }

    //the NavigationBar is set up and its listener is activated; the three options available are Calendar, Mood Statistics and the add function
    private void setupBottomNavigationBar() {
        bottomNav = findViewById(R.id.bottom_navigation);

        BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectedFragment = null;

                switch (item.getItemId()){
                    case R.id.nav_calendar:
                        selectedFragment = new CalendarFragment();
                        setFragmentArguments(entries);
                        break;
                    case R.id.nav_mood_stats:
                        if (checkDateOccurences(entries)) {
                            selectedFragment = new MoodFragment();
                            setFragmentArguments(entries);
                            }
                        break;
                    case R.id.nav_add:
                        startAdd();
                        break;
                }
                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                }

                return true;
            }
        };

        bottomNav.setOnNavigationItemSelectedListener(navListener);

    }

    //the AddActivity is started using Intents
    private void startAdd(){
        Intent i = new Intent(this, AddActivity.class);
        startActivity(i);
    }


    private void insertCalendarEntryIntoDB(String date, int stateValue, String experiences){
        CalendarEntry calendarEntry =new CalendarEntry();
        calendarEntry.setDate(date);
        calendarEntry.setState(stateValue);
        calendarEntry.setExperiences(experiences);
        Log.d(TAG, "MMOD_STATE: OK???????"+ calendarEntry.getState());
        calendarEntryDatabase.daoAccess().insertCalendarEntry(calendarEntry);
    }


    private void enterNewIntro(){
        //new thread to put data in the database.
        new Thread(new Runnable() {
            @Override
            public void run() {
                String experiences = addActivityExtras.getString(getString(R.string.experiences));
                String date = addActivityExtras.getString(getString(R.string.date));
                int stateValue = addActivityExtras.getInt(getString(R.string.mood_state));
                Log.d(TAG, "Main Activity: " + experiences + ", " + date + ", " + stateValue);
                insertCalendarEntryIntoDB(date, stateValue, experiences);

            }
        }).start();
    }

    private void deleteSelectedEntry() {
        //Log.d(TAG,"delete ??? "+ selectedEntry.getEntryId());
        new Thread(new Runnable() {
            @Override
            public void run() {
                calendarEntryDatabase.daoAccess().deleteCalendarEntry(selectedEntry);
            }
        }).start();
        getAllCalendarEntries();
    }

    @Override
    public void onCalendarChanged(int entryId) {
        Log.d(TAG, "Main: onCalendarChanged - entry ID ? " + entryId);
    }

    // gets invoked when item in Calendar Fragment is clicked
    @Override
    public void onEntrySelected(CalendarEntry entry) {
        Log.d(TAG, "Main: onCalendarChanged - entry ID ? " + entry.getEntryId());
        selectedEntry = entry;
        deleteSelectedEntry();

    }

    private boolean checkDateOccurences(List<CalendarEntry> currentEntries) {
        if (currentEntries.isEmpty()) {
            Toast.makeText(MainActivity.this, "You have not enough Dates, please add at least 2 dates", Toast.LENGTH_SHORT).show();
            return false;}
        for (CalendarEntry e : currentEntries) {
            Log.d(TAG, e.getDate());
        }
        ArrayList<String> dates = new ArrayList<>();
        for (CalendarEntry e : currentEntries) {
            dates.add(e.getDate());
        }
        if (CalendarEntry.occurencesMultipleDates(dates)){
            return true;
        }
        else {
            Toast.makeText(MainActivity.this, "You have not enough Dates, please add at least 2 dates", Toast.LENGTH_SHORT).show();
            return false;}
    }

    @Override
    public void onTimePeriodChanged(int periodType) {
        timePeriodType = periodType;
    }

    @Override
    public void onUpdateChartData(List<CalendarEntry> entryOutputs) {
            if (checkDateOccurences(entryOutputs)) {
                selectedFragment = new MoodFragment();
                ArrayList<CalendarEntry> entriesArrayList = new ArrayList<CalendarEntry>(entryOutputs);
                setFragmentArguments(entriesArrayList);
                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            }
        }
    }
}
