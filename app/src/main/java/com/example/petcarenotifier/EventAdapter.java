package com.example.petcarenotifier;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class EventAdapter extends BaseAdapter {
    private Context context;
    private java.util.List<CalendarEvent.Event> events;

    public EventAdapter(Context context, java.util.List<CalendarEvent.Event> events) {
        this.context = context;
        this.events = events;
    }

    @Override
    public int getCount() {
        return events.size();
    }

    @Override
    public Object getItem(int position) {
        return events.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_event, parent, false);
        }

        CalendarEvent.Event event = events.get(position);

        TextView tvDate = convertView.findViewById(R.id.tvEventDate);
        TextView tvTitle = convertView.findViewById(R.id.tvEventTitle);

        tvDate.setText(event.date);
        tvTitle.setText(event.title);

        return convertView;
    }
}