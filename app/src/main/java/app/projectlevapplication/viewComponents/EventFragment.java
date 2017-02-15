package app.projectlevapplication.viewComponents;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import app.projectlevapplication.R;
import app.projectlevapplication.core.Event;
import app.projectlevapplication.utils.Utils;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventFragment extends Fragment {

    Context context;
    Activity activity;
    Event eventToDisplay;
    TextView eventTitle;
    TextView dateTime;
    TextView location;
    TextView membesNumber;
    TextView publisher;
    TextView description;

    public EventFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.activity_event, container, false);
        context = view.getContext();
        activity = getActivity();

        eventTitle = (TextView) view.findViewById(R.id.txtEventTitle);
        dateTime = (TextView) view.findViewById(R.id.txtDateTime);
        location = (TextView) view.findViewById(R.id.txtLocation);
        membesNumber = (TextView) view.findViewById(R.id.txtMembesNumber);
        publisher = (TextView) view.findViewById(R.id.txtPublisher);
        description = (TextView) view.findViewById(R.id.txtDescription);

        Bundle args = getArguments();
        eventToDisplay = (Event) args.getSerializable("mEvent");

        eventTitle.setText(eventToDisplay.getTitle());
        dateTime.setText(Utils.eventToDateString(eventToDisplay.getPublishDate()));
        location.setText(eventToDisplay.getLocation());
        membesNumber.setText(String.valueOf(eventToDisplay.getCapacity()));
        publisher.setText(eventToDisplay.getTitle());
        description.setText(Html.fromHtml(eventToDisplay.getDescription()));

        return view;
    }

}
