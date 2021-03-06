package app.projectortalapplication.viewComponents;


import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.Serializable;

import app.projectortalapplication.R;
import app.projectortalapplication.core.Event;
import app.projectortalapplication.utils.Utils;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventListFragment extends Fragment {

    ListView list;
    EventsListAdapter adapter;
    public ProgressDialog loading;
    Context context;
    Activity activity;
    FragmentManager fragmentManager;
    Button newItemBtn = null;
    long start;
    long duration;

    public EventListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.activity_event_list, container, false);
        start = System.currentTimeMillis();
        context = view.getContext();
        activity = getActivity();
        fragmentManager = activity.getFragmentManager();
        newItemBtn  = (Button) view.findViewById(R.id.new_item_btn);
        if(Utils.getInstance().loadMemberFromPrefs(context) != null) {
            if (Utils.getInstance().loadMemberFromPrefs(context).isAdmin()) {
                newItemBtn.setVisibility(View.VISIBLE);
            } else {
                newItemBtn.setVisibility(View.INVISIBLE);
            }
        }
        list = (ListView) view.findViewById(R.id.eventsList);
        loadEventsList();

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Event selectedItem = adapter.getItem(position);
                Fragment fragment = new EventFragment();
                Bundle args = new Bundle();
                args.putSerializable("mEvent",(Serializable)selectedItem);
                fragment.setArguments(args);
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right);
                ft.replace(R.id.frame_container, fragment,"EventFragment");
                ft.addToBackStack("EventFragment");
                ft.commit();
            }
        });

        newItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new AddNewEventFragment();
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.setCustomAnimations(R.animator.slide_in_up, R.animator.slide_out_right);
                ft.replace(R.id.frame_container, fragment,"AddNewEventFragment");
                ft.addToBackStack("AddNewEventFragment");
                ft.commit();
            }
        });
        return view;
    }

    public void loadEventsList(){
        loading = ProgressDialog.show(context,getString(R.string.progress_dialog_message),getString(R.string.progress_dialog_title),false,false);

        String url = Utils.ALL_COMMUNITY_EVENTS;

        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                loading.dismiss();

                adapter = new EventsListAdapter(activity, Utils.getInstance().responseToEventList(response));
                list.setAdapter(adapter);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        Toast.makeText(context,getString(R.string.error_server_request), Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onPause() {
        super.onPause();
        duration = System.currentTimeMillis() - start;
        int uID;
        if(Utils.getInstance().loadMemberFromPrefs(context) != null){
            uID = Utils.getInstance().loadMemberFromPrefs(context).getMemberID();
        }else{
            uID = 27;
        }
        String url = Utils.POST_USAGE_STATISTICS;
        JsonObjectRequest request_json = new JsonObjectRequest(url, Utils.UsageStatisticsToJsonObject(uID,Utils.milliToSeconds(duration),"רשימת אירועים"),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(request_json);
    }
}
