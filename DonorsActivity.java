package kazinazrul.csedu24.badhon_knih;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class DonorsActivity extends AppCompatActivity {


    private RecyclerView myDonorList;
    private DatabaseReference donorsRef,usersRef;
    private FirebaseAuth mAuth;
    private String online_user_id;


    private Toolbar mToolbar;
    private ImageButton SearchButton;
    private EditText SearchInputText;








    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donors);


        mAuth = FirebaseAuth.getInstance();
        online_user_id = mAuth.getCurrentUser().getUid();
        donorsRef = FirebaseDatabase.getInstance().getReference().child("Donors").child(online_user_id);
        usersRef = FirebaseDatabase.getInstance().getReference().child("Users");


        myDonorList = findViewById(R.id.donor_list);
        LinearLayoutManager linearLayoutManager =new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        myDonorList.setLayoutManager(linearLayoutManager);


        DisplayAllDonors();

    }

    private void DisplayAllDonors() {

        FirebaseRecyclerOptions<Donors> options=new FirebaseRecyclerOptions.Builder<Donors>().setQuery(donorsRef,Donors.class).build();
        FirebaseRecyclerAdapter<Donors, DonorsViewHolder> adapter = new FirebaseRecyclerAdapter<Donors, DonorsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final DonorsViewHolder holder, int position, @NonNull final Donors model) {

                final String userIDs = getRef(position).getKey();


                usersRef.child(userIDs).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){

                            final String userName = dataSnapshot.child("fullname").getValue().toString();
                            final String profileImage = dataSnapshot.child("profileimage").getValue().toString();
                            model.setFullname(userName);
                            model.setProfileimage(profileImage);

                            holder.username.setText(model.getFullname());
                            holder.date.setText(" "+model.getDate());
                            Picasso.get().load(model.getProfileimage()).into(holder.profileimage);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @NonNull
            @Override
            public DonorsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
                View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.all_users_display_layout,viewGroup,false);
                DonorsViewHolder viewHolder=new DonorsViewHolder(view);
                return viewHolder;
            }
        };
        myDonorList.setAdapter(adapter);
        adapter.startListening();
    }
    public class DonorsViewHolder extends RecyclerView.ViewHolder{
        TextView username,date,time,description;
        CircleImageView profileimage;
        public DonorsViewHolder(View itemView) {
            super(itemView);

            username=itemView.findViewById(R.id.all_users_profile_fullname);
            date=itemView.findViewById(R.id.all_users_status);
            profileimage = itemView.findViewById(R.id.all_users_profile_image);
        }
    }




}
