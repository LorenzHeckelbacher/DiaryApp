package com.example.dailythougths;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.Date;

@Dao
public interface DaoAccess {
    @Insert
    void insertCalendarEntry(CalendarEntry calendarEntry);

    @Query ("SELECT * FROM  calendarentry WHERE date =:date")
    CalendarEntry fetchCalendarEntryByDate (String date);
}
