package com.example.dailythougths;

import android.os.AsyncTask;
import android.util.Log;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.support.constraint.Constraints.TAG;

public class ChartTask extends AsyncTask<List<CalendarEntry>,Void, int[][]> {

    public AsyncResponse delegate = null;

    private int timePeriod;
    private static final int DATE_FIELDS = 4;
    private static final String TAG = "ChartTask";
    private int periodMultiplicator;
    private static final int Y = 365; //year
    private static final int M = 30; // month
    private static final int W = 7; //week
    private List<CalendarEntry> entries;


    public ChartTask(int timePeriod, int multiplicatorNum, List<CalendarEntry> entries, AsyncResponse delegate) {
        this.timePeriod = timePeriod;
        periodMultiplicator = multiplicatorNum;
        this.entries = entries;
        this.delegate = delegate;
    }


    // gets dates as an Array for every Calendar Entry and saves them in an 2D-Array
    @Override
    protected int [][] doInBackground(List<CalendarEntry>... lists) {
        List<CalendarEntry> entries = lists[0];
        int [][] entryValues = new int[entries.size()][DATE_FIELDS];
        for (int i =0; i <entryValues.length; i++) {
            CalendarEntry e = entries.get(i);
            entryValues[i][0] = e.getEntryId(); // ID
            int[] dateArray = CalendarEntry.getDateAsArray(e.getDate(), "/");
            entryValues[i][1] = dateArray[0]; // day
            entryValues[i][2] = dateArray[1]; //month
            entryValues[i][3] = dateArray[2]; //year
        }

        return entryValues;
    }

    private int getSelectedYear(int currentYear){
        return currentYear + periodMultiplicator;
    }

    private int getSelectedMonth(int currentMonth){
        return currentMonth + periodMultiplicator;
    }


    // This Method switches the chosen cases for time period, either for all entries, for a year or a month and passes the resulting
    // entries to the Interface method which is implemented in the main activity
    @Override
    protected void onPostExecute(int[][] ints) {
        super.onPostExecute(ints);
        ArrayList<CalendarEntry> resultEntries = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        switch (timePeriod) {
            case Y:
                resultEntries = getYearEntries(getSelectedYear(year), ints);
                break;

            case M:
                resultEntries = getMonthEntries(getSelectedMonth(month), ints);
        }
        for (CalendarEntry entry : resultEntries) {
            Log.d(TAG, "CHART TASK" + entry.getDate());
        }
        delegate.processFinish(resultEntries);
    }



    private CalendarEntry getEntryById(int id) {
        for (CalendarEntry e : entries) {
            if (e.getEntryId() == id) {
                return e;
            }
        }
        return null;
    }

    private ArrayList<CalendarEntry> getYearEntries(int selectedYear, int[][] ints) {
        ArrayList<CalendarEntry> results = new ArrayList<>();
        for (int[] entryDates : ints) {
            if (entryDates[3] == selectedYear) {
                results.add(getEntryById(entryDates[0]));
            }
        }
        return results;
    }

    private ArrayList<CalendarEntry> getMonthEntries(int selectedMonth, int[][] ints) {
        ArrayList<CalendarEntry> results = new ArrayList<>();
        for (int[] entryDates : ints) {
            if (entryDates[2] == selectedMonth) {
                results.add(getEntryById(entryDates[0]));
            }
        }
        return results;
    }

}
