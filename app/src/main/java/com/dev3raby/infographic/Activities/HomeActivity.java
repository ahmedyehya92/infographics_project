package com.dev3raby.infographic.Activities;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.Toast;

import com.dev3raby.infographic.Fragments.BookmarkFragment;
import com.dev3raby.infographic.Fragments.ExploreFragment;
import com.dev3raby.infographic.Fragments.HomeFragment;
import com.dev3raby.infographic.Helper.FCMRegistrationService;
import com.dev3raby.infographic.Helper.FCMTokenRefreshListenerService;
import com.dev3raby.infographic.Helper.SQLiteHandler;
import com.dev3raby.infographic.Helper.SessionManager;
import com.dev3raby.infographic.Helper.ViewPagerAdapter;
import com.dev3raby.infographic.R;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class HomeActivity extends AppCompatActivity {
    final String titleKey = "titleKey";
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private Button exitButton, searchButton;
    private SessionManager session;
    private SQLiteHandler db;
    String title;



    private int[] tabIcons = {R.drawable.ic_home_black_24dp,R.drawable.ic_explore_black_24dp,R.drawable.ic_collections_bookmark_black_24dp};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Intent idIntent = getIntent();
        title = idIntent.getStringExtra(titleKey);
       // Log.e("Token is ", FirebaseInstanceId.getInstance().getToken());
        if (title!= null) {
            Toast.makeText(HomeActivity.this, title, Toast.LENGTH_SHORT).show();
        }
       // Toast.makeText(HomeActivity.this, FirebaseInstanceId.getInstance().getToken(), Toast.LENGTH_SHORT).show();
        session = new SessionManager(getApplicationContext());
        db = new SQLiteHandler(getApplicationContext());
        if (!session.isLoggedIn()) {
            logoutUser();
        }
        exitButton = (Button) findViewById(R.id.btn_exit);
        searchButton = (Button) findViewById(R.id.btn_search);

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();

            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);

        for (int i = 0; i < tabLayout.getChildCount(); i++)
        {
            tabLayout.getChildAt(i).setPadding(2,2,2,2);
        }
    }



    private void setupViewPager(ViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter (getSupportFragmentManager());

        adapter.addFragment(HomeFragment.newInstance("data for fragment 1"),"Home");
        adapter.addFragment(ExploreFragment.newInstance("data for fragment 2"),"Explore");
        adapter.addFragment(BookmarkFragment.newInstance("data for fragment 3"),"Bookmark");
        viewPager.setAdapter(adapter);




    }

    @Override
    protected void onStop() {

        super.onStop();
    }

    public void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
