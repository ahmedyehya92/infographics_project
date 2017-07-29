package com.dev3raby.infographic.Activities;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.dev3raby.infographic.App.AppConfig;
import com.dev3raby.infographic.App.AppController;
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

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;



public class HomeActivity extends AppCompatActivity {
    final String titleKey = "titleKey";
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private Button exitButton, searchButton;
    private SessionManager session;
    private SQLiteHandler db;
    String title;
    String versionCode;
    Integer repeateConnection=0;
    CoordinatorLayout coordinatorLayout;
    Snackbar snackbar;
    private static String apiKey;
    ViewPagerAdapter adapter;







    private int[] tabIcons = {R.drawable.ic_home_black_24dp,R.drawable.ic_explore_black_24dp,R.drawable.ic_collections_bookmark_black_24dp};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        db = new SQLiteHandler(HomeActivity.this);
        HashMap<String, String> user = db.getUserDetails();

        apiKey = user.get("uid");


        coordinatorLayout = (CoordinatorLayout)findViewById(R.id.coordinator_layout);


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

        adapter = new ViewPagerAdapter (getSupportFragmentManager());

        adapter.addFragment(HomeFragment.newInstance("data for fragment 1"),"Home");
        adapter.addFragment(ExploreFragment.newInstance("data for fragment 2"),"Explore");
        adapter.addFragment(BookmarkFragment.newInstance("data for fragment 3"),"Bookmark");
        viewPager.setAdapter(adapter);




    }

    @Override
    protected void onStart() {
        checkUpdate(getVersionInfo(),apiKey);

        super.onStart();




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
    private void checkUpdate(final String versionCode, final String api_key) {
        // Tag used to cancel the request





        String tag_string_req = "req_check_update";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_CHECK_UPDATE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                repeateConnection=0;
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    boolean isNeedUpdate = jObj.getBoolean("status");

                    if (isNeedUpdate)
                    {
                        showalertDialog();
                    }
                    else
                    {


                    }





                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();


                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {


                if (repeateConnection <= 3)
                {
                    checkUpdate(versionCode, api_key);
                    snackbar = Snackbar
                            .make(coordinatorLayout, "تعذر التحديث تحقق من اتصالك بالشبكة", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    repeateConnection++;
                }
                else
                {

                    String msg = "خطأ في الإتصال برجاء المحاولة لاحقا";



                }



            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("version_code", versionCode);


                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();

                headers.put("Authorization", api_key);
                return headers;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public void showalertDialog()
    {
        final Dialog dialog = new Dialog(HomeActivity.this);
        // Include dialog.xml file
        dialog.setContentView(R.layout.update_alert_dialog);

        // Set dialog title
        dialog.setTitle(R.string.title_update_dialog);
        dialog.setCancelable(false);
        // set values for custom dialog components - text, image and button
        dialog.show();


        Button exitButton = (Button) dialog.findViewById(R.id.btn_exit);
        // if decline button is clicked, close the custom dialog
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close dialog

                dialog.dismiss();
                finish();
                System.exit(0);

            }
        });
        Button updateButton = (Button) dialog.findViewById(R.id.btn_update);
        // if decline button is clicked, close the custom dialog
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close dialog

                dialog.dismiss();
                Intent intent;
                intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=" + getPackageName()));
                startActivity(intent);
                finish();
                System.exit(0);

            }
        });

    }

    private String getVersionInfo() {
        String versionName = "";
        Integer versionCode = -1;
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionName = packageInfo.versionName;
            versionCode = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return versionCode.toString();
    }

}
