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
import android.widget.Button;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
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
    private static final int TOTAL = 0;
    private static final int MONTH = 1;
    private static final int YEAR = 2;
    private List<CalendarEntry> entries;
    private DiaryDataChangedListener dataChangedListener;
    private LineChart chart;

    private int periodMultiplicator;
    private int periodType;
    private Button buttonYear;
    private Button buttonMonth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_moods, container, false);
        if (getArguments() != null){
            try {
                entries = (List<CalendarEntry>) getArguments().getSerializable(getString(R.string.entry_list));
                periodType = getArguments().getInt(getString(R.string.period_type));
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }
        else {
            entries = new ArrayList<>();
        }
        periodMultiplicator =0;
        buttonYear = v.findViewById(R.id.buttonMoodFragment);
        buttonMonth = v.findViewById(R.id.buttonMonthMood);
        chart = v.findViewById(R.id.chart);
        setListeners();
        drawChart();
        return v;
    }

    private void setListeners() {
        buttonYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataChangedListener.onTimePeriodChanged(YEAR);
                startChartAsyncTask();
            }
        });
        buttonMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataChangedListener.onTimePeriodChanged(MONTH);
                startChartAsyncTask();
            }
        });
    }

    private void startChartAsyncTask(){
        new ChartTask(Y, periodMultiplicator, entries,  this).execute(entries);
    }

    // This Mehtod creates orderd Entry Items of the mpandroidchart Library and sorts them by date.
    // It saves an ordered List of Mood Entries, and creates dataset lists wich can be plotted
    private void drawChart() {
        ArrayList<Entry> chartEntries = new ArrayList<Entry>();
        Log.d(TAG, "insideDrawChart 1");
        for (CalendarEntry calendarEntry: entries) {
            String date = calendarEntry.getDate().isEmpty() ? getString(R.string.template) : calendarEntry.getDate() ;
            int [] dateArray = CalendarEntry.getDateAsArray(date,getString(R.string.dateSeparator));

            Log.d(TAG, "insideDrawChart 2" + ", "+ date + ", " + calendarEntry.getState());
            chartEntries.add(new Entry(CalendarEntry.getDateNumeric(date, getString(R.string.dateSeparator)), calendarEntry.getState()));
        }
        Collections.sort(chartEntries, new EntryXComparator());
        float [] moodValueArray = new float[chartEntries.size()]; //
        for (int i = 0; i< chartEntries.size(); i++) {
            moodValueArray[i] = chartEntries.get(i).getY();
        }
        ArrayList<Entry> indexedEntries = new ArrayList<>();
        for (int i = 0; i< moodValueArray.length; i++) {
            indexedEntries.add(new Entry(i, moodValueArray[i]));
        }
        XAxisFormatter xAxisFormatter = new XAxisFormatter(periodType, (int)(chartEntries.get(0).getX()), (int)(chartEntries.get(chartEntries.size()-1).getX()));
        LineDataSet dataSet = new LineDataSet(chartEntries, "Label");
        LineDataSet dataSet2 = new LineDataSet(indexedEntries, "Mood");
        dataSet.setColor(android.R.color.holo_red_light);
        dataSet.setValueTextColor(android.R.color.black);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSet2.setDrawValues(false);
        dataSet2.setLineWidth(3);
        dataSet2.setCircleRadius(3);
        dataSets.add(dataSet2);
        plotDataSets(dataSets, xAxisFormatter);
    }

    // Method plots the final data sets as a Line Chart with specific configurations on visual parameters of the library MPandroidchart
    private void plotDataSets(ArrayList<ILineDataSet> dataSets, XAxisFormatter xf) {
        LineData lineData = new LineData(dataSets);
        YAxis leftAxis = chart.getAxisLeft();
        chart.getAxisRight().setDrawLabels(false);
        leftAxis.setValueFormatter(new YAxisFormatter());
        leftAxis.setGranularity(1);
        XAxis xAxis = chart.getXAxis();
        xAxis.setGranularity(1);
        xAxis.setValueFormatter(xf);
        xAxis.setDrawGridLines(false);
        leftAxis.setDrawGridLines(false);
        leftAxis.setTextSize(12);
        xAxis.setTextSize(8);
        chart.setData(lineData);
        chart.setDoubleTapToZoomEnabled(true);
        chart.setTouchEnabled(true);
        chart.setScaleYEnabled(false);
        chart.getDescription().setText("");
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

    @Override
    public void processFinish(List<CalendarEntry> entryOutputs) {
        dataChangedListener.onUpdateChartData(entryOutputs);
    }
}
