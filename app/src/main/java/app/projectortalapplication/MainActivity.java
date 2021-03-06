package app.projectortalapplication;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.Locale;

import app.projectortalapplication.model.NavDrawerItem;
import app.projectortalapplication.utils.Utils;
import app.projectortalapplication.viewComponents.AboutFragment;
import app.projectortalapplication.viewComponents.ArticlesListFragment;
import app.projectortalapplication.viewComponents.EventListFragment;
import app.projectortalapplication.viewComponents.HomeFragment;
import app.projectortalapplication.viewComponents.MembersListFragment;
import app.projectortalapplication.viewComponents.NavDrawerListAdapter;
import app.projectortalapplication.viewComponents.RegisterDialog;



public class MainActivity extends AppCompatActivity implements LogInDialog.DialogFragmentListener , RegisterDialog.DialogFragmentListener{

    private final int PERMISSIONS_REQ= 1212;
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
        if(Utils.getInstance().loadMemberFromPrefs(this) != null) {
            getNumberOfEvents();
        }

        sDefSystemLanguage = Locale.getDefault().getDisplayLanguage();

        mTitle = mDrawerTitle = getTitle();

        // load slide menu items
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        if(sDefSystemLanguage == "English"){
            mDrawerLayout.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }else if(sDefSystemLanguage != "עיברית"){
            mDrawerLayout.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);


        navDrawerItems = new ArrayList<NavDrawerItem>();
//        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], R.mipmap.ic_home_image));
//        // Community Members
//        navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], R.mipmap.ic_launcher));
//        // Community Articles
//        navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], R.mipmap.ic_nurse));
//        // Community events
//        navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], R.mipmap.ic_clock));
//        // Pages
//        navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], R.mipmap.ic_resalt));
//        // About
//        navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], R.mipmap.ic_prescription));
//
//        navDrawerItems.add(new NavDrawerItem(navMenuTitles[6], R.mipmap.ic_his));
//        // Community Members
//        navDrawerItems.add(new NavDrawerItem(navMenuTitles[7], R.mipmap.ic_notifications));
//        // Community Articles
//        navDrawerItems.add(new NavDrawerItem(navMenuTitles[8], R.mipmap.ic_events, true, "5"));
//        // Community events
//        navDrawerItems.add(new NavDrawerItem(navMenuTitles[9], R.mipmap.ic_doc));
//        // Pages
//        navDrawerItems.add(new NavDrawerItem(navMenuTitles[10], R.mipmap.ic_members));
//        // About
//        navDrawerItems.add(new NavDrawerItem(navMenuTitles[11], R.mipmap.ic_ambulanc));

        if(Utils.getInstance().loadMemberFromPrefs(this) != null) {

            // adding nav drawer items to array
            // Home
            navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], R.mipmap.ic_home_image));
            // Community Members
            navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], R.mipmap.ic_members));
            // Community Articles
            navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], R.mipmap.ic_article));
            // Community events
            navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], R.mipmap.ic_events, true, ""));
            // Pages
            navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], R.mipmap.ic_clock));
            // About
            navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], R.mipmap.ic_about));
        } else {
            // Home
            navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], R.mipmap.ic_home_image));
            // About
            navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], R.mipmap.ic_about));
        }

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

    public void getNumberOfEvents(){
        String url = Utils.NUMBER_OF_EVENTS;

        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
               // loading.dismiss();
                if(navDrawerItems.get(3) != null) {
                    navDrawerItems.get(3).setCount(Utils.getInstance().responseToEventsNumber(response));
                    adapter.notifyDataSetChanged();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                       // loading.dismiss();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    /**
     * Slide menu item click listener
     * */
    private class SlideMenuClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            hideKeyboard();
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
                if(Utils.getInstance().loadMemberFromPrefs(this) != null) {
                    fragment = new MembersListFragment();
                }else{
                    fragment = new AboutFragment();
                }
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
                fragment = new AboutFragment();
                break;
            default:
                fragment = new HomeFragment();
                setTitle(getString(R.string.app_name));
                break;
        }

        if (fragment != null) {
            setFragment(fragment,fragment.getClass().getSimpleName());
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
//        Toast.makeText(this,name,Toast.LENGTH_LONG).show();
//        if(name.equals("EventListFragment")){
//            getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//            //getFragmentManager().beginTransaction().add(new HomeFragment(), "HomeFragment").addToBackStack("HomeFragment").commit();
//        }
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right);
        ft.replace(R.id.frame_container, fragment,name);
        ft.addToBackStack(name);
        ft.commit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Fragment currentFragment = getFragmentManager().findFragmentById(R.id.frame_container);
            if(currentFragment != null) {
                if (!currentFragment.getClass().getSimpleName().equals("HomeFragment")) {
                    getFragmentManager().popBackStack();
                }
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
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
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.INTERNET)
                        != PackageManager.PERMISSION_GRANTED  ||
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,
                        android.Manifest.permission.WAKE_LOCK)
                        != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.SET_WALLPAPER)
                        != PackageManager.PERMISSION_GRANTED
                ){
            // No explanation needed, we can request the permission.
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            android.Manifest.permission.CAMERA, android.Manifest.permission.SET_WALLPAPER,
                            android.Manifest.permission.INTERNET, android.Manifest.permission.ACCESS_NETWORK_STATE,android.Manifest.permission.WAKE_LOCK},
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MemberLogin = menu.findItem(R.id.logIn);
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
            if(Utils.getInstance().loadMemberFromPrefs(this) != null) {
                getNumberOfEvents();
            }

            return true;
        }
        // Handle action bar actions click
        switch (item.getItemId()) {
            case R.id.logIn:
                if( mDrawerLayout.isDrawerOpen(Gravity.RIGHT)){
                    mDrawerLayout.closeDrawers();
                }
                if( mDrawerLayout.isDrawerOpen(Gravity.LEFT)){
                    mDrawerLayout.closeDrawers();
                }
                getFragmentManager().beginTransaction().replace(R.id.frame_container, new HomeFragment()).commit();
                if(Utils.getInstance().loadMemberFromPrefs(this) == null) {
                    LogInDialog dialog = new LogInDialog();
                    dialog.show(getFragmentManager(), "dialog");
                }else{
                    //getPreferences(MODE_PRIVATE).edit().clear().apply();
                    Utils.getInstance().writeMemberToPrefs(null,this);
                    MemberLogin.setIcon(R.drawable.ic_input_white_24dp);
                   if( mDrawerLayout.isDrawerOpen(Gravity.RIGHT)){
                       mDrawerLayout.closeDrawers();
                   }
                    if( mDrawerLayout.isDrawerOpen(Gravity.LEFT)){
                        mDrawerLayout.closeDrawers();
                    }
                    navDrawerItems.clear();
                    // Home
                    navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], R.mipmap.ic_home_image));
                    // About
                    navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], R.mipmap.ic_about));
                    adapter = new NavDrawerListAdapter(getApplicationContext(), navDrawerItems);
                    adapter.notifyDataSetChanged();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onDialogPositiveClick(LogInDialog dialog) {
        if(Utils.getInstance().loadMemberFromPrefs(this) != null){
            MemberLogin.setIcon(R.drawable.logout_24dp);
            getNumberOfEvents();
            navDrawerItems.clear();

            navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], R.mipmap.ic_home_image));
            // Community Members
            navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], R.mipmap.ic_members));
            // Community Articles
            navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], R.mipmap.ic_article));
            // Community events
            navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], R.mipmap.ic_events, true, ""));
            // Pages
            navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], R.mipmap.ic_home));
            // About
            navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], R.mipmap.ic_about));

            adapter = new NavDrawerListAdapter(getApplicationContext(), navDrawerItems);
            adapter.notifyDataSetChanged();
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
    private void hideKeyboard() {
        try {
            View view = this.getCurrentFocus();
            if (view != null) {
                final InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
