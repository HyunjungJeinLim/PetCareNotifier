package com.example.petcarenotifier.ui.tracking;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.petcarenotifier.R;
import com.example.petcarenotifier.data.entity.TrackingRecordEntity;

import java.util.List;

public class TrackingHistoryAdapter extends BaseAdapter {
    private final Context context;
    private final List<TrackingRecordEntity> records;

    public TrackingHistoryAdapter(Context context, List<TrackingRecordEntity> records) {
        this.context = context;
        this.records = records;
    }

    @Override
    public int getCount() {
        return records.size();
    }

    @Override
    public Object getItem(int position) {
        return records.get(position);
    }

    @Override
    public long getItemId(int position) {
        return records.get(position).id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.item_tracking_history, parent, false);
        }

        TrackingRecordEntity record = records.get(position);

        TextView tvType = convertView.findViewById(R.id.tvTrackingType);
        TextView tvDate = convertView.findViewById(R.id.tvTrackingDate);
        TextView tvDetails = convertView.findViewById(R.id.tvTrackingDetails);

        tvType.setText(capitalize(record.type));
        tvDate.setText(record.date);
        tvDetails.setText(record.details);

        return convertView;
    }

    private String capitalize(String s) {
        if (s == null || s.isEmpty()) return "";
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }
}
