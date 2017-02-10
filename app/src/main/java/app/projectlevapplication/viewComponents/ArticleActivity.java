package app.projectlevapplication.viewComponents;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import app.projectlevapplication.R;
import app.projectlevapplication.core.Article;
import app.projectlevapplication.core.Comment;
import app.projectlevapplication.core.Member;
import app.projectlevapplication.utils.MyMenuBar;
import app.projectlevapplication.utils.Utils;

public class ArticleActivity extends MyMenuBar {

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

    CommentsListAdapter adapter;
    public ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        content = (TextView) findViewById(R.id.txtContent);
        title = (TextView) findViewById(R.id.articleTitle);
        list = (ListView) findViewById(R.id.commentListView);
        commentMemberImage = (ImageView) findViewById(R.id.commentMemberImage);
        commentMemberName = (TextView) findViewById(R.id.commentMemberName);
        commentContent = (EditText) findViewById(R.id.commentContent);
        memberHeadline = (EditText) findViewById(R.id.memberHeadline);

        mPrefs = getPreferences(MODE_PRIVATE);

        article = (Article) getIntent().getSerializableExtra("mArticle");
        currentMember = Utils.getInstance().loadMemberFromPrefs(this);

        title.setPaintFlags(title.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        title.setText(article.getHeadline());

        content.setText(Html.fromHtml(article.getContent()));


        Picasso.with(this).load(currentMember.getProfilePic()).into(commentMemberImage);
        commentMemberName.setText(currentMember.getFullName());
        loadCommentList();

        submitComment = (Button) findViewById(R.id.btnSendComment);
        submitComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (memberHeadline.length() > 0) {
                    Comment commentToAdd = new Comment();
                    commentToAdd.setHeadline(memberHeadline.getText().toString());
                    commentToAdd.setPublishDate(new Date());
                    commentToAdd.setContent(commentContent.getText().toString());
                    commentToAdd.setAuthorID(currentMember.getMemberID());
                    commentToAdd.setArticleID(article.getArticleID());

                    loading = ProgressDialog.show(ArticleActivity.this,"בבקשה המתן...","מחזיר מידע...",false,false);

                    String url = Utils.POST_COMMENT_FOR_ARTICLE;

                    JsonObjectRequest request_json = new JsonObjectRequest(url, commentToAdd.toJsonObject(),
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Toast.makeText(getApplicationContext(),"success", Toast.LENGTH_SHORT).show();
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(),"error", Toast.LENGTH_SHORT).show();
                        }
                    });

                    RequestQueue requestQueue = Volley.newRequestQueue(ArticleActivity.this);
                    requestQueue.add(request_json);
                }
            }
        });
    }

    public void loadCommentList(){
        loading = ProgressDialog.show(ArticleActivity.this,"בבקשה המתן...","מחזיר מידע...",false,false);

        String url = Utils.ALL_COMMENTS_FOR_ARTICLE+article.getArticleID();

        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //System.out.print(response);
                loading.dismiss();
                // Utils.getInstance().responseToMembersList(response);
                adapter = new CommentsListAdapter(ArticleActivity.this, Utils.getInstance().responseToCommentsList(response));
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
                        Toast.makeText(getApplicationContext(),"error", Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(ArticleActivity.this);
        requestQueue.add(stringRequest);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        loadCommentList();
    }
}
