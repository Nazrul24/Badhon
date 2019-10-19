package kazinazrul.csedu24.badhon_knih;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class FindDonorActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private ImageButton SearchButton;
    private EditText SearchInputText;

    private RecyclerView SearchResultList;
    //private FirebaseAuth mAuth;
    private DatabaseReference userRef;

    String currentUserID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_donor);


        mToolbar =  findViewById(R.id.find_donor_appbar_layout);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Find Donors");

        SearchResultList = findViewById(R.id.search_result_list);
        SearchResultList.setHasFixedSize(true);
        SearchResultList.setLayoutManager(new LinearLayoutManager(this));

        SearchButton = findViewById(R.id.search_people_donors_button);
        SearchInputText = findViewById(R.id.search_box_input);

        //mAuth=FirebaseAuth.getInstance();
        //currentUserID = mAuth.getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        //PostRef =  FirebaseDatabase.getInstance().getReference().child("Posts");

        SearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchBoxInput = SearchInputText.getText().toString();

                SearchPeopleAndDonors(searchBoxInput);

            }
        });

    }


    private void SearchPeopleAndDonors(String searchBoxInput)
    {

        Query SearchDonorQuery = userRef.orderByChild("bloodGroup")
                .startAt(searchBoxInput).endAt(searchBoxInput + "\uf8ff");


        FirebaseRecyclerOptions<FindOthers> options=new FirebaseRecyclerOptions.Builder<FindOthers>().
                setQuery(SearchDonorQuery, FindOthers.class).build(); //query build past the query to FirebaseRecyclerAdapter
        FirebaseRecyclerAdapter<FindOthers, FindDonorActivity.FindFriendViewHolder> adapter=new FirebaseRecyclerAdapter<FindOthers, FindDonorActivity.FindFriendViewHolder>(options)
        {
            @Override
            protected void onBindViewHolder(@NonNull FindDonorActivity.FindFriendViewHolder holder, int position, @NonNull FindOthers model)
            {
                final String PostKey = getRef(position).getKey();
                holder.username.setText(model.getFullname());
                holder.status.setText(model.getStatus());
                Picasso.get().load(model.getProfileimage()).into(holder.profileimage);

                holder.itemView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Intent findOthersIntent = new Intent(FindDonorActivity.this, FindDonorActivity.class);
                        findOthersIntent.putExtra("PostKey", PostKey);
                        startActivity(findOthersIntent);

                    }
                });
            }
            @NonNull
            @Override
            public FindDonorActivity.FindFriendViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
            {
                View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.all_users_display_layout,viewGroup,false);

                FindDonorActivity.FindFriendViewHolder viewHolder=new FindDonorActivity.FindFriendViewHolder(view);
                return viewHolder;
            }
        };

        SearchResultList.setAdapter(adapter);
        adapter.startListening();
    }

    public class FindFriendViewHolder extends RecyclerView.ViewHolder
    {
        TextView username, status;
        CircleImageView profileimage;

        public FindFriendViewHolder(@NonNull View itemView)
        {
            super(itemView);
            username = itemView.findViewById(R.id.all_users_profile_fullname);
            status = itemView.findViewById(R.id.all_users_status);
            profileimage = itemView.findViewById(R.id.all_users_profile_image);
        }
    }
}
