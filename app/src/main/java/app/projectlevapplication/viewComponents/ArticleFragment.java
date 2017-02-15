package app.projectlevapplication.viewComponents;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
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

    CommentsListAdapter adapter;
    public ProgressDialog loading;

    public ArticleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.activity_article, container, false);
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

        return view;
    }

    public void loadCommentList(){
        loading = ProgressDialog.show(activity,"בבקשה המתן...","מחזיר מידע...",false,false);

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
                        Toast.makeText(context,"error", Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
    }
}
