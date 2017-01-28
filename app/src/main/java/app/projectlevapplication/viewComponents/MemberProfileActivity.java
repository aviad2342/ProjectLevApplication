
package app.projectlevapplication.viewComponents;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.squareup.picasso.Picasso;

import app.projectlevapplication.R;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_profile);

        profileImage = (ImageView) findViewById(R.id.profileImage);
        fullName = (TextView) findViewById(R.id.txtMemberName);
        birthDate = (TextView) findViewById(R.id.txtBirthDate);
        mail = (TextView) findViewById(R.id.txtEmail);
        gender = (TextView) findViewById(R.id.txtGender);

       memberToDisplay = (Member) getIntent().getSerializableExtra("mMember");

        Picasso.with(this).load(memberToDisplay.getProfilePic()).into(profileImage);
        fullName.setText(memberToDisplay.getFullName());
        birthDate.setText(Utils.getBirthDate(memberToDisplay.getBirthDate()));
        mail.setText(memberToDisplay.getEmail());
        gender.setText(memberToDisplay.getMemberGender());
    }
}
