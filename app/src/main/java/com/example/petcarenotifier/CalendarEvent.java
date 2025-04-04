package com.example.petcarenotifier;

import java.util.ArrayList;
import java.util.List;

public class CalendarEvent {
    public static List<Event> events = new ArrayList<>();

    static {
        events.add(new Event("2023-06-15", "Vet Appointment"));
    }

    public static class Event {
        public String date;
        public String title;

        public Event(String date, String title) {
            this.date = date;
            this.title = title;
        }
    }
}