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
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class
CalendarFragment extends Fragment {

    private static final String TAG = "CalendarFragment";
    private List<CalendarEntry> entries;
    private EntryItemAdapter entryItemAdapter;
    private DiaryDataChangedListener dataChangedListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_calendar, container, false);
        if (getArguments() != null){
            entries = (List<CalendarEntry>)getArguments().getSerializable(getString(R.string.entry_list));
        }
        else {
            if (entries == null) {
                entries = new ArrayList<>();
            }
        }
        entryItemAdapter = new EntryItemAdapter(getContext(), entries);
        ListView listView = v.findViewById(R.id.diary_entry_list);
        listView.setAdapter(entryItemAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CalendarEntry clickedEntry = entries.get(i);
                dataChangedListener.onEntrySelected(clickedEntry);
            }
        });
        //MainActivity mainActivity = (MainActivity)getActivity();
        //entries = mainActivity.getEntries();
        //entryItemAdapter = new EntryItemAdapter((MainActivity)getActivity(), entries);

        //MainActivity mainActivity = (MainActivity) getActivity();
        //setRetainInstance(true);
        return v;
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
