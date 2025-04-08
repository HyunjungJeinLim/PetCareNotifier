package com.example.petcarenotifier.ui.calendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.petcarenotifier.R;
import com.example.petcarenotifier.data.entity.CalendarEventEntity;

import java.util.List;

public class EventAdapter extends BaseAdapter {
    private final Context context;
    private final List<CalendarEventEntity> events;

    public EventAdapter(Context context, List<CalendarEventEntity> events) {
        this.context = context;
        this.events = events;
    }

    @Override public int getCount() { return events.size(); }
    @Override public Object getItem(int position) { return events.get(position); }
    @Override public long getItemId(int position) { return events.get(position).id; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_event, parent, false);
        }

        CalendarEventEntity event = events.get(position);

        ((TextView) convertView.findViewById(R.id.tvEventDate)).setText(event.date);
        ((TextView) convertView.findViewById(R.id.tvEventTitle)).setText(event.title);

        return convertView;
    }
}

