package app.projectlevapplication.core;

public class Phone {
	private int memberID;
	
	private String phoneNumber;
	
	/** 1 = mobile, 0 = home, 2 = office*/
	private int type;
	
	private boolean publish;

    /**
     *empty Constructor
     */
    public Phone() {
    }

    /**
	 * Partial Constructor
	 * @param phoneNumber
	 */
	public Phone(String phoneNumber) {
		setPhoneNumber(phoneNumber);
	}
	
	/**
	 * Full Constructor
	 * @param phoneNumber
	 * @param memberID
	 * @param publish
	 * @param type
	 */
	public Phone(String phoneNumber, int memberID, boolean publish, int type) {
		setPhoneNumber(phoneNumber);
		setMemberID(memberID);
		setPublish(publish);
		setType(type);
	}
	
	public int getMemberID() {
		return memberID;
	}

	public void setMemberID(int memberID) {
		this.memberID = memberID;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public boolean isPublish() {
		return publish;
	}

	public void setPublish(boolean publish) {
		this.publish = publish;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((phoneNumber == null) ? 0 : phoneNumber.hashCode());
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
		Phone other = (Phone) obj;
		if (phoneNumber == null) {
			if (other.phoneNumber != null)
				return false;
		} else if (!phoneNumber.equals(other.phoneNumber))
			return false;
		return true;
	}
	
	
}
