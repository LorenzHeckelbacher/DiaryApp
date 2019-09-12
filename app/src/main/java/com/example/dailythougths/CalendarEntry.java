package com.example.dailythougths;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import androidx.versionedparcelable.ParcelField;

import java.io.Serializable;
import java.util.Date;


@Entity
public class CalendarEntry implements Serializable {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int entryId;
    private String date;
    private int state;
    private String experiences;


    @NonNull
    public String getDate() {
        return date;
    }

    public void setDate(@NonNull String date) {
        this.date = date;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getEntryId() {
        return entryId;
    }

    public void setEntryId(int entryId) {
        this.entryId = entryId;
    }

    public String getExperiences() {
        return experiences;
    }

    public void setExperiences(String experiences) {
        this.experiences = experiences;
    }

}
