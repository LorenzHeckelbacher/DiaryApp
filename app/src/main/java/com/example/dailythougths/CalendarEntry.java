package com.example.dailythougths;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import androidx.versionedparcelable.ParcelField;

import java.io.Serializable;
import java.util.ArrayList;
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

    public static int getDateNumeric(String date, String separator) {
        int [] dateValues = getDateAsArray(date, separator);
        int year = dateValues[2];
        int month = dateValues[1];
        int day = dateValues[0];
        return year*10000 + (month* 100) + day;
    }

    public static int [] getDateAsArray(String date, String separator) {
        if (date.isEmpty()){date = new String("10/10/2019");}
        String[] dateStrings = date.split(separator);
        int [] dateValues = new int[3];
        for (int i = 0; i < dateStrings.length; i++){
            dateValues[i] = Integer.parseInt(dateStrings[i]);
        }
        return dateValues;
    }
}
