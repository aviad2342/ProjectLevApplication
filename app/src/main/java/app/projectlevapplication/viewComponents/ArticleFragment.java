package app.projectlevapplication.viewComponents;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.Date;

import app.projectlevapplication.R;
import app.projectlevapplication.core.Article;
import app.projectlevapplication.core.Comment;
import app.projectlevapplication.core.Member;
import app.projectlevapplication.utils.Utils;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class ArticleFragment extends Fragment {

    Context context;
    Activity activity;
    Article article;
    TextView title;
    TextView content;
    ListView list;
    ImageView commentMemberImage;
    TextView commentMemberName;
    EditText memberHeadline;
    EditText commentContent;
    Member currentMember;
    SharedPreferences mPrefs;
    Button submitComment;
    Button btnRemoveArticle;
    long start;
    long duration;

    CommentsListAdapter adapter;
    public ProgressDialog loading;

    public ArticleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.activity_article, container, false);
        start = System.currentTimeMillis();
        context = view.getContext();
        activity = getActivity();
        content = (TextView) view.findViewById(R.id.txtContent);
        title = (TextView) view.findViewById(R.id.articleTitle);
        title.requestFocus();
        list = (ListView) view.findViewById(R.id.commentListView);
        commentMemberImage = (ImageView) view.findViewById(R.id.commentMemberImage);
        commentMemberName = (TextView) view.findViewById(R.id.commentMemberName);
        commentContent = (EditText) view.findViewById(R.id.commentContent);
        memberHeadline = (EditText) view.findViewById(R.id.memberHeadline);
        btnRemoveArticle = (Button) view.findViewById(R.id.btnRemoveArticle);
        if(Utils.getInstance().loadMemberFromPrefs(context) != null){
            if(Utils.getInstance().loadMemberFromPrefs(context).isAdmin()){
                btnRemoveArticle.setVisibility(View.VISIBLE);
            }else {
                btnRemoveArticle.setVisibility(View.GONE);
            }
        }

        mPrefs = activity.getPreferences(MODE_PRIVATE);
        Bundle args = getArguments();
        article = (Article) args.getSerializable("mArticle");
        currentMember = Utils.getInstance().loadMemberFromPrefs(context);

        title.setPaintFlags(title.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        title.setText(article.getHeadline());

        content.setText(Html.fromHtml(article.getContent()));


        Picasso.with(activity).load(currentMember.getProfilePic()).into(commentMemberImage);
        commentMemberName.setText(currentMember.getFullName());
        loadCommentList();

        submitComment = (Button) view.findViewById(R.id.btnSendComment);
        submitComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (memberHeadline.length() > 0) {
                    Utils.hideSoftKeyboard(activity);
                    Comment commentToAdd = new Comment();
                    commentToAdd.setHeadline(memberHeadline.getText().toString());
                    commentToAdd.setPublishDate(new Date());
                    commentToAdd.setContent(commentContent.getText().toString());
                    commentToAdd.setAuthorID(currentMember.getMemberID());
                    commentToAdd.setArticleID(article.getArticleID());

                    loading = ProgressDialog.show(context,getString(R.string.article_uploading_comment_progress_dialog_message),getString(R.string.article_uploading_comment_progress_dialog_title),false,false);

                    String url = Utils.POST_COMMENT_FOR_ARTICLE;

                    JsonObjectRequest request_json = new JsonObjectRequest(url, commentToAdd.toJsonObject(),
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    loading.dismiss();
                                    Toast.makeText(context,getString(R.string.article_comment_success_message), Toast.LENGTH_SHORT).show();
                                    loadCommentList();
                                    memberHeadline.setText("");
                                    commentContent.setText("");
                                }
                            }
                            , new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            loading.dismiss();
                            Toast.makeText(context,getString(R.string.article_comment_error_message), Toast.LENGTH_LONG).show();
                            memberHeadline.setText("");
                            commentContent.setText("");
                        }
                    });

                    RequestQueue requestQueue = Volley.newRequestQueue(activity);
                    requestQueue.add(request_json);
                }
            }
        });

        btnRemoveArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage(R.string.article_remove_dialog_message);
                builder. setTitle(R.string.article_remove_dialog_title);
                builder.setPositiveButton(R.string.article_remove_dialog_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        loading = ProgressDialog.show(activity,getString(R.string.event_remove_progress_dialog_message),getString(R.string.event_remove_progress_dialog_title),false,false);
                        String url = Utils.REMOVE_ARTICLE;
                        JsonObjectRequest request_json = new JsonObjectRequest(url, Utils.articleIdToJsonObject(article.getArticleID()),
                                new com.android.volley.Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        loading.dismiss();
                                        Toast.makeText(activity,getString(R.string.article_remove_article_successfully),Toast.LENGTH_SHORT).show();
                                        getFragmentManager().popBackStack();
                                    }
                                }, new com.android.volley.Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                loading.dismiss();
                                Toast.makeText(activity,getString(R.string.article_remove_article_error),Toast.LENGTH_SHORT).show();
                                getFragmentManager().popBackStack();
                            }
                        });
                        RequestQueue requestQueue = Volley.newRequestQueue(activity);
                        requestQueue.add(request_json);

                    }
                });
                builder.setNegativeButton(R.string.article_remove_dialog_abort, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(context, getString(R.string.article_remove_abort_message),Toast.LENGTH_SHORT).show();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        return view;
    }

    public void loadCommentList(){
        loading = ProgressDialog.show(context,getString(R.string.progress_dialog_message),getString(R.string.progress_dialog_title),false,false);

        String url = Utils.ALL_COMMENTS_FOR_ARTICLE+article.getArticleID();

        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //System.out.print(response);
                loading.dismiss();
                // Utils.getInstance().responseToMembersList(response);
                adapter = new CommentsListAdapter(activity, Utils.getInstance().responseToCommentsList(response));
                list.setAdapter(adapter);
                //System.out.print(adapter.getItem(1).toJsonObject().toString());
                // Log.d("YourTag", adapter.getItem(1).toJsonObject().toString());
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
        JsonObjectRequest request_json = new JsonObjectRequest(url, Utils.UsageStatisticsToJsonObject(uID,Utils.milliToSeconds(duration),"מאמרים"),
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
