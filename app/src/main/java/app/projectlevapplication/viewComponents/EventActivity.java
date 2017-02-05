package app.projectlevapplication.viewComponents;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import app.projectlevapplication.R;
import app.projectlevapplication.core.Event;
import app.projectlevapplication.core.Member;
import app.projectlevapplication.utils.MyMenuBar;
import app.projectlevapplication.utils.Utils;

public class EventActivity extends MyMenuBar {

    Event eventToDisplay;
    TextView eventTitle;
    TextView dateTime;
    TextView location;
    TextView membesNumber;
    TextView publisher;
    TextView description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        eventTitle = (TextView) findViewById(R.id.txtEventTitle);
        dateTime = (TextView) findViewById(R.id.txtDateTime);
        location = (TextView) findViewById(R.id.txtLocation);
        membesNumber = (TextView) findViewById(R.id.txtMembesNumber);
        publisher = (TextView) findViewById(R.id.txtPublisher);
        description = (TextView) findViewById(R.id.txtDescription);

        eventToDisplay = (Event) getIntent().getSerializableExtra("mEvent");

        eventTitle.setText(eventToDisplay.getTitle());
        dateTime.setText(Utils.eventToDateString(eventToDisplay.getPublishDate()));
        location.setText(eventToDisplay.getLocation());
        membesNumber.setText(String.valueOf(eventToDisplay.getCapacity()));
        publisher.setText(eventToDisplay.getTitle());
        description.setText(eventToDisplay.getDescription());
    }
}
