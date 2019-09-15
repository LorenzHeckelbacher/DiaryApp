package com.example.dailythougths;

import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

// This class was thought to show the date label for every entry on the y-axis instead of float values
public class XAxisFormatter extends ValueFormatter {

    private int periodType;
    private static final int TOTAL = 0;
    private static final int MONTH = 1;
    private static final int YEAR = 2;
    private int dayDifference;
    private Date [] dates;
    private String [] formattedDates;
    private Date minDate;
    private Date maxDate;
    private Calendar c = Calendar.getInstance();


    public XAxisFormatter (int periodType, int minNumericdate, int maxNumericDate) {
        this.periodType = periodType;
        dayDifference = getDayDifference(minNumericdate, maxNumericDate);
        setDates();
        setFormattedDates();
    }

    private void setFormattedDates() {
        formattedDates = new String[dates.length];
        for (int i = 0; i< dates.length; i++) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            formattedDates[i] = sdf.format(c.getTime());
        }
    }

    private void setDates() {
        c = Calendar.getInstance();
        dates = new Date[dayDifference];
        dates[0] = minDate;
        c.setTime(minDate);
        for (int i = 1; i < dayDifference; i++) {
            c.add(Calendar.DATE, 1);
            dates[i] = c.getTime();
        }
    }

    private int getDayDifference(int minNumericdate, int maxNumericDate) {
        int minDay = minNumericdate % 100;
        int minMonth = (minNumericdate % 10000) / 100;
        int minYear = minNumericdate / 10000;
        int maxDay = maxNumericDate % 100;
        int maxMonth = (maxNumericDate % 10000) / 100;
        int maxYear = maxNumericDate /10000;
        minDate = new GregorianCalendar(minYear, minMonth, minDay).getTime();
        maxDate = new GregorianCalendar(maxYear, maxMonth, maxDay).getTime();
        long diff = maxDate.getTime() - minDate.getTime();
        return (int)TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }

    private String getLabelForFirstDayInYear() {
        return "2019";
    }

    @Override
    public String getFormattedValue(float value) {
        return formattedDates[(int) value];
        //return super.getFormattedValue(value);
    }

}
