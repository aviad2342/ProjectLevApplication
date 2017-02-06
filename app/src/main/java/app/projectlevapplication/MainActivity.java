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
//        if(Utils.getInstance().loadMemberFromPrefs(mPrefs) != null){
//            loginMember = Utils.getInstance().loadMemberFromPrefs(getPreferences(MODE_PRIVATE));
//            Toast.makeText(MainActivity.this,loginMember.getFullName(),Toast.LENGTH_LONG).show();
//        }else {
//            Toast.makeText(MainActivity.this,"naab",Toast.LENGTH_LONG).show();
//        }
        //getPreferences(MODE_PRIVATE).edit().clear().apply();
        //communityMembers.setEnabled(false);


       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       // getJSON(JSON_URL);
        //getData();
        //Picasso.with(this).load("https://s-media-cache-ak0.pinimg.com/originals/53/b1/2c/53b12cfc320db4029d7ce5f25702deb9.png").into(image);
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

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main_menu, menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//        communityMembers = menu.findItem(R.id.communityMembers);
//        select = menu.findItem(R.id.blaa);
//        return super.onPrepareOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.logIn:
//                LogInDialog dialog = new LogInDialog();
//                dialog.show(getFragmentManager(),"dialog");
//                return true;
//            case R.id.communityMembers:
//                Intent membersListIntent = new Intent(this, MembersListActivity.class);
//                startActivity(membersListIntent);
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

//    @Override
//    public void onDialogPositiveClick(LogInDialog dialog) {
//        loginMember = Utils.getInstance().loadMemberFromPrefs(mPrefs);
//        loading = ProgressDialog.show(MainActivity.this,"בבקשה המתן...","מחזיר מידע...",false,false);
//
//        String url = "http://arianlev.esy.es/ArianLev_Community/api/api.php?key=W2jFgx1leQ&opt=2&user="+loginMember.getUsername()+"&pass="+loginMember.getPassword();
//
//        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                loading.dismiss();
//                loginMember.setAdmin(Utils.getInstance().isAdmin(response));
//                Utils.getInstance().writeMemberToPrefs(mPrefs,loginMember);
//
//            }
//        },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(getApplicationContext(),"error", Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
//        requestQueue.add(stringRequest);
//    }

//    @Override
//    public void onDialogNegativeClick(LogInDialog dialog) {
//
//    }
//
//    @Override
//    public void onDialogRegisterClick(LogInDialog dialog) {
//        Toast.makeText(MainActivity.this,"NO",Toast.LENGTH_LONG).show();
//    }

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

//    private void getJSON(String url) {
//        class GetJSON extends AsyncTask<String, Void, String>{
//            ProgressDialog loading;
//
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//                loading = ProgressDialog.show(MainActivity.this, "Please Wait...",null,true,true);
//            }
//
//            @Override
//            protected String doInBackground(String... params) {
//
//                System.out.print(params[0]);
//
//                //Toast.makeText(MainActivity.this, params[0], Toast.LENGTH_SHORT).show();
//                String uri = params[0];
//
//                BufferedReader bufferedReader = null;
//                try {
//                    URL url = new URL(uri);
//                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
//                    StringBuilder sb = new StringBuilder();
//
//                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
//
//                    String json;
//                    while((json = bufferedReader.readLine())!= null){
//                        Toast.makeText(MainActivity.this, json, Toast.LENGTH_SHORT).show();
//                        sb.append(json+"\n");
//                    }
//                    JSONArray jsonPlaces = new JSONArray(sb.toString().trim());
//                    return jsonPlaces.toString();
//
//                }catch(Exception e){
//                    return null;
//                }
//
//            }
//
//            @Override
//            protected void onPostExecute(String s) {
//                super.onPostExecute(s);
//                loading.dismiss();
//
//                textViewJSON.setText(s);
//            }
//        }
//        GetJSON gj = new GetJSON();
//        gj.execute(url);
//    }
}
