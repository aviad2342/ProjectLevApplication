package app.projectlevapplication.utils;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import app.projectlevapplication.LogInDialog;
import app.projectlevapplication.MainActivity;
import app.projectlevapplication.R;
import app.projectlevapplication.core.Member;
import app.projectlevapplication.viewComponents.ArticlesListActivity;
import app.projectlevapplication.viewComponents.ArticlesListFragment;
import app.projectlevapplication.viewComponents.EventListActivity;
import app.projectlevapplication.viewComponents.EventListFragment;
import app.projectlevapplication.viewComponents.MembersListActivity;
import app.projectlevapplication.viewComponents.MembersListFragment;
import app.projectlevapplication.viewComponents.RegisterDialog;

/**
 * Created by user-pc on 28/01/2017.
 */

public class MyMenuBar extends AppCompatActivity implements LogInDialog.DialogFragmentListener , RegisterDialog.DialogFragmentListener{

    public MenuItem MemberLogin;



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        if(Utils.getInstance().loadMemberFromPrefs(this) != null){
            MemberLogin.setIcon(R.drawable.logout_24dp);
        }else {
            MemberLogin.setIcon(R.drawable.ic_input_white_24dp);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Fragment fragment = null;
        FragmentManager fragmentManager = getFragmentManager();
        switch (item.getItemId()) {
            case R.id.logIn:
                if(Utils.getInstance().loadMemberFromPrefs(this) == null) {
                    LogInDialog dialog = new LogInDialog();
                    dialog.show(getFragmentManager(), "dialog");
                }else{
                    //getPreferences(MODE_PRIVATE).edit().clear().apply();
                    Utils.getInstance().writeMemberToPrefs(null,this);
                    Intent homeIntent = new Intent(this, MainActivity.class);
                    startActivity(homeIntent);
                    MemberLogin.setIcon(R.drawable.ic_input_white_24dp);
                }
                return true;

            default:

        }
//        if (fragment != null) {
//            FragmentManager fragmentManager = getFragmentManager();
//            fragmentManager.beginTransaction()
//                    .replace(R.id.frame_container, fragment).commit();
//        }
        return super.onOptionsItemSelected(item);
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
