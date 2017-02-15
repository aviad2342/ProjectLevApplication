package app.projectlevapplication.viewComponents;


import android.app.Activity;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.FragmentTransaction;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.Serializable;

import app.projectlevapplication.MainActivity;
import app.projectlevapplication.R;
import app.projectlevapplication.core.Article;
import app.projectlevapplication.utils.Utils;

import static app.projectlevapplication.MainActivity.hideSoftKeyboard;

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


    public ArticlesListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.activity_articles_list, container, false);
        context = view.getContext();
        activity= getActivity();
        fragmentManager = activity.getFragmentManager();

        list = (ListView) view.findViewById(R.id.articlesList);
        searchArticle = (TextView) view.findViewById(R.id.searchInArticels);
        searchArticle.addTextChangedListener(filterTextWatcher);
        loadArticleList();

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                hideSoftKeyboard(activity);
                Article selectedItem = adapter.getItem(position);
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

        return view;
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
        loading = ProgressDialog.show(activity,"בבקשה המתן...","מחזיר מידע...",false,false);

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
                        Toast.makeText(context,"error", Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
    }

}
