package app.projectlevapplication.viewComponents;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import app.projectlevapplication.utils.Utils;

public class MembersListActivity extends AppCompatActivity {

    ListView list;
    MembersListAdapter adapter;
    public ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_members_list);

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

    public void loadList(){
        loading = ProgressDialog.show(MembersListActivity.this,"Please wait...","Fetching...",false,false);
        String url = "http://arianlev.esy.es/ArianLev_Community/api/api.php?key=W2jFgx1leQ&opt=1";

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
}
