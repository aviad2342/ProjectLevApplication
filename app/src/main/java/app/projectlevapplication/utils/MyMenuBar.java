package app.projectlevapplication.utils;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import app.projectlevapplication.LogInDialog;
import app.projectlevapplication.MainActivity;
import app.projectlevapplication.R;
import app.projectlevapplication.core.Member;
import app.projectlevapplication.viewComponents.ArticlesListActivity;
import app.projectlevapplication.viewComponents.EventListActivity;
import app.projectlevapplication.viewComponents.MembersListActivity;
import app.projectlevapplication.viewComponents.RegisterDialog;

/**
 * Created by user-pc on 28/01/2017.
 */

public class MyMenuBar extends AppCompatActivity implements LogInDialog.DialogFragmentListener , RegisterDialog.DialogFragmentListener{

    public MenuItem MemberLogin;
    public MenuItem MemberLogout;
    public MenuItem communityMembers;
    public MenuItem communityEvents;
    public MenuItem communityArticles;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MemberLogin = menu.findItem(R.id.logIn);
        MemberLogout = menu.findItem(R.id.logout);
        communityMembers = menu.findItem(R.id.communityMembers);
        communityEvents = menu.findItem(R.id.events);
        communityArticles = menu.findItem(R.id.articles);
        if( (Utils.currentMemberLogin = Utils.getInstance().loadMemberFromPrefs(this)) != null){
            MemberLogin.setIcon(R.drawable.logout_24dp);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logIn:
                if(Utils.getInstance().loadMemberFromPrefs(this) == null) {
                    LogInDialog dialog = new LogInDialog();
                    dialog.show(getFragmentManager(), "dialog");
                }else{
                    //getPreferences(MODE_PRIVATE).edit().clear().apply();
                    Utils.getInstance().writeMemberToPrefs(null,this);
                    MemberLogin.setIcon(R.drawable.ic_input_white_24dp);
                }
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
}
