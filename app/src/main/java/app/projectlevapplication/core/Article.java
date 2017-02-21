package app.projectlevapplication.core;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Article implements Serializable {
	
	private int articleID;
	
	private String headline;
	
	private Date publishDate;
	
	private String content;
	
	private int authorID;

	private String authorName;

    private int views;

    private int Comments;

    /**
     * Empty Constructor
     */
    public Article() {
    }

    /**
	 * Partial Constructor 
	 * @param articleID
	 */
	public Article(int articleID) {
		setArticleID(articleID);
	}
	
	/**
	 * Full Constructor
	 * @param articleID
	 * @param headline
	 * @param publishDate
	 * @param content
	 * @param authorID
	 * @param views
	 */
	public Article(int articleID, String headline, Date publishDate, String content, int authorID, int views) {
		setArticleID(articleID);
		setHeadline(headline);
		setPublishDate(publishDate);
		setContent(content);
		setAuthorID(authorID);
		setViews(views);
	}

	public int getArticleID() {
		return articleID;
	}

	public void setArticleID(int articleID) {
		this.articleID = articleID;
	}

	public String getHeadline() {
		return headline;
	}

	public void setHeadline(String headline) {
		this.headline = headline;
	}

	public Date getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getAuthorID() {
		return authorID;
	}

	public void setAuthorID(int authorID) {
		this.authorID = authorID;
	}

	public int getViews() {
		return views;
	}

	public void setViews(int views) {
		this.views = views;
	}

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public int getComments() {
        return Comments;
    }

    public void setComments(int comments) {
        Comments = comments;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + articleID;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Article other = (Article) obj;
		if (articleID != other.articleID)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Article{" +
				"headline='" + headline + '\'' +
				'}';
	}

	public JSONObject toJsonObject(){

		JSONObject jsonObject= new JSONObject();
		try {
			jsonObject.put("headline", getHeadline());
			jsonObject.put("content", getContent());
			jsonObject.put("author", getAuthorID());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonObject;
	}
}
