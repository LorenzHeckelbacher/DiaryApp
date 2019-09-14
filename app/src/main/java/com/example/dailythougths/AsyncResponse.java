package com.example.dailythougths;

import java.util.List;

public interface AsyncResponse {
    void processFinish(List<CalendarEntry> entryOutputs);
}
