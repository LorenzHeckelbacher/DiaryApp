package com.example.dailythougths;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public enum MoodState implements Serializable {
    VeryBad(1), Bad(2), Okay(3), Good(4), VeryGood(5)
    ;

    private int value;
    private static Map map = new HashMap<>();

    private MoodState(int value) {
        this.value = value;
    }

    static {
        for (MoodState moodState : MoodState.values()) {
            map.put(moodState.value, moodState);
        }
    }

    public static MoodState valueOf(int pageType) {
        return (MoodState) map.get(pageType);
    }

    public int getValue() {
        return value;
    }
}
