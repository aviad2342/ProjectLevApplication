package app.projectlevapplication;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Config;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
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
import java.util.ArrayList;
import java.util.Locale;

import app.projectlevapplication.core.Member;
import app.projectlevapplication.model.LinkToDataBase;
import app.projectlevapplication.model.NavDrawerItem;
import app.projectlevapplication.utils.MyMenuBar;
import app.projectlevapplication.utils.Utils;
import app.projectlevapplication.viewComponents.ArticlesListFragment;
import app.projectlevapplication.viewComponents.EventListActivity;
import app.projectlevapplication.viewComponents.EventListFragment;
import app.projectlevapplication.viewComponents.EventsListAdapter;
import app.projectlevapplication.viewComponents.HomeFragment;
import app.projectlevapplication.viewComponents.MembersListActivity;
import app.projectlevapplication.viewComponents.MembersListFragment;
import app.projectlevapplication.viewComponents.NavDrawerListAdapter;
import app.projectlevapplication.viewComponents.RegisterDialog;



public class MainActivity extends AppCompatActivity implements LogInDialog.DialogFragmentListener , RegisterDialog.DialogFragmentListener{

    private final int PERMISSIONS_REQ= 1212;
    private static final String JSON_URL = "http://arianlev.esy.es/ArianLev_Community/api/api.php?key=W2jFgx1leQ&opt=0&user=Admin&pass=Admin";
    public ProgressDialog loading;
    SharedPreferences mPrefs;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    public MenuItem MemberLogin;
    public String sDefSystemLanguage;

    // nav drawer title
    private CharSequence mDrawerTitle;

    // used to store app title
    private CharSequence mTitle;

    // slide menu items
    private String[] navMenuTitles;

    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPrefs = getPreferences(MODE_PRIVATE);
        permissionsRequest();
        sDefSystemLanguage = Locale.getDefault().getDisplayLanguage();
       // getSupportActionBar().setIcon(R.mipmap.ic_lev);

        //txt.setText(Utils.getInstance().loadMemberFromPrefs(this).toString());
        mTitle = mDrawerTitle = getTitle();

        // load slide menu items
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        //Toast.makeText(getApplicationContext(),Locale.getDefault().getDisplayLanguage(), Toast.LENGTH_LONG).show();
        if(sDefSystemLanguage == "English"){
            mDrawerLayout.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }else if(sDefSystemLanguage != "עיברית"){
            mDrawerLayout.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

        setFragment(new HomeFragment(),"HomeFragment");

        navDrawerItems = new ArrayList<NavDrawerItem>();

        // adding nav drawer items to array
        // Home
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], R.mipmap.ic_home_image));
        // Find People
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], R.mipmap.ic_members));
        // Photos
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], R.mipmap.ic_article));
        // Communities, Will add a counter here
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[3],R.mipmap.ic_events, true, "22"));
        // Pages
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[4],R.mipmap.ic_home));
        // What's hot, We  will add a counter here
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], R.mipmap.ic_about, true, "50+"));


        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

        // setting the nav drawer list adapter
        adapter = new NavDrawerListAdapter(getApplicationContext(), navDrawerItems);
        mDrawerList.setAdapter(adapter);

        // enabling action bar app icon and behaving it as toggle button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name // nav drawer close - description for accessibility
        ) {
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(mTitle);
                // calling onPrepareOptionsMenu() to show action bar icons
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(mDrawerTitle);
                // calling onPrepareOptionsMenu() to hide action bar icons
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.addDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            // on first time display view for first nav item
            displayView(0);
        }
    }
//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        // TODO Auto-generated method stub
//        super.onWindowFocusChanged(hasFocus);
//        if (hasFocus) {
//            getFragmentManager().beginTransaction().replace(R.id.frame_container, new HomeFragment()).commit();
//        }
//    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        getFragmentManager().beginTransaction().replace(R.id.frame_container, new HomeFragment()).commit();
//    }

    /**
     * Slide menu item click listener
     * */
    private class SlideMenuClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // display view for selected nav drawer item
            hideSoftKeyboard(MainActivity.this);
            displayView(position);
        }
    }

    /**
     * Diplaying fragment view for selected nav drawer list item
     * */
    private void displayView(int position) {
        // update the main content by replacing fragments
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new HomeFragment();
                setTitle(getString(R.string.app_name));
                break;
            case 1:
                fragment = new MembersListFragment();
                break;
            case 2:
                fragment = new ArticlesListFragment();
                break;
            case 3:
                fragment = new EventListFragment();
                break;
            case 4:
                //fragment = new PagesFragment();
                break;
            case 5:
                //fragment = new WhatsHotFragment();
                break;

            default:
                break;
        }

        if (fragment != null) {
            setFragment(fragment,fragment.getClass().getName());
           // FragmentManager fragmentManager = getFragmentManager();
            //fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();

            // update selected item and title, then close the drawer
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            if(position != 0) {
                setTitle(navMenuTitles[position]);
            }
            mDrawerLayout.closeDrawer(mDrawerList);
        } else {
            // error in creating fragment
            Log.e("MainActivity", "Error in creating fragment");
        }
    }

    public void setFragment(Fragment fragment, String name) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right);
        ft.replace(R.id.frame_container, fragment,name);
        ft.addToBackStack(name);
        ft.commit();
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        if(sDefSystemLanguage == "עיברית"){
            mDrawerLayout.setLayoutDirection(View.LAYOUT_DIRECTION_LOCALE);
        }
        mDrawerToggle.onConfigurationChanged(newConfig);
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
                            android.Manifest.permission.CAMERA, android.Manifest.permission.INTERNET, android.Manifest.permission.ACCESS_NETWORK_STATE},
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
           // txt.setText(Utils.getInstance().responseToMember(response).toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MemberLogin = menu.findItem(R.id.logIn);
        // if nav drawer is opened, hide the action items
//        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
//        menu.findItem(R.id.menu).setVisible(!drawerOpen);
        if(Utils.getInstance().loadMemberFromPrefs(this) != null){
            MemberLogin.setIcon(R.drawable.logout_24dp);
        }else {
            MemberLogin.setIcon(R.drawable.ic_input_white_24dp);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action bar actions click
        switch (item.getItemId()) {
            case R.id.logIn:
                getFragmentManager().beginTransaction().replace(R.id.frame_container, new HomeFragment()).commit();
                if(Utils.getInstance().loadMemberFromPrefs(this) == null) {
                    LogInDialog dialog = new LogInDialog();
                    dialog.show(getFragmentManager(), "dialog");
                }else{
                    //getPreferences(MODE_PRIVATE).edit().clear().apply();
                    Utils.getInstance().writeMemberToPrefs(null,this);
                    MemberLogin.setIcon(R.drawable.ic_input_white_24dp);
                }
                return true;
//            case R.id.menu:
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onDialogPositiveClick(LogInDialog dialog) {
        if(Utils.getInstance().loadMemberFromPrefs(this) != null){
            MemberLogin.setIcon(R.drawable.logout_24dp);
        }
    }

    @Override
    public void onDialogNegativeClick(LogInDialog dialog) {

    }

    @Override
    public void onDialogRegisterClick(LogInDialog dialog) {
        RegisterDialog registerDialog = new RegisterDialog();
        registerDialog.show(getFragmentManager(), "registerDialog");
    }

    @Override
    public void onDialogPositive(RegisterDialog dialog) {

    }

    @Override
    public void onDialogNegative(RegisterDialog dialog) {

    }
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }
}
