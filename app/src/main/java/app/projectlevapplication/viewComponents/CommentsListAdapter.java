package app.projectlevapplication.viewComponents;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.List;

import app.projectlevapplication.R;
import app.projectlevapplication.core.Comment;
import app.projectlevapplication.core.Member;
import app.projectlevapplication.utils.Utils;

/**
 * Created by Aviad on 08/02/2017.
 */

public class CommentsListAdapter extends ArrayAdapter<Comment> {

    private List<Comment> comments = null;
    private Activity context = null;

    public CommentsListAdapter(Context context, List<Comment> comments) {
        super(context, R.layout.comment_list, comments);
        this.comments = comments;
        this.context = (Activity)context;
    }
    @Override
    public int getCount() {
        return comments.size();
    }

    @Override
    public Comment getItem(int position) {
        return comments.get(position);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        View row = view;
        if(row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getLayoutInflater();
            row = inflater.inflate(R.layout.comment_list, null, true);
        }
        ImageView image = (ImageView) row.findViewById(R.id.commentListImage);
        TextView txtName = (TextView) row.findViewById(R.id.commentListMember);
        TextView commentDateTime = (TextView) row.findViewById(R.id.commentDateTime);
        TextView commentHeadline  = (TextView) row.findViewById(R.id.commentHeadline);
        TextView content = (TextView) row.findViewById(R.id.commentListContent);

        Comment comment = comments.get(position);


        Picasso.with(context).load(comment.getAuthorProfilePic()).into(image);
        txtName.setText(comment.getAuthorName());
        commentHeadline.setText(comment.getHeadline());
        //SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        commentDateTime.setText(Utils.eventToDateString(comment.getPublishDate()));
        content.setText(Html.fromHtml(comment.getContent()));

        return row;

    }
}
