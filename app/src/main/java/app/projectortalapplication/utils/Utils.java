package app.projectortalapplication.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.preference.PreferenceManager;
import android.view.inputmethod.InputMethodManager;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import app.projectortalapplication.core.Article;
import app.projectortalapplication.core.Comment;
import app.projectortalapplication.core.Event;
import app.projectortalapplication.core.Member;
import app.projectortalapplication.core.Phone;

/**
 * Created by user-pc on 24/01/2017.
 */

public class Utils {

    // --------------------------------------------------------Data URLs----------------------------------------------------------------------------

    public static final String ALL_COMMUNITY_MEMBERS = "http://ortalitah.com/api/api.php?key=W2jFgx1leQ&opt=1";

    public static final String ALL_COMMUNITY_ARTICLES = "http://ortalitah.com/api/api.php?key=W2jFgx1leQ&opt=6";

    public static final String ALL_COMMUNITY_EVENTS = "http://ortalitah.com/api/api.php?key=W2jFgx1leQ&opt=8";

    public static final String ALL_COMMENTS_FOR_ARTICLE = "http://ortalitah.com/api/api.php?key=W2jFgx1leQ&opt=7&art=";

    public static final String COMMUNITY_MEMBER_PHONES = "http://ortalitah.com/api/api.php?key=W2jFgx1leQ&opt=3&ID=";

    public static final String COMMUNITY_ABOUT = "http://ortalitah.com/api/api.php?key=W2jFgx1leQ&opt=5";

    public static final String ALL_ARTICLE_IMAGES = "http://ortalitah.com/api/api.php?key=W2jFgx1leQ&opt=9&evn=";

    public static final String NUMBER_OF_EVENTS = "http://ortalitah.com/api/api.php?key=W2jFgx1leQ&opt=16";

    public static final String COMMUNITY_OPENING_STATEMENT = "http://www.ortalitah.com/api/api.php?key=W2jFgx1leQ&opt=4";


    // -----------------------------------------------------Post Data URLs---------------------------------------------------------------------------
    public static final String POST_COMMENT_FOR_ARTICLE = "http://ortalitah.com/api/api.php?key=W2jFgx1leQ&opt=11";

    public static final String ADD_NEW_MEMBER =  "http://ortalitah.com/api/api.php?key=W2jFgx1leQ&opt=12";

    public static final String ADD_NEW_EVENT =  "http://www.ortalitah.com/api/api.php?key=W2jFgx1leQ&opt=13";

    public static final String POST_ARTICLE_IS_WATCHED = "http://www.ortalitah.com/api/api.php?key=W2jFgx1leQ&opt=17";

    public static final String POST_USAGE_STATISTICS = "http://www.ortalitah.com/api/api.php?key=W2jFgx1leQ&opt=18";

    public static final String ADD_NEW_ARTICLE =  "http://www.ortalitah.com/api/api.php?key=W2jFgx1leQ&opt=15";

    public static final String REMOVE_EVENT =  "http://www.ortalitah.com/api/api.php?key=W2jFgx1leQ&opt=19";

    public static final String REMOVE_ARTICLE =  "http://www.ortalitah.com/api/api.php?key=W2jFgx1leQ&opt=20";

    public static final String EDIT_EVENT =  "http://www.ortalitah.com/api/api.php?key=W2jFgx1leQ&opt=21";

   // ---------------------------------------------------------Image UELs----------------------------------------------------------------------------

    public static final String MEMBER_IMAGE = "http://ortalitah.com/public/img/uploads/members/";

    public static final String EVENTS_IMAGE = "http://ortalitah.com/public/img/uploads/events/";

    public static final String DEFAULT_IMAGE = "http://ortalitah.com/public/img/profile.jpg";

    public static final String NEW_MEMBER_DEFAULT_IMAGE = "null_profile.jpg";

    private static Utils _instance;

    /** CHECK WHETHER INTERNET CONNECTION IS AVAILABLE OR NOT */
    public static boolean checkConnection(Context context) {
        return  ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
    }

    /**
     * singleton
     * @return _instance
     */
    public synchronized static Utils getInstance()
    {
        if (_instance == null)
        {
            _instance = new Utils();
        }
        return _instance;
    }


    /**
     * Format a Date Object
     * @param date
     * @return String Date Format
     */
    public static String getBirthDate(Date date)  {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        return format.format(date);
    }

    /**
     * Convert Boolean To Int Value
     * @param b
     * @return Int
     */
    public static int getBoolean(boolean b)  {
        return (b) ? 1 : 0;
    }

    /**
     * Parse jsonObject Response From Server Into Member object
     * @param response
     * @return Member Object
     */
    public Member responseToMember(String response)  {

        if(response.length() < 1){
            return null;
        }
        Member member = new Member();
        try {
            JSONObject jsonObject = new JSONObject(response);

            member.setMemberID(Integer.parseInt(jsonObject.getString("memberID")));
            member.setUsername(jsonObject.getString("username"));
            member.setPassword(jsonObject.getString("password"));
            member.setFullName(jsonObject.getString("fullName"));

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            member.setBirthDate(format.parse(jsonObject.getString("birthdate")));

            member.setEmail(jsonObject.getString("email"));
            member.setGender(Integer.parseInt(jsonObject.getString("gender")));
            member.setStatus(Integer.parseInt(jsonObject.getString("status")));
            member.setChildren(Integer.parseInt(jsonObject.getString("children")));
            member.setState(jsonObject.getString("state"));
            member.setStreet(jsonObject.getString("street"));
            member.setHouseNum(Integer.parseInt(jsonObject.getString("houseNum")));
            member.setZipCode(Integer.parseInt(jsonObject.getString("zipCode")));
            member.setEducation(Integer.parseInt(jsonObject.getString("education")));

            String proImage = jsonObject.getString("profilePic");
            if(proImage == "null"){
                member.setProfilePic(DEFAULT_IMAGE);
            }else {
                member.setProfilePic(MEMBER_IMAGE+proImage);
            }

            member.setApproved(getBool(jsonObject.getString("isApproved")));

            SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            member.setRegistrationDate(format2.parse(jsonObject.getString("registrationDate")));
            member.setSubExpire(format2.parse(jsonObject.getString("subExpire")));
            member.setSendMails(getBool(jsonObject.getString("sendMails")));
            member.setAdmin(getBool(jsonObject.getString("isAdmin")));

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        return member;
    }

    /**
     * Parse JSONArray Response From Server Into Phone ArrayList
     * @param response
     * @return ArrayList<Phone>
     */
    public ArrayList<Phone> responseToPhoneList(String response)  {

        if(response.length() < 1){
            return null;
        }
        ArrayList<Phone> phones = new ArrayList<>();
        Phone phone;
        try {
            JSONArray jsonArray = new JSONArray(response);
            for (int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                phone = new Phone();

                phone.setMemberID(Integer.parseInt(jsonObject.getString("memberID")));
                phone.setPhoneNumber(jsonObject.getString("phoneNumber"));
                phone.setType(Integer.parseInt(jsonObject.getString("type")));
                phone.setPublish(getBool(jsonObject.getString("publish")));
                phones.add(phone);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return phones;
    }


    /**
     * Convert Image Server Response To Url Server Image
     * @param response
     * @return String Url
     */
    public ArrayList<String> responseToMediaUrlList(String response)  {

        if(response.length() < 1){
            return null;
        }
        ArrayList<String> medias = new ArrayList<>();
        String media;
        try {
            JSONArray jsonArray = new JSONArray(response);
            for (int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                //media = new String();
                media = EVENTS_IMAGE+jsonObject.getString("fileName");

                medias.add(media);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return medias;
    }

    /**
     * Parse Response From Server Into About
     * @param response
     * @return String About
     */
    public String responseToAbout(String response)  {
        if(response.length() < 1){
            return null;
        }
        String about;
        try {
            JSONObject jsonObject = new JSONObject(response);
            about = (jsonObject.getString("bigBlob"));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return about;
    }

    /**
     *Parse Response From Server Into Opening Statement
     * @param response
     * @return String Opening Statement
     */
    public String responseToOpening(String response)  {
        if(response.length() < 1){
            return null;
        }
        String about;
        try {
            JSONObject jsonObject = new JSONObject(response);
            about = (jsonObject.getString("smallText"));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return about;
    }

    /**
     * Parse Response From Server Into Number Of Events
     * @param response
     * @return String Events Number
     */
    public String responseToEventsNumber(String response)  {
        if(response.length() < 1){
            return "";
        }
        try {
            JSONObject jsonObject = new JSONObject(response);
            return jsonObject.getString("count");
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * Parse JSONArray Response From Server Into Member ArrayList
     * @param response
     * @return ArrayList<Member>
     */
    public ArrayList<Member> responseToMembersList(String response)  {

        if(response.length() < 1){
            return null;
        }
        ArrayList<Member> members = new ArrayList<>();
        Member member;

        try {
            JSONArray jsonArray = new JSONArray(response);

            for (int i = 0; i < jsonArray.length(); i++){

                JSONObject jsonObject = jsonArray.getJSONObject(i);

                member = new Member();

                member.setMemberID(Integer.parseInt(jsonObject.getString("memberID")));
                member.setUsername(jsonObject.getString("username"));
                member.setPassword(jsonObject.getString("password"));
                member.setFullName(jsonObject.getString("fullName"));

                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                member.setBirthDate(format.parse(jsonObject.getString("birthdate")));

                member.setEmail(jsonObject.getString("email"));
                member.setGender(Integer.parseInt(jsonObject.getString("gender")));
                member.setStatus(Integer.parseInt(jsonObject.getString("status")));
                member.setChildren(Integer.parseInt(jsonObject.getString("children")));
                member.setState(jsonObject.getString("state"));
                member.setStreet(jsonObject.getString("street"));
                member.setHouseNum(Integer.parseInt(jsonObject.getString("houseNum")));
                member.setZipCode(Integer.parseInt(jsonObject.getString("zipCode")));
                member.setEducation(Integer.parseInt(jsonObject.getString("education")));

                String proImage = jsonObject.getString("profilePic");
                if(proImage == "null"){
                    member.setProfilePic(DEFAULT_IMAGE);
                }else {
                    member.setProfilePic(MEMBER_IMAGE+proImage);
                }
                member.setApproved(getBool(jsonObject.getString("isApproved")));

                SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                member.setRegistrationDate(format2.parse(jsonObject.getString("registrationDate")));
                member.setSubExpire(format2.parse(jsonObject.getString("subExpire")));
                member.setSendMails(getBool(jsonObject.getString("sendMails")));
                member.setAdmin(getBool(jsonObject.getString("isAdmin")));

                members.add(member);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        return members;
    }

    /**
     * Parse JSONArray Response From Server Into Article ArrayList
     * @param response
     * @return ArrayList<Article>
     */
    public ArrayList<Article> responseToArticleList(String response)  {

        if(response.length() < 1){
            return null;
        }
        ArrayList<Article> articles = new ArrayList<>();
        Article article;

        try {
            JSONArray jsonArray = new JSONArray(response);

            for (int i = 0; i < jsonArray.length(); i++){

                JSONObject jsonObject = jsonArray.getJSONObject(i);

                article = new Article();

                article.setArticleID(Integer.parseInt(jsonObject.getString("articleID")));
                article.setHeadline(jsonObject.getString("headline"));

                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                article.setPublishDate(format.parse(jsonObject.getString("dateTime")));

                article.setContent(jsonObject.getString("content"));
                article.setAuthorID(Integer.parseInt(jsonObject.getString("author")));
                article.setAuthorName(jsonObject.getString("fullName"));
                article.setViews(Integer.parseInt(jsonObject.getString("views")));
                article.setComments(Integer.parseInt(jsonObject.getString("comments")));

                articles.add(article);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        return articles;
    }

    /**
     * Parse JSONArray Response From Server Into Event ArrayList
     * @param response
     * @return ArrayList<Event>
     */
    public ArrayList<Event> responseToEventList(String response)  {

        if(response.length() < 1){
            return null;
        }
        ArrayList<Event> events = new ArrayList<>();
        Event event;

        try {
            JSONArray jsonArray = new JSONArray(response);

            for (int i = 0; i < jsonArray.length(); i++){

                JSONObject jsonObject = jsonArray.getJSONObject(i);

                event = new Event();

                event.setEventID(Integer.parseInt(jsonObject.getString("eventID")));
                event.setTitle(jsonObject.getString("title"));

                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                event.setPublishDate(format.parse(jsonObject.getString("dateTime")));

                event.setCapacity(Integer.parseInt(jsonObject.getString("capacity")));
                event.setLocation(jsonObject.getString("location"));
                event.setDescription(jsonObject.getString("description"));
                event.setPublisherID(Integer.parseInt(jsonObject.getString("publisher")));
                event.setImageCount(Integer.parseInt(jsonObject.getString("count")));

                events.add(event);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        return events;
    }

    /**
     * Parse JSONArray Response From Server Into Comment ArrayList
     * @param response
     * @return ArrayList<Comment>
     */
    public ArrayList<Comment> responseToCommentsList(String response)  {

        if(response.length() < 1){
            return null;
        }
        ArrayList<Comment> comments = new ArrayList<>();
        Comment comment;

        try {
            JSONArray jsonArray = new JSONArray(response);

            for (int i = 0; i < jsonArray.length(); i++){

                JSONObject jsonObject = jsonArray.getJSONObject(i);

                comment = new Comment();

                comment.setCommentID(Integer.parseInt(jsonObject.getString("commentID")));
                comment.setHeadline(jsonObject.getString("headline"));

                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                comment.setPublishDate(format.parse(jsonObject.getString("dateTime")));

                comment.setContent(jsonObject.getString("content"));
                comment.setAuthorID(Integer.parseInt(jsonObject.getString("writer")));
                comment.setArticleID(Integer.parseInt(jsonObject.getString("article")));
                comment.setAuthorName(jsonObject.getString("fullName"));

                String proImage = jsonObject.getString("profilePic");
                if(proImage == "null"){
                    comment.setAuthorProfilePic(DEFAULT_IMAGE);
                }else {
                    comment.setAuthorProfilePic(MEMBER_IMAGE+proImage);
                }

                comments.add(comment);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        return comments;
    }

    /**
     * Convert String To Boolean Value
     * @param b
     * @return boolean
     */
    public boolean getBool(String b){
        if(b == "null"){
            return false;
        }
        return (Integer.parseInt(b) == 1);
    }

    /**
     * Convert Date Object To String Date Format
     * @param dateToStr
     * @return String Date Format
     */
    public static String eventToDateString(Date dateToStr)  {
        String date;
        String time;
        SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy");
        date = formatDate.format(dateToStr);
        SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm");
        time = formatTime.format(dateToStr);

        return  date+"  בשעה: "+time;
    }
    /**
     * Write a Logd-in Member Object to SharedPreferences
     * @param context
     * @param member
     */
    public static void writeMemberToPrefs(Member member, Context context)  {
        try {
            SharedPreferences mPrefs =  PreferenceManager.getDefaultSharedPreferences(context);
            Editor prefsEditor = mPrefs.edit();
            Gson gson = new Gson();
            String json = gson.toJson(member);
            prefsEditor.putString("loginMember", json);
            prefsEditor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Load Logd-in Member Object from SharedPreferences
     * @param context
     * @return Member Object
     */
    public Member loadMemberFromPrefs(Context context)  {
        SharedPreferences mPrefs =  PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = mPrefs.getString("loginMember", "");
        Member obj = gson.fromJson(json, Member.class);
        return obj;
    }

    /**
     * Hide Keyboard
     * @param activity
     */
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    /**
     * Convert Usage Statistics Data To JSONObject
     * @param userID
     * @param usage
     * @param uiName
     * @return JSONObject
     */
    public static JSONObject UsageStatisticsToJsonObject(int userID, double usage, String uiName){

        JSONObject jsonObject= new JSONObject();
        try {
            jsonObject.put("userID", userID);
            jsonObject.put("uiName", uiName);
            jsonObject.put("duration", usage);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    /**
     * Convert Int Event ID To JSONObject
     * @param eventID
     * @return JSONObject
     */
    public static JSONObject eventIdToJsonObject(int eventID){
        JSONObject jsonObject= new JSONObject();
        try {
            jsonObject.put("eventID", eventID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    /**
     * Convert Int Article ID  To JSONObject
     * @param articleID
     * @return
     */
    public static JSONObject articleIdToJsonObject(int articleID){
        JSONObject jsonObject= new JSONObject();
        try {
            jsonObject.put("articleID", articleID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


    /**
     * Convert Long Milliseconds To Double Seconds
     * @param milliseconds
     * @return Double Seconds
     */
    public static double milliToSeconds(long milliseconds){
        return milliseconds / 1000.0;
    }
}
