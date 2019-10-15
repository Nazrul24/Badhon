package kazinazrul.csedu24.badhon_knih;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class PostActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private ImageButton SelectPostImage;
    private Button UpdatePostButton;
    private EditText PostDescription;
    private static final int Gallery_Pick = 1;
    private Uri imaageUri;
    private String Description;

    private StorageReference PostImageReference;

    private String saveCurrentDate,saveCurrentTime,postRandomName;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        PostImageReference = FirebaseStorage.getInstance().getReference();

        SelectPostImage = (ImageButton) findViewById(R.id.select_post_image);
        UpdatePostButton = (Button) findViewById(R.id.update_post_button);
        PostDescription = (EditText) findViewById(R.id.post_description);


        mToolbar = (Toolbar) findViewById(R.id.update_post_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Update Post");



        SelectPostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenGallery();
            }
        });

        UpdatePostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValidPostInfo();
            }
        });


    }

    private void ValidPostInfo() {
        Description = PostDescription.getText().toString();
        if(TextUtils.isEmpty(Description)){
            Toast.makeText(this,"Please write your post....",Toast.LENGTH_SHORT).show();
        }
        
        else{
            StoringImageToStorage();
        }
    }

    private void StoringImageToStorage() {
        Calendar callDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
        saveCurrentDate = currentDate.format(callDate.getTime());


        Calendar callTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
        saveCurrentTime = currentTime.format(callTime.getTime());

        postRandomName = saveCurrentDate+saveCurrentTime;


        StorageReference filepath = PostImageReference.child("Post Images").child(imaageUri.getLastPathSegment() + postRandomName + ".jpg");

        filepath.putFile(imaageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){
                    Toast.makeText(PostActivity.this,"Image Uploaded Successfully...",Toast.LENGTH_SHORT).show();
                }
                else {
                    String message = task.getException().getMessage();
                    Toast.makeText(PostActivity.this,"Error Occured..." + message,Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void OpenGallery() {

        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,Gallery_Pick);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==Gallery_Pick && resultCode == RESULT_OK && data!=null){
            imaageUri = data.getData();
            SelectPostImage.setImageURI(imaageUri);

        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            sendUserToMainActivity();
        }
        return super.onOptionsItemSelected(item);

    }

    private void sendUserToMainActivity() {
        Intent mainIntent = new Intent(PostActivity.this,MainActivity.class);
        startActivity(mainIntent);

    }
}
