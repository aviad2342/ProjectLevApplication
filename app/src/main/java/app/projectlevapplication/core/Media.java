package app.projectlevapplication.core;

import java.io.Serializable;

public class Media implements Serializable {

	private int mediaID;
	
	private int eventID;
	
	private String fileName;
	
	/**
	 * Partial Constructor 
	 * @param mediaID
	 */
	public Media(int mediaID) {
		setMediaID(mediaID);
	}
	
	/**
	 * Full Constructor 
	 * @param mediaID
	 * @param eventID
	 * @param fileName
	 */
	public Media(int mediaID,int eventID, String fileName) {
		setMediaID(mediaID);
		setEventID(mediaID);
		setFileName(fileName);
	}
	
	
	public int getMediaID() {
		return mediaID;
	}

	public void setMediaID(int mediaID) {
		this.mediaID = mediaID;
	}

	public int getEventID() {
		return eventID;
	}

	public void setEventID(int eventID) {
		this.eventID = eventID;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + mediaID;
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
		Media other = (Media) obj;
		if (mediaID != other.mediaID)
			return false;
		return true;
	}
	
	
	
}
