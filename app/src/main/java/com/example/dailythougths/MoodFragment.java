package com.example.dailythougths;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

public class MoodFragment extends Fragment {

    private static final String TAG = "MoodFragment";
    private List<CalendarEntry> entries;
    private DiaryDataChangedListener dataChangedListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_moods, container, false);
        if (getArguments() != null){
            try {
                entries = (List<CalendarEntry>) getArguments().getSerializable(getString(R.string.entry_list));
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }
        else {
            entries = new ArrayList<>();
        }
        LineChart chart = v.findViewById(R.id.chart);
        List<Entry> chartEntries = new ArrayList<Entry>();
        drawChart(chart, chartEntries);

        return v;
    }

    private void drawChart(LineChart chart, List<Entry> chartEntries) {
        Log.d(TAG, "insideDrawChart 1");
        for (CalendarEntry calendarEntry: entries) {
            String date = calendarEntry.getDate().isEmpty() ? getString(R.string.template) : calendarEntry.getDate() ;
            Log.d(TAG, "insideDrawChart 2" + date + ", " + calendarEntry.getState());
            chartEntries.add(new Entry(CalendarEntry.getDateNumeric(date), calendarEntry.getState()));
        }
        LineDataSet dataSet = new LineDataSet(chartEntries, "Label");
        dataSet.setColor(android.R.color.holo_red_light);
        dataSet.setValueTextColor(android.R.color.black);
        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);
        chart.invalidate();
    }

    @Override
    public void onAttach(Context context) {
        try {
            FragmentActivity mainActivity = getActivity();
            if (mainActivity != null) {
                dataChangedListener = (DiaryDataChangedListener) mainActivity;
            }
        } catch (ClassCastException e) {
            Log.e(TAG, "onAttach: ClassCastException " + e.getMessage());
        }
        super.onAttach(context);
    }
}
