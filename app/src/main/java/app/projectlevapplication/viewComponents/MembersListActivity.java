package app.projectlevapplication.viewComponents;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.Serializable;

import app.projectlevapplication.LogInDialog;
import app.projectlevapplication.R;
import app.projectlevapplication.core.Member;
import app.projectlevapplication.utils.MyMenuBar;
import app.projectlevapplication.utils.Utils;

public class MembersListActivity extends MyMenuBar {

    ListView list;
    MembersListAdapter adapter;
    public ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_members_list);
        getSupportActionBar().setTitle("חברי קהילה");
        list = (ListView) findViewById(R.id.membersList);
        loadList();


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Member selectedItem = adapter.getItem(position);
                Intent intent = new Intent(MembersListActivity.this, MemberProfileActivity.class);
                intent.putExtra("mMember", (Serializable)selectedItem);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MemberLogin = menu.findItem(R.id.logIn);
        MemberLogout = menu.findItem(R.id.logout);
        communityMembers = menu.findItem(R.id.communityMembers);
        communityEvents = menu.findItem(R.id.events);
        communityArticles = menu.findItem(R.id.articles);
        MemberLogin.setIcon(R.drawable.logout_24dp);
        return super.onPrepareOptionsMenu(menu);
    }
    public void loadList(){
        loading = ProgressDialog.show(MembersListActivity.this,"בבקשה המתן...","מחזיר מידע...",false,false);

        String url = Utils.ALL_COMMUNITY_MEMBERS;

        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //System.out.print(response);
                loading.dismiss();
               // Utils.getInstance().responseToMembersList(response);
                adapter = new MembersListAdapter(MembersListActivity.this, Utils.getInstance().responseToMembersList(response));
                list.setAdapter(adapter);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),"error", Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(MembersListActivity.this);
        requestQueue.add(stringRequest);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //loadList();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        loadList();
    }

    @Override
    public void invalidateOptionsMenu() {
        super.invalidateOptionsMenu();
    }
}
