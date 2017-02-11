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

import app.projectlevapplication.R;
import app.projectlevapplication.core.Event;
import app.projectlevapplication.core.Member;
import app.projectlevapplication.utils.MyMenuBar;
import app.projectlevapplication.utils.Utils;

public class EventListActivity extends MyMenuBar {

    ListView list;
    EventsListAdapter adapter;
    public ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);
        getSupportActionBar().setTitle("אירועים");
        list = (ListView) findViewById(R.id.eventsList);
        loadEventsList();

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Event selectedItem = adapter.getItem(position);
                Intent intent = new Intent(EventListActivity.this, EventActivity.class);
                intent.putExtra("mEvent", (Serializable)selectedItem);
                startActivity(intent);
            }
        });
    }

    public void loadEventsList(){
        loading = ProgressDialog.show(EventListActivity.this,"בבקשה המתן...","מחזיר מידע...",false,false);

        String url = Utils.ALL_COMMUNITY_EVENTS;

        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                loading.dismiss();

                adapter = new EventsListAdapter(EventListActivity.this, Utils.getInstance().responseToEventList(response));
                list.setAdapter(adapter);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        Toast.makeText(getApplicationContext(),"error", Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(EventListActivity.this);
        requestQueue.add(stringRequest);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        loadEventsList();
    }
}
