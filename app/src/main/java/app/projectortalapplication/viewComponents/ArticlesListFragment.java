package app.projectortalapplication.viewComponents;


import android.app.Activity;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.FragmentTransaction;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import app.projectortalapplication.R;
import app.projectortalapplication.core.Article;
import app.projectortalapplication.utils.Utils;

import static app.projectortalapplication.MainActivity.hideSoftKeyboard;

/**
 * A simple {@link Fragment} subclass.
 */
public class ArticlesListFragment extends Fragment {

    ListView list;
    ArrayAdapter<String> headlineAdapter;
    TextView searchArticle;
    ArticlesListAdapter adapter;
    ProgressDialog loading;
    Context context;
    Activity activity;
    FragmentManager fragmentManager;
    Button newItemBtn = null;
    long start;
    long duration;


    public ArticlesListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_articles_list, container, false);
        start = System.currentTimeMillis();
        context = view.getContext();
        activity = getActivity();
        fragmentManager = activity.getFragmentManager();

        list = (ListView) view.findViewById(R.id.articlesList);
        searchArticle = (TextView) view.findViewById(R.id.searchInArticels);
        searchArticle.addTextChangedListener(filterTextWatcher);
        newItemBtn = (Button) view.findViewById(R.id.new_article_btn);
        if (Utils.getInstance().loadMemberFromPrefs(context) != null) {
            if (Utils.getInstance().loadMemberFromPrefs(context).isAdmin()) {
                newItemBtn.setVisibility(View.VISIBLE);
            } else {
            newItemBtn.setVisibility(View.INVISIBLE);
        }
    }
        loadArticleList();

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                hideSoftKeyboard(activity);
                Article selectedItem = adapter.getItem(position);
                postArticleWatch(selectedItem.getArticleID());
                Fragment fragment = new ArticleFragment();
                Bundle args = new Bundle();
                args.putSerializable("mArticle",(Serializable)selectedItem);
                fragment.setArguments(args);
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right);
                ft.replace(R.id.frame_container, fragment,"ArticleFragment");
                ft.addToBackStack("ArticleFragment");
                ft.commit();
            }
        });

        newItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new AddNewArticleFragment();
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.setCustomAnimations(R.animator.slide_in_up, R.animator.slide_out_right);
                ft.replace(R.id.frame_container, fragment,"AddNewArticleFragment");
                ft.addToBackStack("AddNewArticleFragment");
                ft.commit();
            }
        });
        return view;
    }
    private TextWatcher filterTextWatcher = new TextWatcher() {

        @Override
        public void afterTextChanged(Editable s) {

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

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
        loading = ProgressDialog.show(context,getString(R.string.progress_dialog_message),getString(R.string.progress_dialog_title),false,false);

        String url = Utils.ALL_COMMUNITY_ARTICLES;

        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //System.out.print(response);
                loading.dismiss();
                // Utils.getInstance().responseToMembersList(response);
                adapter = new ArticlesListAdapter(activity, Utils.getInstance().responseToArticleList(response));
                list.setAdapter(adapter);
                // headlineAdapter = new ArrayAdapter<String>(ArticlesListActivity.this,R.layout.auto_complete_text_item,Utils.getInstance().responseToArticleHeadlineList(response));
                // searchArticel.setAdapter(headlineAdapter);

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        Toast.makeText(context,getString(R.string.error_server_request), Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
    }
    public void postArticleWatch(int articleId) {
            String url = Utils.POST_ARTICLE_IS_WATCHED;
            JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("articleID", String.valueOf(articleId));
        } catch (JSONException e) {
            e.printStackTrace();
        }
            JsonObjectRequest request_json = new JsonObjectRequest(url, jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                        }
                    }
                    , new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            RequestQueue requestQueue = Volley.newRequestQueue(activity);
            requestQueue.add(request_json);
    }

    @Override
    public void onPause() {
        super.onPause();
        // Loading Usage Statistics To Server Using Request_json
        duration = System.currentTimeMillis() - start;
        int uID;
        if(Utils.getInstance().loadMemberFromPrefs(context) != null){
            uID = Utils.getInstance().loadMemberFromPrefs(context).getMemberID();
        }else{
            uID = 27;
        }
        String url = Utils.POST_USAGE_STATISTICS;
            JsonObjectRequest request_json = new JsonObjectRequest(url, Utils.UsageStatisticsToJsonObject(uID,Utils.milliToSeconds(duration),"רשימת מאמרים"),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                        }
                    }
                    , new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });
        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(request_json);
    }
}
