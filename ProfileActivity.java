package kazinazrul.csedu24.badhon_knih;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private TextView userName,bloodGroup,userStatus,userDistrict,userGender;
    private CircleImageView userProfileImage;
    private DatabaseReference profileUserRef;
    private FirebaseAuth mAuth;

    String currentUserID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        profileUserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID);


        userName = findViewById(R.id.my_profile_name);
        bloodGroup = findViewById(R.id.my_blood_group);
        userStatus = findViewById(R.id.my_profile_status);
        userDistrict = findViewById(R.id.my_district);
        userGender = findViewById(R.id.my_gender);
        userProfileImage =findViewById(R.id.my_profile_pic);


        profileUserRef.addValueEventListener(new ValueEventListener() {
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
}
