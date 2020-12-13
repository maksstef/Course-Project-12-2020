package com.example.courseproject;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class EventAdapter extends ArrayAdapter<Events>{

    private LayoutInflater inflater;
    private int layout;
    private List<Events> events;

    public EventAdapter(Context context, int resource, List<Events> events){
        super(context, resource, events );
        this.events = events;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }

    public EventAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View view=inflater.inflate(this.layout, parent, false);

        TextView nameView = (TextView) view.findViewById(R.id.title_on_list);
        TextView nameView2 = (TextView) view.findViewById(R.id.date_on_list);

        Events event = events.get(position);

        nameView.setText(event.getTitle());
        nameView2.setText(event.getDate());
        return view;
    }
}
