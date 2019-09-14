package com.example.dailythougths;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.EntryXComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MoodFragment extends Fragment implements AsyncResponse {

    private static final String TAG = "MoodFragment";
    private static final int Y = 365; //year
    private static final int M = 30; // month
    private static final int W = 7; //week
    private List<CalendarEntry> entries;
    private DiaryDataChangedListener dataChangedListener;
    private LineChart chart;

    private int periodMultiplicator;
    private Button button;

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
        periodMultiplicator =0;
        button = v.findViewById(R.id.buttonMoodFragment);
        chart = v.findViewById(R.id.chart);
        setListeners();
        drawCompleteChart();
        //prepareDatesForEntries("year");
        return v;
    }

    private void setListeners() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startChartAsyncTask();
            }
        });
    }

    private void startChartAsyncTask(){
        new ChartTask(Y, periodMultiplicator, entries,  this).execute(entries);
    }


    private void drawCompleteChart() {
        ArrayList<Entry> chartEntries = new ArrayList<Entry>();
        Log.d(TAG, "insideDrawChart 1");
        for (CalendarEntry calendarEntry: entries) {
            String date = calendarEntry.getDate().isEmpty() ? getString(R.string.template) : calendarEntry.getDate() ;
            Log.d(TAG, "insideDrawChart 2" + ", "+ date + ", " + calendarEntry.getState());
            chartEntries.add(new Entry(CalendarEntry.getDateNumeric(date, getString(R.string.dateSeparator)), calendarEntry.getState()));
        }
        Collections.sort(chartEntries, new EntryXComparator());
        Log.d(TAG, chartEntries.toString());
        LineDataSet dataSet = new LineDataSet(chartEntries, "Label");
        Log.d(TAG, dataSet.toString());
        dataSet.setColor(android.R.color.holo_red_light);
        dataSet.setValueTextColor(android.R.color.black);
        //Log.d(TAG,"Line Data: " + lineData.getDataSetByLabel("Label", true) + ", ");
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(dataSet);
        LineData lineData = new LineData(dataSets);
        chart.setData(lineData);
        //Log.d(TAG,"Chart: " + chart.toString() + ", ");
        chart.invalidate();
    }

    private void drawPartOfChart(ArrayList<CalendarEntry> calendarEntries) {
        ArrayList<Entry> chartEntries = new ArrayList<Entry>();

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

    @Override
    public void processFinish(List<CalendarEntry> entryOutputs) {
        dataChangedListener.onUpdateChartData(entryOutputs);
    }
}
