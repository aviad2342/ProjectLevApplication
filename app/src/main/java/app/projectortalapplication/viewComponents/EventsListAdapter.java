package app.projectortalapplication.viewComponents;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import app.projectortalapplication.R;
import app.projectortalapplication.core.Event;
import app.projectortalapplication.utils.Utils;

/**
 * Created by Aviad on 04/02/2017.
 */

public class EventsListAdapter extends ArrayAdapter<Event> {

    private List<Event> events;
    private Activity context;

    public EventsListAdapter(Context context, List<Event> events) {
        super(context, R.layout.event_list ,events);
        this.events = events;
        this.context = (Activity)context;
    }

    @Override
    public int getCount() {
        return events.size();
    }

    @Override
    public Event getItem(int position) {
        return events.get(position);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        View row = view;
        if(row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getLayoutInflater();
            row = inflater.inflate(R.layout.event_list, null, true);
        }
        TextView eventTitle = (TextView) row.findViewById(R.id.eventTitle);
        TextView eventTimeAndDate = (TextView) row.findViewById(R.id.eventTimeAndDate);
        TextView numberOfMembers = (TextView) row.findViewById(R.id.numberOfMembers);
        TextView eventLocation = (TextView) row.findViewById(R.id.eventLocation);

        Event event = events.get(position);

        eventTitle.setText(event.getTitle());
        eventTimeAndDate.setText(Utils.eventToDateString(event.getPublishDate()));
        numberOfMembers.setText(String.valueOf(event.getCapacity())+" משתתפים");
        eventLocation.setText(event.getLocation());

        return row;

    }
}
