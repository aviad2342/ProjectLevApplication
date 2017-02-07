package app.projectlevapplication.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import app.projectlevapplication.core.Article;
import app.projectlevapplication.core.Comment;
import app.projectlevapplication.core.Event;
import app.projectlevapplication.core.Member;

/**
 * Created by user-pc on 24/01/2017.
 */

public class Utils {

    // --------------------------------------------------------Data URLs----------------------------------------------------------------------------


    public static final String ALL_COMMUNITY_MEMBERS = "http://arianlev.esy.es/ArianLev_Community/api/api.php?key=W2jFgx1leQ&opt=1";

    public static final String ALL_COMMUNITY_ARTICLES = "http://arianlev.esy.es/ArianLev_Community/api/api.php?key=W2jFgx1leQ&opt=6";

    public static final String ALL_COMMUNITY_EVENTS = "http://arianlev.esy.es/ArianLev_Community/api/api.php?key=W2jFgx1leQ&opt=8";

   // ---------------------------------------------------------Image UELs----------------------------------------------------------------------------

    public static final String MEMBER_IMAGE = "http://arianlev.esy.es/ArianLev_Community/public/img/uploads/members/";

    public static final String EVENTS_IMAGE = "http://arianlev.esy.es/ArianLev_Community/public/img/uploads/events/";

    public static final String TESTIMONY_IMAGE = "http://arianlev.esy.es/ArianLev_Community/public/img/uploads/testimony/";

    public static final String THERAPISTS_IMAGE = "http://arianlev.esy.es/ArianLev_Community/public/img/uploads/therapists/";

    public static final String GALLERY_IMAGE = "http://arianlev.com/gallery/";

    public static final String DEFAULT_IMAGE = "http://arianlev.esy.es/ArianLev_Community/public/img/profile.jpg";

    private static Utils _instance;

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

    public static void loadMemberImage(Activity activity, String imageToLoad, ImageView view)  {
        Picasso.with(activity).load(MEMBER_IMAGE+imageToLoad).into(view);
    }

    public static String getBirthDate(Date date)  {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        return format.format(date);
    }

    public static String getGender(Date date)  {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        return date.toString();
    }

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

    public boolean isAdmin(String response)  {

        if(response.length() < 1){
            return false;
        }
        boolean isAdmin;
        try {
            JSONArray jsonArray = new JSONArray(response);
            //JSONObject jsonObject = new JSONObject(response);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            isAdmin = (Integer.parseInt(jsonObject.getString("TRUE")) != 0);

        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
        return isAdmin;
    }

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

    public ArrayList<String> responseToArticleHeadlineList(String response)  {
        if(response.length() < 1){
            return null;
        }
        ArrayList<String> headlines = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(response);

            for (int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                headlines.add(jsonObject.getString("headline"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return headlines;
    }

    public boolean getBool(String b){
        return (b == "1");
    }

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
     * write \ update a Member record to SharedPreferences
     * @param mPrefs
     * @param member
     */
    public void writeMemberToPrefs(SharedPreferences mPrefs, Member member)  {
        try {
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
     * load Member from SharedPreferences
     * @param mPrefs
     * @return UserProfile
     */
    public Member loadMemberFromPrefs(SharedPreferences mPrefs)  {
        Gson gson = new Gson();
        String json = mPrefs.getString("loginMember", "");
        Member obj = gson.fromJson(json, Member.class);
        return obj;
    }
}
