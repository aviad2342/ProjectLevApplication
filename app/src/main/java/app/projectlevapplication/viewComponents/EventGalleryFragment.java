package app.projectlevapplication.viewComponents;


import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONObject;

import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;

import app.projectlevapplication.R;
import app.projectlevapplication.core.Event;
import app.projectlevapplication.core.Media;
import app.projectlevapplication.utils.Utils;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventGalleryFragment extends Fragment {

    Context context;
    Activity activity;
    ProgressDialog loading;
    ImageView imageDisplay;
    ArrayList<Media> medias;
    ArrayList<String> urls;
    GridView galleryGridView;
    ArrayList<Bitmap> bitmapList;
    GalleryAdapter adapter;
    int eventId;
    long start;
    long duration;

    public EventGalleryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_event_gallery, container, false);
        start = System.currentTimeMillis();
        context = view.getContext();
        activity = getActivity();
        urls = new ArrayList<>();
        medias = new ArrayList<>();
        galleryGridView = (GridView) view.findViewById(R.id.galleryGridView);
        imageDisplay = (ImageView) view.findViewById(R.id.imageDisplay);
        bitmapList = new ArrayList<>();

        Bundle args = getArguments();
        eventId = args.getInt("mEventId");
        loadGallery();
        galleryGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = adapter.getItem(position);
                Picasso.with(context).load(selectedItem).into(imageDisplay);
            }
        });
        return view;
    }

    public void loadGallery(){
        loading = ProgressDialog.show(context,getString(R.string.progress_dialog_message),getString(R.string.progress_dialog_title),false,false);

        String url = Utils.ALL_ARTICLE_IMAGES+eventId;

        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loading.dismiss();
                adapter = new GalleryAdapter(context, Utils.getInstance().responseToMediaUrlList(response));
                galleryGridView.setAdapter(adapter);
                Picasso.with(context).load(adapter.getItem(0)).into(imageDisplay);

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

//    private Bitmap urlImageToBitmap(String imageUrl) throws Exception {
//        Bitmap result = null;
//        URL url = new URL(imageUrl);
//        if(url != null) {
//            result = BitmapFactory.decodeStream(url.openConnection().getInputStream());
//        }
//        return result;
//    }
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
    JsonObjectRequest request_json = new JsonObjectRequest(url, Utils.UsageStatisticsToJsonObject(uID,Utils.milliToSeconds(duration),"גלריית אירוע"),
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
