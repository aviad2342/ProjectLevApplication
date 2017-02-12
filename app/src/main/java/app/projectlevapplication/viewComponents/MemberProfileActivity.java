
package app.projectlevapplication.viewComponents;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.Date;

import app.projectlevapplication.R;
import app.projectlevapplication.core.Comment;
import app.projectlevapplication.core.Member;
import app.projectlevapplication.utils.MyMenuBar;
import app.projectlevapplication.utils.Utils;

public class MemberProfileActivity extends MyMenuBar {

    Member memberToDisplay;
    ImageView profileImage;
    TextView fullName;
    TextView birthDate;
    TextView mail;
    TextView gender;
    TextView txtMebmberPhoneNumber;
    TextView txtMemberAddress;
    TextView txtMemberJoinDate;
    TextView txtMemberStatus;
    TextView txtMemberEndSubscriptionDate;
    LinearLayout adminViewStatus;
    LinearLayout adminViewSubscription;
    LinearLayout adminViewBtn;
    public ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_profile);

        profileImage = (ImageView) findViewById(R.id.profileImage);
        fullName = (TextView) findViewById(R.id.txtMemberName);
        birthDate = (TextView) findViewById(R.id.txtBirthDate);
        mail = (TextView) findViewById(R.id.txtEmail);
        gender = (TextView) findViewById(R.id.txtGender);
        txtMebmberPhoneNumber = (TextView) findViewById(R.id.txtMebmberPhoneNumber);
        txtMemberAddress = (TextView) findViewById(R.id.txtMemberAddress);
        txtMemberJoinDate = (TextView) findViewById(R.id.txtMemberJoinDate);
        txtMemberStatus = (TextView) findViewById(R.id.txtMemberStatus);
        txtMemberEndSubscriptionDate = (TextView) findViewById(R.id.txtMemberEndSubscriptionDate);
        adminViewStatus = (LinearLayout) findViewById(R.id.adminViewStatus);
        adminViewSubscription = (LinearLayout) findViewById(R.id.adminViewSubscription);
        adminViewBtn = (LinearLayout) findViewById(R.id.adminViewBtn);

       memberToDisplay = (Member) getIntent().getSerializableExtra("mMember");

        Picasso.with(this).load(memberToDisplay.getProfilePic()).into(profileImage);
        fullName.setText(memberToDisplay.getFullName());
        birthDate.setText(Utils.getBirthDate(memberToDisplay.getBirthDate()));
        mail.setText(memberToDisplay.getEmail());
        gender.setText(memberToDisplay.getMemberGender());
        if(Utils.getInstance().loadMemberFromPrefs(this).isAdmin()){
            adminViewStatus.setVisibility(View.VISIBLE);
            adminViewSubscription.setVisibility(View.VISIBLE);
            adminViewBtn.setVisibility(View.VISIBLE);
            loadMemberPhoneForAdmin();
        }else {
            loadMemberPhone();
        }
        txtMemberAddress.setText(memberToDisplay.getMemberAddress());
        txtMemberJoinDate.setText(Utils.eventToDateString(memberToDisplay.getRegistrationDate()));
        txtMemberStatus.setText(memberToDisplay.getMemberStatus());
        txtMemberEndSubscriptionDate.setText(Utils.eventToDateString(memberToDisplay.getSubExpire()));



        Button btnEditMember = (Button) findViewById(R.id.btnEditMember);
        btnEditMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                        }
                    });
    }

    public void loadMemberPhoneForAdmin(){
        loading = ProgressDialog.show(MemberProfileActivity.this,"בבקשה המתן...","מחזיר מידע...",false,false);

        String url = Utils.COMMUNITY_MEMBER_PHONES+memberToDisplay.getMemberID();

        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loading.dismiss();
                String phoneNum = "";
                memberToDisplay.setPhones(Utils.getInstance().responseToPhoneList(response));
                if(memberToDisplay.getPhones() != null){
                    for (int i = 0; i < memberToDisplay.getPhones().size(); i++){
                        if(memberToDisplay.getPhones().get(i).getType() == 1){
                            phoneNum = memberToDisplay.getPhones().get(i).getPhoneNumber()+"-"+memberToDisplay.getPhones().get(i).getNumberType();
                        }
                    }
                    if(phoneNum == ""){
                        for (int i = 0; i < memberToDisplay.getPhones().size(); i++){
                            if(memberToDisplay.getPhones().get(i).getType() == 0){
                                phoneNum = memberToDisplay.getPhones().get(i).getPhoneNumber()+"-"+memberToDisplay.getPhones().get(i).getNumberType();
                            }
                        }
                    }
                    if(phoneNum == ""){
                        for (int i = 0; i < memberToDisplay.getPhones().size(); i++){
                            if(memberToDisplay.getPhones().get(i).getType() == 2){
                                phoneNum = memberToDisplay.getPhones().get(i).getPhoneNumber()+"-"+memberToDisplay.getPhones().get(i).getNumberType();
                            }
                        }
                    }
                    if(phoneNum == ""){
                        phoneNum = "לא צויין";
                    }
                }
                txtMebmberPhoneNumber.setText(phoneNum);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        Toast.makeText(getApplicationContext(),"error", Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(MemberProfileActivity.this);
        requestQueue.add(stringRequest);
    }

    public void loadMemberPhone(){
        loading = ProgressDialog.show(MemberProfileActivity.this,"בבקשה המתן...","מחזיר מידע...",false,false);

        String url = Utils.COMMUNITY_MEMBER_PHONES+memberToDisplay.getMemberID();

        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loading.dismiss();
                String phoneNum = "";
                memberToDisplay.setPhones(Utils.getInstance().responseToPhoneList(response));
                if(memberToDisplay.getPhones() != null){
                    for (int i = 0; i < memberToDisplay.getPhones().size(); i++){
                        if(memberToDisplay.getPhones().get(i).getType() == 1 && memberToDisplay.getPhones().get(i).isPublish()){
                            phoneNum = memberToDisplay.getPhones().get(i).getPhoneNumber()+"-"+memberToDisplay.getPhones().get(i).getNumberType();
                        }
                    }
                    if(phoneNum == ""){
                        for (int i = 0; i < memberToDisplay.getPhones().size(); i++){
                            if(memberToDisplay.getPhones().get(i).getType() == 0 && memberToDisplay.getPhones().get(i).isPublish()){
                                phoneNum = memberToDisplay.getPhones().get(i).getPhoneNumber()+"-"+memberToDisplay.getPhones().get(i).getNumberType();
                            }
                        }
                    }
                    if(phoneNum == ""){
                        for (int i = 0; i < memberToDisplay.getPhones().size(); i++){
                            if(memberToDisplay.getPhones().get(i).getType() == 2 && memberToDisplay.getPhones().get(i).isPublish()){
                                phoneNum = memberToDisplay.getPhones().get(i).getPhoneNumber()+"-"+memberToDisplay.getPhones().get(i).getNumberType();
                            }
                        }
                    }
                    if(phoneNum == ""){
                        phoneNum = "לא צויין";
                    }
                }
                txtMebmberPhoneNumber.setText(phoneNum);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        Toast.makeText(getApplicationContext(),"error", Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(MemberProfileActivity.this);
        requestQueue.add(stringRequest);
    }
}
