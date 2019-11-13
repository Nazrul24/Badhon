package kazinazrul.csedu24.badhon_knih;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    private Toolbar chatToolBar;
    private ImageButton SendMessageButton,SendImageFileButton;
    private EditText userMessageInput;
    private RecyclerView userMessageList;
    private String messageRecieverID,messageRecieverName;

    private TextView recieverName;
    private CircleImageView recieverProfileImage;
    private DatabaseReference RootRef;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        RootRef = FirebaseDatabase.getInstance().getReference();
        messageRecieverID = getIntent().getExtras().get("visit_user_id").toString();
        messageRecieverName = getIntent().getExtras().get("userName").toString();


        InitializeFields();
        DisplayRecieverInfo();

    }

    private void DisplayRecieverInfo() {
        recieverName.setText(messageRecieverName);
        RootRef.child("Users").child(messageRecieverID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    final String profileImage = dataSnapshot.child("profileimage").getValue().toString();
                    Picasso.get().load(profileImage).placeholder(R.drawable.profile).into(recieverProfileImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void InitializeFields() {
        chatToolBar = findViewById(R.id.chat_bar_layout);
        setSupportActionBar(chatToolBar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);

        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View action_bar_view = layoutInflater.inflate(R.layout.chat_custom_bar,null);
        actionBar.setCustomView(action_bar_view);

        recieverName = findViewById(R.id.custom_profile_name);
        recieverProfileImage = findViewById(R.id.custom_profile_image);

        SendMessageButton = findViewById(R.id.send_message_button);
        SendImageFileButton = findViewById(R.id.send_image_file_button);
        userMessageInput = findViewById(R.id.input_message) ;
        userMessageList = findViewById(R.id.messages_list_users) ;

    }
}
