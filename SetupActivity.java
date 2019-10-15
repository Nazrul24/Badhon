package kazinazrul.csedu24.badhon_knih;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SetupActivity extends AppCompatActivity {


    private EditText BloodGroup,FullName,District,Bloodcon;
    private Button SaveInformationButton;
    private CircleImageView ProfileImage;
    private FirebaseAuth mAuth;

    private DatabaseReference userRef;
    String currentUserID;

    private StorageReference UserProfileImageRef;
    private ProgressDialog loadingBar;

    final static int Gallery_Pick = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);


        loadingBar = new ProgressDialog(this);

        mAuth=FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();

        userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID);

        UserProfileImageRef = FirebaseStorage.getInstance().getReference().child("profile Images");

        BloodGroup = (EditText) findViewById(R.id.setup_username);
        Bloodcon = (EditText) findViewById(R.id.setup_blood_condition);
        FullName = (EditText) findViewById(R.id.setup_full_name);
        District = (EditText) findViewById(R.id.setup_district_name);
        SaveInformationButton = (Button) findViewById(R.id.update_post_button);
        ProfileImage = (CircleImageView) findViewById(R.id.setup_profile_image);

        SaveInformationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveAccountSetupInformation();
            }
        });


        ProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,Gallery_Pick);


            }
        });

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                if(dataSnapshot.exists()){
                    if(dataSnapshot.hasChild("profileimage")){

                        String image = dataSnapshot.child("profileimage").getValue().toString();
                        Picasso.get().load(image).placeholder(R.drawable.profile).into(ProfileImage);

                    }

                    else {
                        Toast.makeText(SetupActivity.this,"Please select profile image...",Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Gallery_Pick && resultCode == RESULT_OK && data!=null){
            Uri imageUri = data.getData();
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this);


        }

        if(requestCode ==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if(resultCode == RESULT_OK){
                loadingBar.setTitle("Profile Image");
                loadingBar.setMessage("Please wait...");
                loadingBar.show();
                loadingBar.setCanceledOnTouchOutside(true);

                Uri resultUri = result.getUri();

                StorageReference filePath = UserProfileImageRef.child(currentUserID + ".jpg");
                filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(SetupActivity.this,"Profile image uploaded successfully...",Toast.LENGTH_SHORT).show();

                            final String downloadUrl = task.getResult().getStorage().getDownloadUrl().toString();

                            userRef.child("profileimage").setValue(downloadUrl)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){

                                                Intent selfIntent = new Intent(SetupActivity.this,SetupActivity.class);
                                                startActivity(selfIntent);


                                                loadingBar.dismiss();
                                                Toast.makeText(SetupActivity.this,"Profile image uploaded successfully...",Toast.LENGTH_SHORT).show();
                                            }

                                            else{

                                                loadingBar.dismiss();
                                                String message = task.getException().getMessage();
                                                Toast.makeText(SetupActivity.this,"Error..."+message,Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                        }
                    }
                });



            }

            else {
                loadingBar.dismiss();
                Toast.makeText(SetupActivity.this,"Please try again...",Toast.LENGTH_SHORT).show();
            }

        }



    }

    private void SaveAccountSetupInformation() {
        String bloodgroup = BloodGroup.getText().toString();
        String fullname = FullName.getText().toString();
        String district = District.getText().toString();
        String bloodcon = Bloodcon.getText().toString();

        if(TextUtils.isEmpty(bloodgroup)){
            Toast.makeText(this,"Please write your blood group",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(fullname)){
            Toast.makeText(this,"Please write your full name",Toast.LENGTH_SHORT).show();
        }

        else if(TextUtils.isEmpty(district)){
            Toast.makeText(this,"Please write your district",Toast.LENGTH_SHORT).show();
        }

        else if(TextUtils.isEmpty(bloodcon)){
            Toast.makeText(this,"Please write if you are able to donate ",Toast.LENGTH_SHORT).show();
        }

        else {



            loadingBar.setTitle("Saving Information");
            loadingBar.setMessage("Please wait...");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);


            HashMap userMap = new HashMap();
            userMap.put("bloodGroup",bloodgroup);
            userMap.put("fullname",fullname);
            userMap.put("district",district);
            userMap.put("Available ? ",bloodcon);
            userMap.put("status","Hello i am using BADHON");
            userMap.put("gender","none");
            userRef.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful()){

                        loadingBar.dismiss();
                        SendUserToMainActivity();
                        Toast.makeText(SetupActivity.this,"Your Account is created Successfully...",Toast.LENGTH_SHORT).show();
                    }

                    else {
                        loadingBar.dismiss();
                        String message = task.getException().getMessage();
                        Toast.makeText(SetupActivity.this,"Error Occured..." + message,Toast.LENGTH_SHORT).show();

                    }
                }
            });


        }

    }

    private void SendUserToMainActivity() {
        Intent mainIntent = new Intent(SetupActivity.this,MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }


}
