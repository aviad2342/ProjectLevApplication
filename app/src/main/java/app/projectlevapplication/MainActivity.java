package app.projectlevapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Config;
import android.view.Menu;
import android.view.MenuItem;
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

import app.projectlevapplication.model.LinkToDataBase;
import app.projectlevapplication.viewComponents.MembersListActivity;


public class MainActivity extends AppCompatActivity implements LogInDialog.DialogFragmentListener{

    private TextView textViewJSON;
    private static final String JSON_URL = "http://arianlev.esy.es/ArianLev_Community/api/api.php?key=W2jFgx1leQ&opt=1";
    public ProgressDialog loading;
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textViewJSON = (TextView) findViewById(R.id.json);
        image = (ImageView) findViewById(R.id.imagev);
       // getJSON(JSON_URL);
        //getData();
        //Picasso.with(this).load("https://s-media-cache-ak0.pinimg.com/originals/53/b1/2c/53b12cfc320db4029d7ce5f25702deb9.png").into(image);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logIn:
                LogInDialog dialog = new LogInDialog();
                dialog.show(getFragmentManager(),"dialog");
                return true;
            case R.id.communityMembers:
                Intent membersListIntent = new Intent(this, MembersListActivity.class);
                startActivity(membersListIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDialogPositiveClick(LogInDialog dialog) {
        Toast.makeText(MainActivity.this,"OK",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDialogNegativeClick(LogInDialog dialog) {

    }

    @Override
    public void onDialogRegisterClick(LogInDialog dialog) {
        Toast.makeText(MainActivity.this,"NO",Toast.LENGTH_LONG).show();
    }

    private void getData() {

        loading = ProgressDialog.show(this,"Please wait...","Fetching...",false,false);

        String url = JSON_URL;

        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loading.dismiss();
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
           // JSONObject jsonObject = new JSONObject(response);
            //JSONArray result = jsonObject.getJSONArray(response);
            JSONArray result = new JSONArray(response);
            textViewJSON.setText(result.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void getJSON(String url) {
        class GetJSON extends AsyncTask<String, Void, String>{
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(MainActivity.this, "Please Wait...",null,true,true);
            }

            @Override
            protected String doInBackground(String... params) {

                System.out.print(params[0]);

                //Toast.makeText(MainActivity.this, params[0], Toast.LENGTH_SHORT).show();
                String uri = params[0];

                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();

                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String json;
                    while((json = bufferedReader.readLine())!= null){
                        Toast.makeText(MainActivity.this, json, Toast.LENGTH_SHORT).show();
                        sb.append(json+"\n");
                    }
                    JSONArray jsonPlaces = new JSONArray(sb.toString().trim());
                    return jsonPlaces.toString();

                }catch(Exception e){
                    return null;
                }

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();

                textViewJSON.setText(s);
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute(url);
    }
}
