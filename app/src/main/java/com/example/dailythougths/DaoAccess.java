package com.example.dailythougths;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;


import java.util.Date;
import java.util.List;

@Dao
public interface DaoAccess {
    @Insert
    void insertCalendarEntry(CalendarEntry calendarEntry);

    @Update
    void updateEntries(CalendarEntry... entries);

    @Query ("SELECT * FROM  calendarentry WHERE date =:date")
    CalendarEntry fetchCalendarEntryByDate (String date);

    @Query("SELECT * FROM calendarentry ORDER BY date")
    CalendarEntry[]  loadAllEntries();
}
