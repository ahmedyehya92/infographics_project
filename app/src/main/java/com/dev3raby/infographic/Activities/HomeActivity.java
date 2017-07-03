package com.dev3raby.infographic.Activities;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;

import com.dev3raby.infographic.Fragments.BookmarkFragment;
import com.dev3raby.infographic.Fragments.ExploreFragment;
import com.dev3raby.infographic.Fragments.HomeFragment;
import com.dev3raby.infographic.Helper.SQLiteHandler;
import com.dev3raby.infographic.Helper.SessionManager;
import com.dev3raby.infographic.Helper.ViewPagerAdapter;
import com.dev3raby.infographic.R;


public class HomeActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private Button exitButton;
    private SessionManager session;
    private SQLiteHandler db;



    private int[] tabIcons = {R.drawable.ic_home_black_24dp,R.drawable.ic_explore_black_24dp,R.drawable.ic_collections_bookmark_black_24dp};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        session = new SessionManager(getApplicationContext());
        db = new SQLiteHandler(getApplicationContext());
        if (!session.isLoggedIn()) {
            logoutUser();
        }
        exitButton = (Button) findViewById(R.id.btn_exit);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();

            }
        });


        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);

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
