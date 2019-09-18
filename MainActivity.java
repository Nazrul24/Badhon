package kazinazrul.csedu24.badhon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private RecyclerView postList;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawable_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);

        View navView = navigationView.inflateHeaderView(R.layout.navigation_header);


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                UserMenuSelector(item);
                return false;
            }
        });

    }

    private void UserMenuSelector(MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_profile:
                Toast.makeText(this,"Profile",Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_home:
                Toast.makeText(this,"Home",Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_message:
                Toast.makeText(this,"Message",Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_notification:
                Toast.makeText(this,"Notification",Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_settings:
                Toast.makeText(this,"Settings",Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_donor:
                Toast.makeText(this,"Donor",Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_post:
                Toast.makeText(this,"Post",Toast.LENGTH_SHORT).show();
                break;

                case R.id.nav_logout:
                Toast.makeText(this,"Logout",Toast.LENGTH_SHORT).show();
                break;


        }
    }
}
