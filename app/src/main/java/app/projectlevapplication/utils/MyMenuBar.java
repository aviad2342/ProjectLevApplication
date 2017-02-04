package app.projectlevapplication.utils;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import app.projectlevapplication.LogInDialog;
import app.projectlevapplication.R;
import app.projectlevapplication.viewComponents.ArticlesListActivity;
import app.projectlevapplication.viewComponents.EventListActivity;
import app.projectlevapplication.viewComponents.MembersListActivity;

/**
 * Created by user-pc on 28/01/2017.
 */

public class MyMenuBar extends AppCompatActivity {

    public static MenuItem communityMembers;
    public static MenuItem communityEvents;
    public static MenuItem communityArticles;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        communityMembers = menu.findItem(R.id.communityMembers);
        communityEvents = menu.findItem(R.id.events);
        communityArticles = menu.findItem(R.id.articles);
        return super.onPrepareOptionsMenu(menu);
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
            case R.id.events:
                Intent eventsListIntent = new Intent(this, EventListActivity.class);
                startActivity(eventsListIntent);
                return true;
            case R.id.articles:
                Intent articlesListIntent = new Intent(this, ArticlesListActivity.class);
                startActivity(articlesListIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
