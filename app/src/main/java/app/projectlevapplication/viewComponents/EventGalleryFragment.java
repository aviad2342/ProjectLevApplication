package app.projectlevapplication.viewComponents;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

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
    ArrayList<Media> medias;
    ArrayList<ImageView> images;
    int eventId;

    public EventGalleryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_event_gallery, container, false);
        context = view.getContext();
        activity = getActivity();
        images = new ArrayList<>();
        medias = new ArrayList<>();

        Bundle args = getArguments();
        eventId = args.getInt("mEventId");
        loadGallery();
        return view;
    }
    public void loadGallery(){
        loading = ProgressDialog.show(activity,"בבקשה המתן...","מחזיר מידע...",false,false);

        String url = Utils.ALL_ARTICLE_IMAGES+eventId;

        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loading.dismiss();
                medias = Utils.getInstance().responseToMediaList(response);
                ImageView image;
                for(int i = 0; i < medias.size(); i++){
                    image = new ImageView(context);
                    Picasso.with(activity).load(Utils.EVENTS_IMAGE+medias.get(i).getFileName()).into(image);
                    images.add(image);
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        Toast.makeText(context,"error", Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
    }

}
