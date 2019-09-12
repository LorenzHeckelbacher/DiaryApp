package com.example.dailythougths;

import android.os.Parcelable;

import java.io.Serializable;

public interface DiaryDataChangedListener {

    //void addCalendarEntry(String date, int stateValue, String experiences);
    void onCalendarChanged(int entryId);

    void onEntrySelected(CalendarEntry entry);
}
