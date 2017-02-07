package app.projectlevapplication.viewComponents;

import android.graphics.Paint;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import app.projectlevapplication.R;
import app.projectlevapplication.core.Article;
import app.projectlevapplication.utils.MyMenuBar;

public class ArticleActivity extends MyMenuBar {

    Article article;
    TextView title;
    TextView content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        content = (TextView) findViewById(R.id.txtContent);
        title = (TextView) findViewById(R.id.articleTitle);

        article = (Article) getIntent().getSerializableExtra("mArticle");

        title.setPaintFlags(title.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        title.setText(article.getHeadline());

        content.setText(Html.fromHtml(article.getContent()));

    }
}
