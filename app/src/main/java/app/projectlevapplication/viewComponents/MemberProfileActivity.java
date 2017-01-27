
package app.projectlevapplication.viewComponents;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import app.projectlevapplication.R;
import app.projectlevapplication.core.Member;

public class MemberProfileActivity extends AppCompatActivity {

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
        fullName = (TextView) findViewById(R.id.txtName);
        birthDate = (TextView) findViewById(R.id.txtBirthDate);
        mail = (TextView) findViewById(R.id.txtEmail);
        gender = (TextView) findViewById(R.id.txtGender);
    }
}
