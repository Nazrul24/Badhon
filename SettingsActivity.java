package kazinazrul.csedu24.badhon_knih;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private EditText userName,bloodGroup,userStatus,userDistrict,userGender;
    private Button UpdateAccountSettingsButton;
    private CircleImageView userProfileImage;

    private DatabaseReference settingsUserRef;
    private FirebaseAuth mAuth;
    private String currentUserID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        settingsUserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID);

        mToolbar = (Toolbar) findViewById(R.id.settings_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Account Settings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        userName = findViewById(R.id.settings_username);
        bloodGroup = findViewById(R.id.settings_bloodgroup);
        userStatus = findViewById(R.id.settings_status);
        userDistrict = findViewById(R.id.settings_district);
        userGender = findViewById(R.id.settings_gender);
        UpdateAccountSettingsButton = findViewById(R.id.update_account_settings_button);
        userProfileImage =findViewById(R.id.settings_profile_image);

        settingsUserRef.addValueEventListener(new ValueEventListener() {
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


                    userName.setText(myUserName);
                    bloodGroup.setText(myBloodGroup);
                    userStatus.setText(myProfileStatus);
                    userDistrict.setText(myDistrict);
                    userGender.setText(myGender);
                    //userName.setText(myUserName);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        UpdateAccountSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValidateAccountInfo();
            }
        });


    }

    private void ValidateAccountInfo() {
        String username = userName.getText().toString();
        String bloodgroup = bloodGroup.getText().toString();
        String status = userStatus.getText().toString();
        String district = userDistrict.getText().toString();
        String gender = userGender.getText().toString();

        if(TextUtils.isEmpty(username)){
            Toast.makeText(this,"Please write your name",Toast.LENGTH_SHORT).show();
        }

        else if(TextUtils.isEmpty(bloodgroup)){
            Toast.makeText(this,"Please write your blood group ",Toast.LENGTH_SHORT).show();
        }

        else if(TextUtils.isEmpty(status)){
            Toast.makeText(this,"Please write your status",Toast.LENGTH_SHORT).show();
        }

        else if(TextUtils.isEmpty(district)){
            Toast.makeText(this,"Please write your district",Toast.LENGTH_SHORT).show();
        }

        else if(TextUtils.isEmpty(gender)){
            Toast.makeText(this,"Please write your gender",Toast.LENGTH_SHORT).show();
        }

        else{
            UpdateAccountInfo(username,bloodgroup,status,district,gender);
        }
    }

    private void UpdateAccountInfo(String username, String bloodgroup, String status, String district, String gender) {
        HashMap userMap = new HashMap();

        userMap.put("fullname",username);
        userMap.put("bloodGroup",bloodgroup);
        userMap.put("status",status);
        userMap.put("district",district);
        userMap.put("gender",gender);

        settingsUserRef.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if(task.isSuccessful()){
                    Toast.makeText(SettingsActivity.this,"Account Information Updated Successfully",Toast.LENGTH_SHORT).show();
                    SendUserToMainActivity();

                }
                else{
                    Toast.makeText(SettingsActivity.this,"Error Occured Please check your Internet Connection...",Toast.LENGTH_SHORT).show();

                }
            }
        });
    }


    private void SendUserToMainActivity() {
        Intent mainIntent = new Intent(SettingsActivity.this,MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }
}
