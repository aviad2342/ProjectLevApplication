package app.projectlevapplication.viewComponents;

import android.app.Activity;
import android.content.Context;
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
import app.projectlevapplication.core.Article;


/**
 * Created by user-pc on 29/01/2017.
 */

public class ArticlesListAdapter extends ArrayAdapter<Article> {

    private List<Article> articles = null;
    private Activity context = null;

    public ArticlesListAdapter(Context context, List<Article> articles) {
        super(context, R.layout.articles_list, articles);
        this.articles = articles;
        this.context = (Activity)context;
    }
    @Override
    public int getCount() {
        return articles.size();
    }

    @Override
    public Article getItem(int position) {
        return articles.get(position);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        View row = view;
        if(row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getLayoutInflater();
            row = inflater.inflate(R.layout.articles_list, null, true);
        }
        TextView articleName = (TextView) row.findViewById(R.id.articleName);
        TextView authorName = (TextView) row.findViewById(R.id.authorName);
        TextView publishDate = (TextView) row.findViewById(R.id.publishDate);
        TextView viewsNumber = (TextView) row.findViewById(R.id.viewsNumber);
        TextView commentsNumber = (TextView) row.findViewById(R.id.commentsNumber);

        Article article = articles.get(position);

        articleName.setText(article.getHeadline());
        //authorName.setText(article.get);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        publishDate.setText(format.format(article.getPublishDate()));
        viewsNumber.setText(article.getViews());
        //commentsNumber.setText(article.get);

        return row;

    }
}
