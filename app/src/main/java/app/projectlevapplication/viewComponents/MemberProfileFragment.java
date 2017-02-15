package app.projectlevapplication.viewComponents;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import app.projectlevapplication.R;
import app.projectlevapplication.core.Member;
import app.projectlevapplication.utils.Utils;

/**
 * A simple {@link Fragment} subclass.
 */
public class MemberProfileFragment extends Fragment {

    Context context;
    Activity activity;
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

    public MemberProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.activity_member_profile, container, false);
        context = view.getContext();
        activity= getActivity();

        profileImage = (ImageView) view.findViewById(R.id.profileImage);
        fullName = (TextView) view.findViewById(R.id.txtMemberName);
        birthDate = (TextView) view.findViewById(R.id.txtBirthDate);
        mail = (TextView) view.findViewById(R.id.txtEmail);
        gender = (TextView) view.findViewById(R.id.txtGender);
        txtMebmberPhoneNumber = (TextView) view.findViewById(R.id.txtMebmberPhoneNumber);
        txtMemberAddress = (TextView) view.findViewById(R.id.txtMemberAddress);
        txtMemberJoinDate = (TextView) view.findViewById(R.id.txtMemberJoinDate);
        txtMemberStatus = (TextView) view.findViewById(R.id.txtMemberStatus);
        txtMemberEndSubscriptionDate = (TextView) view.findViewById(R.id.txtMemberEndSubscriptionDate);
        adminViewStatus = (LinearLayout) view.findViewById(R.id.adminViewStatus);
        adminViewSubscription = (LinearLayout) view.findViewById(R.id.adminViewSubscription);
        adminViewBtn = (LinearLayout) view.findViewById(R.id.adminViewBtn);

        Bundle args = getArguments();
        memberToDisplay = (Member) args.getSerializable("mMember");

        Picasso.with(activity).load(memberToDisplay.getProfilePic()).into(profileImage);
        fullName.setText(memberToDisplay.getFullName());
        birthDate.setText(Utils.getBirthDate(memberToDisplay.getBirthDate()));
        mail.setText(memberToDisplay.getEmail());
        gender.setText(memberToDisplay.getMemberGender());
        if(Utils.getInstance().loadMemberFromPrefs(context).isAdmin()){
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
        System.out.print(memberToDisplay.toJsonObject().toString());

        Button btnEditMember = (Button) view.findViewById(R.id.btnEditMember);
        btnEditMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return view;
    }

    public void loadMemberPhoneForAdmin(){
        loading = ProgressDialog.show(activity,"בבקשה המתן...","מחזיר מידע...",false,false);

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
                        Toast.makeText(context,"error", Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
    }

    public void loadMemberPhone(){
        loading = ProgressDialog.show(activity,"בבקשה המתן...","מחזיר מידע...",false,false);

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
                        Toast.makeText(context,"error", Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
    }
}
