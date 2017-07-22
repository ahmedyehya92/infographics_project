package com.dev3raby.infographic.Activities;

import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.dev3raby.infographic.Adapters.SearchRecyclerViewAdapter;
import com.dev3raby.infographic.App.AppConfig;
import com.dev3raby.infographic.App.AppController;
import com.dev3raby.infographic.DataModels.MainDataModel;
import com.dev3raby.infographic.Helper.SQLiteHandler;
import com.dev3raby.infographic.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private Button backButton;
    ArrayList<MainDataModel> arrayList = new ArrayList<>();
    private boolean loading = true;
    Integer page = 1;
    private static JSONArray jsonArray;
    private SQLiteHandler db;
    private static String apiKey;
    private static JSONObject infographic;
    private static boolean lastItem;
    Integer repeateConnection=0;
    SearchRecyclerViewAdapter adapter;
    private SearchView mSearchView;
    private static RecyclerView mainRecyclerView;
    LinearLayoutManager mainLayoutManager;
    String queryC;
    final String notificationKey = "idKey";
    String notificationId;
    private AdView mAdView;
    CoordinatorLayout coordinatorLayout;
    Snackbar snackbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mAdView.loadAd(adRequest);
        Intent idIntent = getIntent();
        notificationId = idIntent.getStringExtra(notificationKey);
        initiView();
        db = new SQLiteHandler(getApplicationContext());

        HashMap<String, String> user = db.getUserDetails();

        apiKey = user.get("uid");

        mSearchView.animate();

        initRecyclerViews();

        populatRecyclerView();

        if (notificationId!=null)
        {
            getInfographics(apiKey,notificationId);
        }

        setupSearchView();
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    @Override
    protected void onStop() {
        super.onStop();
        lastItem=false;
    }

    public void initiView()
    {
        coordinatorLayout = (CoordinatorLayout)findViewById(R.id.coordinator_layout);
        mSearchView = (SearchView) findViewById(R.id.search_view);
        backButton = (Button) findViewById(R.id.btn_back);
    }

    private void setupSearchView() {
        if (notificationId==null) {
            mSearchView.setIconifiedByDefault(false);
        }
        else
        {
            mSearchView.setIconifiedByDefault(true);
        }
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setSubmitButtonEnabled(true);



    }

    public boolean onQueryTextChange(String newText) {

        return true;
    }

    public boolean onQueryTextSubmit(String query) {
        implementScrollListener();
        queryC = query;
        arrayList.clear();
        adapter.notifyDataSetChanged();
        page=1;
        loading=true;
        lastItem = false;
        searchQuery(page,query,apiKey);
        return false;
    }

    private void initRecyclerViews() {
        // for infographics recyclerView
        mainRecyclerView = (RecyclerView)
                findViewById(R.id.search_recycler_view);
        mainRecyclerView.setHasFixedSize(false);

        //Set RecyclerView type according to intent value
        mainLayoutManager =  new LinearLayoutManager(SearchActivity.this);
        //  mainLayoutManager.setAutoMeasureEnabled(true);
        //  mainRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));// Here 2 is no. of columns to be displayed

        mainRecyclerView
                .setLayoutManager(mainLayoutManager);
    }

    private void populatRecyclerView() {

        adapter = new SearchRecyclerViewAdapter(SearchActivity.this, arrayList);
        mainRecyclerView.setAdapter(adapter);// set adapter on recyclerview
        adapter.notifyDataSetChanged();

    }

    private void implementScrollListener() {

        mainRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }


            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {

                if (loading) {


                    if (dy > 0) //check for scroll down
                    {
                        int visibleItemCount = mainRecyclerView.getLayoutManager().getChildCount();
                        int  totalItemCount = mainRecyclerView.getLayoutManager().getItemCount();
                        int  pastVisiblesItems = ((LinearLayoutManager) mainRecyclerView.getLayoutManager()).findFirstVisibleItemPosition();

                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            loading = false;

                            Log.v("...", " Reached Last Item");

                            loadMoreVideos();
                        }

                    }
                }
            }
        });


    }


    private void loadMoreVideos() {
        // Show Progress Layout

        // Handler to show refresh for a period of time you can use async task
        // while commnunicating serve
        if (lastItem != true) {
            page++;
            searchQuery(page, queryC, apiKey);


        } else {

        }
    }

    private void searchQuery(final Integer nPage, final String query, final String api_key) {
        // Tag used to cancel the request





        String tag_string_req = "req_search";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_SEARCH, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                repeateConnection=0;
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");


                    // Check for error node in json
                    if (!error) {
                        // user successfully logged in
                        // Create login session

                        jsonArray = jObj.getJSONArray("infographics");
                        if (jsonArray.length()==0)
                        {
                            lastItem = true;

                        }

                        else {



                            for (int i = 0; i < jsonArray.length(); i++) {

                                infographic = jsonArray.getJSONObject(i);

                                arrayList.add(new MainDataModel(infographic.getInt("id"), infographic.getString("name"), infographic.getString("source_name"), infographic.getString("type_icon_url"), infographic.getString("image_url"), infographic.getInt("like_counter"), infographic.getInt("seen")));


                                runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {
                                        // your stuff to update the UI

                                        adapter.notifyDataSetChanged();
                                    }
                                });
                                loading = true;
                            }





                            //   adsArrayList.add(mRecyclerViewItems.size()-1);


                            //  addNativeExpressAds(mRecyclerViewItems.size()-2);














                        }

                    } else {
                        // Error in login. Get the error message
                        //final String errorMsg = jObj.getString("message");

                        //showAlertDialog(errorMsg);
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
                    searchQuery(nPage, query, api_key);
                    snackbar = Snackbar
                            .make(coordinatorLayout, "تحقق من اتصالك بالشبكة", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    repeateConnection++;
                }
                else
                {

                    String msg = "خطأ في الإتصال برجاء المحاولة لاحقا";
                    //showAlertDialog(msg);


                }



            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("page", nPage.toString());
                params.put("query", query);

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

    private void getInfographics(final String api_key, final String categoryId) {
        // Tag used to cancel the request
        String tag_string_req = "req_getInfographicsNoti";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_GET_NOTIFIED_INFOGRAPHICS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                repeateConnection=0;
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");


                    // Check for error node in json
                    if (!error) {
                        // user successfully logged in
                        // Create login session

                        jsonArray = jObj.getJSONArray("infographics");




                            for (int i = 0; i < jsonArray.length(); i++) {

                                infographic = jsonArray.getJSONObject(i);

                                arrayList.add(new MainDataModel(infographic.getInt("id"), infographic.getString("name"), infographic.getString("source_name"), infographic.getString("type_icon_url"), infographic.getString("image_url"), infographic.getInt("like_counter"), infographic.getInt("seen")));


                                runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {
                                        // your stuff to update the UI

                                        adapter.notifyDataSetChanged();
                                    }
                                });

                            }



                            //   adsArrayList.add(mRecyclerViewItems.size()-1);


                            //  addNativeExpressAds(mRecyclerViewItems.size()-2);
















                    } else {
                        // Error in login. Get the error message
                        //final String errorMsg = jObj.getString("message");

                        //showAlertDialog(errorMsg);
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
                    getInfographics(api_key, categoryId);
                    snackbar = Snackbar
                            .make(coordinatorLayout, "تحقق من اتصالك بالشبكة", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    repeateConnection++;
                }
                else
                {

                    String msg = "خطأ في الإتصال برجاء المحاولة لاحقا";
                    //showAlertDialog(msg);


                }



            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("categoryid", categoryId);



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
}
