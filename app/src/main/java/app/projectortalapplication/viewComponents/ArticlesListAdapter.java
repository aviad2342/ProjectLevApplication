package app.projectortalapplication.viewComponents;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import app.projectortalapplication.R;
import app.projectortalapplication.core.Article;
import app.projectortalapplication.utils.Utils;


/**
 * Created by user-pc on 29/01/2017.
 */

public class ArticlesListAdapter extends ArrayAdapter<Article> implements Filterable {

    private List<Article> articles;
    private Activity context;
    private Filter filter;

    public ArticlesListAdapter(Context context, List<Article> articles) {
        super(context, R.layout.articles_list, articles);
        this.articles = articles;
        this.context = (Activity) context;
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
    public Filter getFilter() {
        if (filter == null)
            filter = new AppFilter<Article>(articles);
        return filter;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        View row = view;
        if (row == null) {
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
        authorName.setText(article.getAuthorName());
        publishDate.setText(Utils.eventToDateString(article.getPublishDate()));
        viewsNumber.setText(String.valueOf(article.getViews()));
        commentsNumber.setText(String.valueOf(article.getComments()));

        return row;

    }

    private class AppFilter<T> extends Filter {

        private ArrayList<T> sourceObjects;

        public AppFilter(List<T> articles) {
            sourceObjects = new ArrayList<T>();
            synchronized (this) {
                sourceObjects.addAll(articles);
            }
        }

        @Override
        protected FilterResults performFiltering(CharSequence chars) {
            String filterSeq = chars.toString().toLowerCase();
            FilterResults result = new FilterResults();
            if (filterSeq != null && filterSeq.length() > 0) {
                ArrayList<T> filter = new ArrayList<T>();

                for (T object : sourceObjects) {
                    // the filtering itself:
                    if (object.toString().toLowerCase().contains(filterSeq))
                        filter.add(object);
                }
                result.count = filter.size();
                result.values = filter;
            } else {
                // add all objects
                synchronized (this) {
                    result.values = sourceObjects;
                    result.count = sourceObjects.size();
                }
            }
            return result;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            // NOTE: this function is *always* called from the UI thread.
            ArrayList<T> filtered = (ArrayList<T>) results.values;
            notifyDataSetChanged();
            clear();
            for (int i = 0, l = filtered.size(); i < l; i++)
                add((Article) filtered.get(i));
            notifyDataSetInvalidated();
        }
    }
}