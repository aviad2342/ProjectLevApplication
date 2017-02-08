package app.projectlevapplication;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Config;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import app.projectlevapplication.core.Member;
import app.projectlevapplication.model.LinkToDataBase;
import app.projectlevapplication.utils.MyMenuBar;
import app.projectlevapplication.utils.Utils;
import app.projectlevapplication.viewComponents.EventListActivity;
import app.projectlevapplication.viewComponents.EventsListAdapter;
import app.projectlevapplication.viewComponents.MembersListActivity;


public class MainActivity extends MyMenuBar{

    private final int PERMISSIONS_REQ= 1212;
    private TextView textViewJSON;
    private static final String JSON_URL = "http://arianlev.esy.es/ArianLev_Community/api/api.php?key=W2jFgx1leQ&opt=0&user=Admin&pass=Admin";
    public ProgressDialog loading;
    TextView txt;
    public Member loginMember;
    SharedPreferences mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textViewJSON = (TextView) findViewById(R.id.json);
        txt = (TextView) findViewById(R.id.textViewtest);
        mPrefs = getPreferences(MODE_PRIVATE);
        permissionsRequest();
        //txt.setText(Utils.getInstance().loadMemberFromPrefs(this).toString());
    }

    @Override
    public void invalidateOptionsMenu() {
        super.invalidateOptionsMenu();
    }

    private void permissionsRequest() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.INTERNET)
                        != PackageManager.PERMISSION_GRANTED  ||
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED
                ||
                ContextCompat.checkSelfPermission(this,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED
                ){
            // No explanation needed, we can request the permission.
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            android.Manifest.permission.CAMERA, android.Manifest.permission.INTERNET},
                    PERMISSIONS_REQ);
        }
        else{
            // all granted already do another stack
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSIONS_REQ: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0) {
                    int grantsOkcount = 0;
                    for(int i=0; i<grantResults.length;i++){
                        if(grantResults[i] == PackageManager.PERMISSION_GRANTED){
                            grantsOkcount++;
                        }
                    }
                    if(grantsOkcount==grantResults.length) {
                        Toast.makeText(this, "all permissions granted!", Toast.LENGTH_LONG).show();
                        // permission was granted, yay! Do the
                        // contacts-related task you need to do.

                    }
                    else{
                        Toast.makeText(this, "some permissions was not granted!", Toast.LENGTH_LONG).show();
                    }
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void getData() {

        loading = ProgressDialog.show(this,"Please wait...","Fetching...",false,false);

        String url = JSON_URL;

        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loading.dismiss();
                //txt.setText(response);
                showJSON(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this,error.getMessage().toString(),Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void showJSON(String response){
        try {
           JSONObject jsonObject = new JSONObject(response);
            //JSONArray result = jsonObject.getJSONArray(response);
            //JSONArray result = new JSONArray(response);
            txt.setText(Utils.getInstance().responseToMember(response).toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
