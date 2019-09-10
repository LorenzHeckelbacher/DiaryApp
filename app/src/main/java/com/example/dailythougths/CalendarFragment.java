package com.example.dailythougths;

import android.arch.persistence.room.Database;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;


public class CalendarFragment extends Fragment implements DiaryDataChangedListener {



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //MainActivity mainActivity = (MainActivity) getActivity();
        //setRetainInstance(true);
        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }

    @Override
    public void onCalendarChanged() {

    }
}
