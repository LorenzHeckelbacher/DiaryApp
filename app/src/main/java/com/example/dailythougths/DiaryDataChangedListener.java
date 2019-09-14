package com.example.dailythougths;

import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public interface DiaryDataChangedListener {

    //void addCalendarEntry(String date, int stateValue, String experiences);
    void onCalendarChanged(int entryId);

    void onEntrySelected(CalendarEntry entry);

    void onTimePeriodChanged(ArrayList<CalendarEntry> calendarEntryArrayList);

    void onUpdateChartData(List<CalendarEntry> entryOutputs);
}
