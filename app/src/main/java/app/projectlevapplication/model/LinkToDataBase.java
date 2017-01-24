package app.projectlevapplication.model;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.io.BufferedReader;
import java.io.Console;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import app.projectlevapplication.MainActivity;

/**
 * Created by user-pc on 23/01/2017.
 */

public class LinkToDataBase {

    private void getJsonArrayData(String url, Activity activity) {
        JSONArray result;
        ProgressDialog loading = ProgressDialog.show(activity,"Please wait...","Retrieving data...",false,false);

        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //loading.dismiss();

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
    }

    private void showJSON(String response){
        try {
            // JSONObject jsonObject = new JSONObject(response);
            //JSONArray result = jsonObject.getJSONArray(response);
            JSONArray result = new JSONArray(response);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    public void getJSON(String url, Activity activity) {
        class GetJSON extends AsyncTask<String, Void, String> {
            Activity activity;
            ProgressDialog loading;

            public GetJSON(Activity activity) {
                this.activity = activity;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(activity, "Please Wait...",null,true,true);
            }

            @Override
            protected String doInBackground(String... params) {

                String uri = "http://arianlev.esy.es/ArianLev_Community/api/api.php?key=W2jFgx1leQ&opt=1";

                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();

                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String json;
                    while((json = bufferedReader.readLine())!= null){
                        System.out.print(json);
                        sb.append(json+"\n");
                    }

                    return sb.toString().trim();

                }catch(Exception e){
                    return null;
                }

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
            }
        }
        GetJSON gj = new GetJSON(activity);
        gj.execute(url);
    }
}
