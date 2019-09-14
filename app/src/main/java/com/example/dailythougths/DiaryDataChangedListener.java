package com.example.dailythougths;

import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.view.MenuItem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public interface DiaryDataChangedListener {

    boolean onNavigationItemSelected(@NonNull MenuItem menuItem);

    //void addCalendarEntry(String date, int stateValue, String experiences);
    void onCalendarChanged(int entryId);

    void onEntrySelected(CalendarEntry entry);

    void onTimePeriodChanged(ArrayList<CalendarEntry> calendarEntryArrayList);

    void onUpdateChartData(List<CalendarEntry> entryOutputs);
}
