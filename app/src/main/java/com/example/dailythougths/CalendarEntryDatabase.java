package com.example.dailythougths;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;


@Database(entities = {CalendarEntry.class}, version = 1, exportSchema = false)
public abstract class CalendarEntryDatabase extends RoomDatabase {
    public abstract DaoAccess daoAccess();
}
