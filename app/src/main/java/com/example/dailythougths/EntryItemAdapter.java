package com.example.dailythougths;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class EntryItemAdapter extends ArrayAdapter<CalendarEntry> {

    private List<CalendarEntry> entryList;
    private Context context;

    public EntryItemAdapter(Context context, List<CalendarEntry> objects) {
        super(context, R.layout.single_entry, objects);
        this.context = context;
        entryList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v =convertView;

        if (v == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.single_entry, null);
        }

        TextView mood = v.findViewById(R.id.entry_mood);
        TextView experience = v.findViewById(R.id.experience_text);
        TextView date = v.findViewById(R.id.entry_date);

        CalendarEntry e = entryList.get(position);
        mood.setText(String.valueOf(e.getState()));
        experience.setText(e.getExperiences());
        date.setText(e.getDate());

        return v;
    }
}
