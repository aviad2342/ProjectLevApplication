package app.projectlevapplication.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import app.projectlevapplication.core.Member;

/**
 * Created by user-pc on 24/01/2017.
 */

public class Utils {

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

    public Member responseToMember(String response)  {

        if(response.length() < 1){
            return null;
        }
        Member member = new Member();
        try {
           // JSONArray jsonArray = new JSONArray(response);
           // JSONObject jsonObject = jsonArray.getJSONObject(0);
            String noob = "{\"memberID\":\"1\",\"username\":\"Admin\",\"password\":\"Admin\",\"fullName\":\"איתמר פרנקל\",\"birthdate\":\"1988-09-29\",\"email\":\"ifrenkelr@gmail.com\",\"gender\":\"0\",\"status\":\"0\",\"children\":\"0\",\"state\":\"קריית מוצקין\",\"street\":\"קדיש לוז 50 \",\"houseNum\":\"3\",\"zipCode\":\"-1\",\"education\":\"0\",\"profilePic\":\"lzCDiG0XmblnE3sadmin_profile.jpg\",\"isApproved\":\"1\",\"registrationDate\":\"2016-07-06 14:21:19\",\"lastVisited\":\"2017-01-21 17:57:00\",\"newVisit\":\"2017-01-21 23:40:00\",\"subExpire\":\"2017-07-06 00:00:00\",\"sendMails\":\"1\"}";
            JSONObject jsonObject = new JSONObject(noob);

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
            member.setProfilePic(jsonObject.getString("profilePic"));
            member.setApproved(Boolean.parseBoolean(jsonObject.getString("isApproved")));

            SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            member.setRegistrationDate(format2.parse(jsonObject.getString("registrationDate")));
            member.setSubExpire(format2.parse(jsonObject.getString("subExpire")));

            member.setSendMails(Boolean.parseBoolean(jsonObject.getString("sendMails")));


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
     * write \ update a Member record to SharedPreferences
     * @param mPrefs
     * @param member
     */
    public void writeMemberToPrefs(SharedPreferences mPrefs, Member member)  {
        try {
            SharedPreferences.Editor prefsEditor = mPrefs.edit();
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
