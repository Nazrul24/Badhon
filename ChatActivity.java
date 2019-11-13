package kazinazrul.csedu24.badhon_knih;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    private Toolbar chatToolBar;
    private ImageButton SendMessageButton,SendImageFileButton;
    private EditText userMessageInput;
    private RecyclerView userMessageList;
    private String messageRecieverID,messageRecieverName,messageSenderID,saveCurrentTime,saveCurrentDate;

    private TextView recieverName;
    private CircleImageView recieverProfileImage;
    private DatabaseReference RootRef;
    private FirebaseAuth mAuth;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mAuth = FirebaseAuth.getInstance();
        messageSenderID = mAuth.getCurrentUser().getUid();

        RootRef = FirebaseDatabase.getInstance().getReference();
        messageRecieverID = getIntent().getExtras().get("visit_user_id").toString();
        messageRecieverName = getIntent().getExtras().get("userName").toString();


        InitializeFields();
        DisplayRecieverInfo();

        SendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendMessage();
            }
        });

    }

    private void SendMessage() {
        String messageText = userMessageInput.getText().toString();
        if(TextUtils.isEmpty(messageText)){
            Toast.makeText(this,"Please write a message first...",Toast.LENGTH_SHORT).show();
        }
        else{
            String message_sender_ref = "Messages/"+messageSenderID + "/"+messageRecieverID;
            String message_reciever_ref = "Messages/"+messageRecieverID + "/"+messageSenderID;
            DatabaseReference user_message_key = RootRef.child("Messages").child(messageSenderID).child(messageRecieverID).push();

            String message_push_id = user_message_key.getKey();

            Calendar callDate = Calendar.getInstance();
            SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
            saveCurrentDate = currentDate.format(callDate.getTime());


            Calendar callTime = Calendar.getInstance();
            SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm aa");
            saveCurrentTime = currentTime.format(callTime.getTime());

            Map messageTextBody = new HashMap();

            messageTextBody.put("message",messageText);
            messageTextBody.put("time",saveCurrentTime);
            messageTextBody.put("date",saveCurrentDate);
            messageTextBody.put("type","text");
            messageTextBody.put("from",messageSenderID);

            Map messageBodyDetails = new HashMap();
            messageBodyDetails.put(message_sender_ref+"/"+message_push_id,messageTextBody);
            messageBodyDetails.put(message_reciever_ref+"/"+message_push_id,messageTextBody);


            RootRef.updateChildren(messageBodyDetails).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful()){
                        Toast.makeText(ChatActivity.this,"Sent",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        String message = task.getException().getMessage();
                        Toast.makeText(ChatActivity.this,"Error..."+message,Toast.LENGTH_SHORT).show();
                    }

                    userMessageInput.setText("");
                }
            });


        }
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
