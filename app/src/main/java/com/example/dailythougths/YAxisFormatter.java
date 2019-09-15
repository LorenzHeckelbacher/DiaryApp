package com.example.dailythougths;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

public class YAxisFormatter extends ValueFormatter {

    private static final int [] MOOD = {1, 2, 3, 4, 5};

    @Override
    public String getFormattedValue(float value) {
        int num = (int) value;
        for (int i : MOOD) {
            if (i == num) {
                return MoodState.valueOf((int)value).name();
            }
        }
        return super.getFormattedValue(value);
    }
}
