package kazinazrul.csedu24.badhon_knih;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.telephony.mbms.MbmsErrors;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class PersonProfileActivity extends AppCompatActivity {

    private TextView userName,bloodGroup,userStatus,userDistrict,userGender;
    private CircleImageView userProfileImage;
    private Button SendRequestButton,DeclineRequestButton;
    private DatabaseReference UsersRef;
    private FirebaseAuth mAuth;
    private String senderUserID,recieverUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_profile);

        mAuth = FirebaseAuth.getInstance();
        recieverUserID = getIntent().getExtras().get("visit_user_id").toString();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");

        InitializeFields();

        UsersRef.child(recieverUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    String myProfileImage = dataSnapshot.child("profileimage").getValue().toString();
                    String myUserName = dataSnapshot.child("fullname").getValue().toString();
                    String myBloodGroup = dataSnapshot.child("bloodGroup").getValue().toString();
                    String myProfileStatus = dataSnapshot.child("status").getValue().toString();
                    String myDistrict = dataSnapshot.child("district").getValue().toString();
                    String myGender = dataSnapshot.child("gender").getValue().toString();


                    Picasso.get().load(myProfileImage).placeholder(R.drawable.profile).into(userProfileImage);


                    userName.setText("@"+myUserName);
                    bloodGroup.setText(myBloodGroup);
                    userStatus.setText(myProfileStatus);
                    userDistrict.setText("District : "+myDistrict);
                    userGender.setText("Gender : " + myGender);




                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void InitializeFields() {
        userName = findViewById(R.id.person_profile_name);
        bloodGroup = findViewById(R.id.person_blood_group);
        userStatus = findViewById(R.id.person_profile_status);
        userDistrict = findViewById(R.id.person_district);
        userGender = findViewById(R.id.person_gender);
        userProfileImage =findViewById(R.id.person_profile_pic);
        SendRequestButton = findViewById(R.id.send_request_button);
        DeclineRequestButton = findViewById(R.id.decline_request_button);

    }
}
