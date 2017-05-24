package app.projectortalapplication.viewComponents;


import android.app.Activity;
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

import org.json.JSONObject;

import app.projectortalapplication.R;
import app.projectortalapplication.utils.Utils;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    Context context;
    Activity activity;
    TextView txtOpening;
    long start;
    long duration;
    ProgressDialog loading;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_home, container, false);
        start = System.currentTimeMillis();
        context = view.getContext();
        activity = getActivity();
        txtOpening = (TextView) view.findViewById(R.id.txtOpening);
        loadOpening();
        return view;
    }

    public void loadOpening(){
        loading = ProgressDialog.show(context,getString(R.string.progress_dialog_message),getString(R.string.progress_dialog_title),false,false);
        String url = Utils.COMMUNITY_OPENING_STATEMENT;
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loading.dismiss();
                txtOpening.setText(Html.fromHtml(Utils.getInstance().responseToOpening(response)));
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
        // Loading Usage Statistics To Server Using Request_json
        duration = System.currentTimeMillis() - start;
        int uID;
        if(Utils.getInstance().loadMemberFromPrefs(context) != null){
            uID = Utils.getInstance().loadMemberFromPrefs(context).getMemberID();
        }else{
            uID = 27;
        }
        String url = Utils.POST_USAGE_STATISTICS;
        JsonObjectRequest request_json = new JsonObjectRequest(url, Utils.UsageStatisticsToJsonObject(uID,Utils.milliToSeconds(duration),"דף הבית"),
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
