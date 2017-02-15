package app.projectlevapplication.viewComponents;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;

import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.FilterQueryProvider;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.Serializable;

import app.projectlevapplication.R;
import app.projectlevapplication.core.Article;
import app.projectlevapplication.core.Member;
import app.projectlevapplication.utils.MyMenuBar;
import app.projectlevapplication.utils.Utils;

public class ArticlesListActivity extends MyMenuBar {

    ListView list;
    ArrayAdapter<String> headlineAdapter;
    TextView searchArticle;
    ArticlesListAdapter adapter;
    public ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_articles_list);

        list = (ListView) findViewById(R.id.articlesList);
        searchArticle = (TextView) findViewById(R.id.searchInArticels);
        searchArticle.addTextChangedListener(filterTextWatcher);
        loadArticleList();

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Article selectedItem = adapter.getItem(position);
                Intent intent = new Intent(ArticlesListActivity.this, ArticleActivity.class);
                intent.putExtra("mArticle", (Serializable)selectedItem);
                startActivity(intent);
            }
        });
    }
    private TextWatcher filterTextWatcher = new TextWatcher() {

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            if (adapter != null) {
                adapter.getFilter().filter(s);
            } else {
                Log.d("filter", "no filter availible");
            }
        }
    };

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
               // headlineAdapter = new ArrayAdapter<String>(ArticlesListActivity.this,R.layout.auto_complete_text_item,Utils.getInstance().responseToArticleHeadlineList(response));
               // searchArticel.setAdapter(headlineAdapter);

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
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
