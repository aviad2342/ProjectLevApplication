package app.projectlevapplication.core;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import app.projectlevapplication.utils.Utils;


public class Member implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int memberID;
	
	private String username;
	
	private String password;
	
	private String fullName;
	
	private Date birthDate;
	
	private String email;
	
	/** user's gender: 0 = male, 1 = female, (-1) = didn't say */
	private int gender;
	
	/** user's status: 0 = single... */
	private int status;
	
	/** user's children number; (-1) means he didn't say */
	private int children;
	
	private String state;
	
	private String street;
	
	private int houseNum;
	
	private int zipCode;
	
	private int education;
	
	private String profilePic;
	
	private boolean isApproved;
	
	private Date registrationDate;
	
	private Date subExpire;
	
	private boolean sendMails;
	
	private boolean isAdmin;
	
	private ArrayList<Phone> phones;

    private String phoneNumber;

    /** 1 = mobile, 0 = home, 2 = office*/
    private int type;

    private boolean publish;


    /**
     * empty Constructor
     */
    public Member() {
    }

	/**
	 * Partial Constructor
	 * @param memberID
	 */
	public Member(int memberID) {
		setMemberID(memberID);
	}

	/**
	 *
	 * @param memberID
	 * @param username
	 * @param password
	 * @param fullName
	 * @param birthDate
	 * @param email
	 * @param gender
	 * @param status
	 * @param children
	 * @param houseNum
	 * @param zipCode
	 * @param education
	 * @param profilePic
	 * @param isApproved
     * @param registrationDate
     * @param subExpire
     * @param sendMails
     */
	public Member(int memberID, String username, String password, String fullName, Date birthDate, String email, int gender, int status, int children, int houseNum, int zipCode, int education, String profilePic, boolean isApproved, Date registrationDate, Date subExpire, boolean sendMails) {
		setMemberID(memberID);
		setUsername(username);
		setPassword(password);
		setFullName(fullName);
		setEmail(email);
		setBirthDate(birthDate);
		setGender(gender);
		setStatus(status);
		setChildren(children);
		setHouseNum(houseNum);
		setZipCode(zipCode);
		setEducation(education);
		setProfilePic(profilePic);
		setApproved(isApproved);
		setRegistrationDate(registrationDate);
		setSubExpire(subExpire);
		setSendMails(sendMails);

	}

	/**
	 * Full Constructor
	 * @param memberID
	 * @param username
	 * @param password
	 * @param fullName
	 * @param email
	 * @param birthDate
	 * @param gender
	 * @param status
	 * @param children
	 * @param state
	 * @param street
	 * @param houseNum
	 * @param zipCode
	 * @param education
	 * @param profilePic
	 * @param isApproved
	 * @param registrationDate
	 * @param subExpire
	 * @param sendMails
	 * @param isAdmin
	 */
	public Member(int memberID, String username, String password, String fullName, String email, Date birthDate, int gender, int status, int children, String state, String street, int houseNum, int zipCode, int education, String profilePic, boolean isApproved, Date registrationDate, Date subExpire, boolean sendMails, boolean isAdmin, ArrayList<Phone> phones) {
		setMemberID(memberID);
		setUsername(username);
		setPassword(password);
		setFullName(fullName);
		setEmail(email);
		setBirthDate(birthDate);
		setGender(gender);
		setStatus(status);
		setChildren(children);
		setState(state);
		setStreet(street);
		setHouseNum(houseNum);
		setZipCode(zipCode);
		setEducation(education);
		setProfilePic(profilePic);
		setApproved(isApproved);
		setRegistrationDate(registrationDate);
		setSubExpire(subExpire);
		setSendMails(sendMails);
		setAdmin(isAdmin);
		setPhones(phones);
	}

	public int getMemberID() {
		return memberID;
	}

	public void setMemberID(int memberID) {
		this.memberID = memberID;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getChildren() {
		return children;
	}

	public void setChildren(int children) {
		this.children = children;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public int getHouseNum() {
		return houseNum;
	}

	public void setHouseNum(int houseNum) {
		this.houseNum = houseNum;
	}

	public int getZipCode() {
		return zipCode;
	}

	public void setZipCode(int zipCode) {
		this.zipCode = zipCode;
	}

	public int getEducation() {
		return education;
	}

	public void setEducation(int education) {
		this.education = education;
	}

	public String getProfilePic() {
		return profilePic;
	}

	public void setProfilePic(String profilePic) {
		this.profilePic = profilePic;
	}

	public boolean isApproved() {
		return isApproved;
	}

	public void setApproved(boolean isApproved) {
		this.isApproved = isApproved;
	}

	public Date getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}

	public Date getSubExpire() {
		return subExpire;
	}

	public void setSubExpire(Date subExpire) {
		this.subExpire = subExpire;
	}

	public boolean isSendMails() {
		return sendMails;
	}

	public void setSendMails(boolean sendMails) {
		this.sendMails = sendMails;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
	
	public ArrayList<Phone> getPhones() {
		return phones;
	}

	public void setPhones(ArrayList<Phone> phones) {
		this.phones = phones;
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

	public String getMemberGender() {
        switch (gender){
            case 0:
                return "זכר";
            case 1:
                return "נקבה";
            default:
                return "לא צויין";
        }
	}

    public String getMemberStatus() {
        if(gender == 0) {
            switch (status){
                case 0:
                    return "רווק";
                case 1:
                    return "נשוי";
                case 2:
                    return "אלמן";
                case 3:
                    return "גרוש";
                default:
                    return "לא צויין";
            }
        }
        else if(gender == 1) {
            switch (status){
                case 0:
                    return "רווקה";
                case 1:
                    return "נשואה";
                case 2:
                    return "אלמנה";
                case 3:
                    return "גרושה";
                default:
                    return "לא צויין";
            }
        }
        else{
            return "לא צויין";
        }
    }

	public String getMemberAddress() {
		if(state != null && street != null && houseNum > 0){
			return street+" "+houseNum+", "+state;
		}
		return "לא צויינה כתובת";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + memberID;
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
		Member other = (Member) obj;
		if (memberID != other.memberID)
			return false;
		return true;
	}

    @Override
    public String toString() {
        return "Member{" +
                "memberID=" + memberID +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", fullName='" + fullName + '\'' +
                ", birthDate=" + birthDate +
                ", email='" + email + '\'' +
                ", gender=" + gender +
                ", status=" + status +
                ", children=" + children +
                ", state='" + state + '\'' +
                ", street='" + street + '\'' +
                ", houseNum=" + houseNum +
                ", zipCode=" + zipCode +
                ", education=" + education +
                ", profilePic='" + profilePic + '\'' +
                ", isApproved=" + isApproved +
                ", registrationDate=" + registrationDate +
                ", subExpire=" + subExpire +
                ", sendMails=" + sendMails +
                ", isAdmin=" + isAdmin +
                ", phones=" + phones +
                '}';
    }

	/**
	 * Convert Member Object Into JSONObject
	 * @return
     */
    public JSONObject toJsonObject(){
		JSONObject jsonObject= new JSONObject();
		try {
			jsonObject.put("username", getUsername());
			jsonObject.put("password", getPassword());
			jsonObject.put("fullName", getFullName());
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
			jsonObject.put("birthdate",format.format(getBirthDate()));
			jsonObject.put("email", getEmail());
			jsonObject.put("phoneNumber", getPhoneNumber());
			jsonObject.put("type", getType());
			jsonObject.put("publish", Utils.getBoolean(isPublish()));
			jsonObject.put("profilePic", getProfilePic());
			jsonObject.put("sendMails", Utils.getBoolean(isSendMails()));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonObject;
	}

}
