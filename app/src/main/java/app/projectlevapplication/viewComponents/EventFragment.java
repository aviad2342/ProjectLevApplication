package app.projectlevapplication.viewComponents;


import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.Date;

import app.projectlevapplication.R;
import app.projectlevapplication.core.Comment;
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
    TextView txtGalleryPics;
    TextView btnWatch;
    FragmentManager fragmentManager;
    ProgressDialog loading;

    public EventFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.activity_event, container, false);
        context = view.getContext();
        activity = getActivity();
        fragmentManager = activity.getFragmentManager();

        eventTitle = (TextView) view.findViewById(R.id.txtEventTitle);
        dateTime = (TextView) view.findViewById(R.id.txtDateTime);
        location = (TextView) view.findViewById(R.id.txtLocation);
        membesNumber = (TextView) view.findViewById(R.id.txtMembesNumber);
        publisher = (TextView) view.findViewById(R.id.txtPublisher);
        description = (TextView) view.findViewById(R.id.txtDescription);
        txtGalleryPics = (TextView) view.findViewById(R.id.txtGalleryPics);
        btnWatch = (TextView) view.findViewById(R.id.btnWatch);

        Bundle args = getArguments();
        eventToDisplay = (Event) args.getSerializable("mEvent");

        eventTitle.setText(eventToDisplay.getTitle());
        dateTime.setText(Utils.eventToDateString(eventToDisplay.getPublishDate()));
        location.setText(eventToDisplay.getLocation());
        membesNumber.setText(String.valueOf(eventToDisplay.getCapacity()));
        publisher.setText(eventToDisplay.getTitle());
        description.setText(Html.fromHtml(eventToDisplay.getDescription()));

        if(eventToDisplay.getImageCount() > 0){
            txtGalleryPics.setText(String.valueOf(eventToDisplay.getImageCount()));
            btnWatch.setVisibility(View.VISIBLE);
        }else{
            txtGalleryPics.setText(getString(R.string.event_no_gallery_picture_found));
            btnWatch.setVisibility(View.INVISIBLE);
        }

        btnWatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new EventGalleryFragment();
                Bundle args = new Bundle();
                args.putInt("mEventId",eventToDisplay.getEventID());
                fragment.setArguments(args);
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right);
                ft.replace(R.id.frame_container, fragment,"EventGalleryFragment");
                ft.addToBackStack("EventGalleryFragment");
                ft.commit();
            }
        });

        return view;
    }
}
