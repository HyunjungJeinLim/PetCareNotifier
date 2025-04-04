package com.example.petcarenotifier;

import java.util.ArrayList;
import java.util.List;

public class TrackingRecord {
    public static List<Record> records = new ArrayList<>();

    static {
        records.add(new Record("2023-06-10", "Normal", "poop"));
        records.add(new Record("2023-06-09", "8:00 AM", "potty"));
    }

    public static class Record {
        public String date;
        public String details;
        public String type;

        public Record(String date, String details, String type) {
            this.date = date;
            this.details = details;
            this.type = type;
        }
    }
}