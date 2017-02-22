package app.projectlevapplication.viewComponents;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Html;
import android.text.Spanned;
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

import app.projectlevapplication.R;
import app.projectlevapplication.utils.Utils;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutFragment extends Fragment implements Html.ImageGetter {

    Context context;
    Activity activity;
    TextView aboutTitle;
    TextView aboutContent;
    ProgressDialog loading;
    long start;
    long duration;

    public AboutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_about, container, false);
        start = System.currentTimeMillis();
        context = view.getContext();
        activity = getActivity();
        aboutTitle = (TextView) view.findViewById(R.id.aboutTitle);
        aboutContent = (TextView) view.findViewById(R.id.aboutContent);
        loadAbout();
        return view;
    }
    // Loading About From Server Using String Request
    public void loadAbout(){
        loading = ProgressDialog.show(context,getString(R.string.progress_dialog_message),getString(R.string.progress_dialog_title),false,false);

        String url = Utils.COMMUNITY_ABOUT;

        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loading.dismiss();
                Spanned spanned = Html.fromHtml(Utils.getInstance().responseToAbout(response), AboutFragment.this, null);
                aboutContent.setText(spanned);
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
    public Drawable getDrawable(String source) {
        int id = R.drawable.personal_small;

        if(source.equals("http://arianlev.com/gallery/personal_small.jpg")){
            id = R.drawable.personal_small;
            // id= activity.getResources().getIdentifier("collegel_small", "drawable",activity.getPackageName());
        }

        if(source.equals("http://arianlev.com/gallery/collegel_small.jpg")){
            id = R.drawable.collegel_small;
           // id= activity.getResources().getIdentifier("collegel_small", "drawable",activity.getPackageName());
        }

        if(source.equals("http://arianlev.com/gallery/business_small.jpg")){
            id = R.drawable.business_small;
        }
        LevelListDrawable d = new LevelListDrawable();
        Drawable empty = activity.getDrawable(id);;
        d.addLevel(0, 0, empty);
        d.setBounds(0, 0, empty.getIntrinsicWidth(), empty.getIntrinsicHeight());

        return d;
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
        JsonObjectRequest request_json = new JsonObjectRequest(url, Utils.UsageStatisticsToJsonObject(uID,Utils.milliToSeconds(duration),"אודות"),
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
