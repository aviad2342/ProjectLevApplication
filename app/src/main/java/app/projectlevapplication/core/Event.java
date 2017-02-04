package app.projectlevapplication.core;

import java.util.ArrayList;
import java.util.Date;

public class Event {

	private int eventID;
	
	private String title;
	
	private Date publishDate;
	
	private int capacity;
	
	private String location;
	
	private String description;
	
	private int publisherID;
	
	private ArrayList<Media> media;

    /**
     * Empty Constructor
     */
    public Event() {
    }

    /**
	 * Partial Constructor 
	 * @param eventID
	 */
	public Event (int eventID) {
		setEventID(eventID);
	}
	
	/**
	 * Full Constructor 
	 * @param eventID
	 * @param title
	 * @param capacity
	 * @param publishDate
	 * @param location
	 * @param description
	 * @param publisherID
	 * @param media
	 */
	public Event(int eventID, String title, int capacity, Date publishDate, String location, String description, int publisherID, ArrayList<Media> media) {
		setEventID(eventID);
		setTitle(title);
		setCapacity(capacity);
		setPublishDate(publishDate);
		setLocation(location);
		setDescription(description);
		setPublisherID(publisherID);
		setMedia(media);
	}

	public int getEventID() {
		return eventID;
	}

	public void setEventID(int eventID) {
		this.eventID = eventID;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getPublisherID() {
		return publisherID;
	}

	public void setPublisherID(int publisherID) {
		this.publisherID = publisherID;
	}

	
	
	public ArrayList<Media> getMedia() {
		return media;
	}

	public void setMedia(ArrayList<Media> media) {
		this.media = media;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + eventID;
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
		Event other = (Event) obj;
		if (eventID != other.eventID)
			return false;
		return true;
	}
	
	
	
}
