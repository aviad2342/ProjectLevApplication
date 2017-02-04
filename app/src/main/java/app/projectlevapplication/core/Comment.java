package app.projectlevapplication.core;

import java.util.Date;

public class Comment {

private int commentID;
	
	private String headline;
	
	private Date publishDate;
	
	private String content;
	
	private int authorID;

	private String authorName;

    private String authorProfilePic;
	
	private int articleID;

    /**
     * Empty Constructor
     */
    public Comment() {
    }

    /**
	 * Partial Constructor 
	 * @param commentID
	 */
	public Comment(int commentID) {
		setCommentID(commentID);
	}
	
	/**
	 * Full Constructor 
	 * @param commentID
	 * @param headline
	 * @param publishDate
	 * @param content
	 * @param authorID
	 * @param articleID
	 */
	public Comment(int commentID, String headline, Date publishDate, String content, int authorID, int articleID) {
		setCommentID(commentID);
		setHeadline(headline);
		setPublishDate(publishDate);
		setContent(content);
		setAuthorID(authorID);
		setArticleID(articleID);
	}

    public Comment(String headline, Date publishDate, String content, int authorID, String authorName, String authorProfilePic, int articleID) {
        setCommentID(commentID);
        setHeadline(headline);
        setPublishDate(publishDate);
        setContent(content);
        setAuthorID(authorID);
        setAuthorName(authorName);
        setAuthorProfilePic(authorProfilePic);
        setArticleID(articleID);
    }

    public int getCommentID() {
		return commentID;
	}

	public void setCommentID(int commentID) {
		this.commentID = commentID;
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

	public int getArticleID() {
		return articleID;
	}

	public void setArticleID(int articleID) {
		this.articleID = articleID;
	}

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorProfilePic() {
        return authorProfilePic;
    }

    public void setAuthorProfilePic(String authorProfilePic) {
        this.authorProfilePic = authorProfilePic;
    }

    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + commentID;
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
		Comment other = (Comment) obj;
		if (commentID != other.commentID)
			return false;
		return true;
	}
	
	
	
}
