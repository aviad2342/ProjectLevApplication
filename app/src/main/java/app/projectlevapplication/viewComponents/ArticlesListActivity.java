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
import app.projectlevapplication.core.Article;
import app.projectlevapplication.core.Member;
import app.projectlevapplication.utils.Utils;

public class ArticlesListActivity extends AppCompatActivity {

    ListView list;
    ArticlesListAdapter adapter;
    public ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articles_list);

        list = (ListView) findViewById(R.id.articlesList);
        loadArticleList();

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Article selectedItem = adapter.getItem(position);
//                Intent intent = new Intent(ArticlesListActivity.this, MemberProfileActivity.class);
//                intent.putExtra("mArticle", (Serializable)selectedItem);
//                startActivity(intent);
            }
        });
    }

    public void loadArticleList(){
        loading = ProgressDialog.show(ArticlesListActivity.this,"בבקשה המתן...","מחזיר מידע...",false,false);

        String url = Utils.ALL_COMMUNITY_ARTICLES;

        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //System.out.print(response);
                loading.dismiss();
                // Utils.getInstance().responseToMembersList(response);
                adapter = new ArticlesListAdapter(ArticlesListActivity.this, Utils.getInstance().responseToArticleList(response));
                list.setAdapter(adapter);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),"error", Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(ArticlesListActivity.this);
        requestQueue.add(stringRequest);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        loadArticleList();
    }
}
