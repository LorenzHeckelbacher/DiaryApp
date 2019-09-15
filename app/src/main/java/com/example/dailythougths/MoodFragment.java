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
    private static final int W = 7; //week
    private static final int TOTAL = 0;
    private static final int MONTH = 1;
    private static final int YEAR = 2;
    private static final int MONTH_LONG = 31;
    private static final int MONTH_SHORT = 31;
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
        //prepareDatesForEntries("year");
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
            //Log.d(TAG, "Einzelner Mood Entry ???" + chartEntries.get(i).getY());
            moodValueArray[i] = chartEntries.get(i).getY();
        }
        ArrayList<Entry> indexedEntries = new ArrayList<>();
        for (int i = 0; i< moodValueArray.length; i++) {
            indexedEntries.add(new Entry(i, moodValueArray[i]));
        }
        //float daysDifference = getDaysDifference(chartEntries.get(0).getX(), chartEntries.get(chartEntries.size()-1).getX());
        XAxisFormatter xAxisFormatter = new XAxisFormatter(periodType, (int)(chartEntries.get(0).getX()), (int)(chartEntries.get(chartEntries.size()-1).getX()));



        Log.d(TAG, chartEntries.toString());
        LineDataSet dataSet = new LineDataSet(chartEntries, "Label");
        LineDataSet dataSet2 = new LineDataSet(indexedEntries, "Mood");
        Log.d(TAG, dataSet.toString());
        dataSet.setColor(android.R.color.holo_red_light);
        dataSet.setValueTextColor(android.R.color.black);
        //Log.d(TAG,"Line Data: " + lineData.getDataSetByLabel("Label", true) + ", ");
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSet2.setDrawValues(false);
        dataSet2.setLineWidth(3);
        dataSet2.setCircleRadius(3);
        dataSets.add(dataSet2); // HIER STAND VORHER KEINE 2
        LineData lineData = new LineData(dataSets);
        YAxis leftAxis = chart.getAxisLeft();
        chart.getAxisRight().setDrawLabels(false);
        leftAxis.setValueFormatter(new YAxisFormatter());
        leftAxis.setGranularity(1);
        XAxis xAxis = chart.getXAxis();
        xAxis.setGranularity(1);
        xAxis.setValueFormatter(xAxisFormatter);
        xAxis.setDrawGridLines(false);
        leftAxis.setDrawGridLines(false);
        leftAxis.setTextSize(12);
        xAxis.setTextSize(8);
        chart.setData(lineData);
        chart.setDoubleTapToZoomEnabled(true);
        chart.setTouchEnabled(true);
        chart.setScaleYEnabled(false);
        chart.getDescription().setText("");
        //Log.d(TAG,"Chart: " + chart.toString() + ", ");
        chart.invalidate();
    }

    private float getDaysDifference(float min, float max) {
        switch (periodType) {
            case TOTAL:
                break;
            case YEAR:
                return (float)Y;
            case MONTH:
                return (float) M;
            default:
                break;
        }
        return (max -min);
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
