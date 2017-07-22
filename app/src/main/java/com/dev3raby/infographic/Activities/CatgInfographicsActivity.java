package com.dev3raby.infographic.Activities;

import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.dev3raby.infographic.App.AppConfig;
import com.dev3raby.infographic.App.AppController;
import com.dev3raby.infographic.DataModels.LayoutTypeModel;
import com.dev3raby.infographic.DataModels.MainDataModel;
import com.dev3raby.infographic.Helper.EndLessScrollListener;
import com.dev3raby.infographic.Helper.SQLiteHandler;
import com.dev3raby.infographic.R;
import com.dev3raby.infographic.Adapters.CatgInfoRecyclerAdapter;
import com.dev3raby.infographic.DataModels.CatgInfoDataModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CatgInfographicsActivity extends AppCompatActivity {

    private static RecyclerView catgInfoRecyclerView;
    CatgInfoRecyclerAdapter adapter;
    private static boolean followed = false;
    private Button followButton, backButton;
    Integer repeateConnection=0;
    private static String apiKey;
    private static String user_id;
    LinearLayoutManager mainLayoutManager;
    private static JSONObject infographic;
    private static JSONArray jsonArray;
    private boolean loading = true;
    private static boolean lastItem;
    private SQLiteHandler db;
    final String categoryIdKey = "categoryIdKey";
    final String categoryNameKey = "categoryNameKey";
    private String category_id;
    private String category_name;
    Integer page = 1;
    private TextView catgTitle;
    CoordinatorLayout coordinatorLayout;
    Snackbar snackbar;




    private ArrayList<CatgInfoDataModel> arrayList = new ArrayList<>();


    public static final String[] SOURCE_NAME= {"Omar Yehya", "أحمد العربي", "maloomaday.com", "Ali.Kindy", "arinfographic","arinfographic"};
    public static final String[] INFOGRAPHIC_NAME= {"مراحل عملية التسويق", "التخزين السحابي", "كيف يمكن تحفيز الناس", "8 مؤشرات مرتبطة بمنفذي العمليات الانتحارية", "احصائيات العاب الفيديو حول العالم", "القادة والمدراء التربويين"};
    public static final String[] INFOGRAPHIC_IMAGE = {"https://shamssona.files.wordpress.com/2015/08/img_20150814_162450.jpg", "http://everyleader.net/sites/default/files/Infographic-every-leader-confrontations.jpg.png", "http://www.alarabyanews.com/upload/photo/gallery/9/7/960.jpg", "https://www.graphic-academy.com/wp-content/uploads/2016/05/image-11.jpeg", "https://s-media-cache-ak0.pinimg.com/736x/71/63/64/71636447376825c06dba6426110a2d91.jpg","https://mir-s3-cdn-cf.behance.net/project_modules/disp/741fe716989131.562b415c27a27.jpg"};
    public static final String[] SOURSE_ICON = {"https://www.iconfinder.com/data/icons/logotypes/32/twitter-128.png", "https://vignette2.wikia.nocookie.net/drunken-peasants-podcast/images/e/e0/Tumblr_icon.png", "http://beadage.net/wp-content/themes/beadage/images/icon-url.png", "aboveblue.gr/wp-content/uploads/2017/03/instagram_yeni_logo_app_2-300x300.png", "http://market.designmodo.com/wp-content/uploads/2015/06/behance-logo.png","http://diylogodesigns.com/blog/wp-content/uploads/2016/05/Instagram-logo-png-icon.png"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catg_infographics);

        db = new SQLiteHandler(getApplicationContext());
        HashMap<String, String> user = db.getUserDetails();


        user_id = user.get("user_id");
        apiKey = user.get("uid");

        Intent idIntent = getIntent();
        category_id = idIntent.getStringExtra(categoryIdKey);
        category_name = idIntent.getStringExtra(categoryNameKey);

        initView();
        catgTitle.setText(category_name);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               followAction(user_id,category_id,apiKey);
            }
        });
        initRecyclerViews();
        implementScrollListener();
        populatRecyclerView();

        getInfographics(page,user_id,apiKey,category_id);
      //  implementScrollListener(mainLayoutManager);

    }

    @Override
    public void onStop() {

        lastItem = false;
        super.onStop();
    }

    public void initView()
    {
        catgTitle = (TextView) findViewById(R.id.tx_category_title);
        followButton = (Button) findViewById(R.id.btn_follow);
        backButton = (Button) findViewById(R.id.btn_back);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);
    }

    public boolean isFollowed ()
    {
        if (!followed)
        {
            followButton.setBackgroundResource(R.drawable.unfollowed_btn_shape);
            followButton.setTextColor(getResources().getColor(R.color.actionButtons));
            followButton.setText(getResources().getString(R.string.follow_btn));
            return false;
        }
        else {
            followButton.setBackgroundResource(R.drawable.followed_btn_shape);
            followButton.setTextColor(getResources().getColor(R.color.white));
            followButton.setText(getResources().getString(R.string.followed_btn));
            return true;
        }
    }

    public void followButtonClick() {
        if (!followed)
        {
            followButton.setBackgroundResource(R.drawable.followed_btn_shape);
            followButton.setTextColor(getResources().getColor(R.color.white));
            followButton.setText(getResources().getString(R.string.followed_btn));
            followed = true;
        }
        else {

            followButton.setBackgroundResource(R.drawable.unfollowed_btn_shape);
            followButton.setTextColor(getResources().getColor(R.color.actionButtons));
            followButton.setText(getResources().getString(R.string.follow_btn));
            followed = false;
        }
    }

    private void initRecyclerViews() {
        // for infographics recyclerView
        catgInfoRecyclerView = (RecyclerView)
                findViewById(R.id.catginfo_recycler_view);
        catgInfoRecyclerView.setHasFixedSize(false);

        //Set RecyclerView type according to intent value
        mainLayoutManager =  new LinearLayoutManager(CatgInfographicsActivity.this);
        //  mainLayoutManager.setAutoMeasureEnabled(true);
        //  mainRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));// Here 2 is no. of columns to be displayed

        catgInfoRecyclerView
                .setLayoutManager(mainLayoutManager);
    }

    private void populatRecyclerView() {

        adapter = new CatgInfoRecyclerAdapter(CatgInfographicsActivity.this, arrayList);
        catgInfoRecyclerView.setAdapter(adapter);// set adapter on recyclerview
        adapter.notifyDataSetChanged();

    }

    private void getInfographics(final Integer nPage, final String user_id, final String api_key, final String categoryId) {
        // Tag used to cancel the request
        String tag_string_req = "req_getInfographicsCatL";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_GET_CATEGORISED_INFOGRAPHICS, new Response.Listener<String>() {

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
                        boolean isFollowed = jObj.getBoolean("isFollowed");
                        if (isFollowed)
                        {
                            followButton.setBackgroundResource(R.drawable.followed_btn_shape);
                            followButton.setTextColor(getResources().getColor(R.color.white));
                            followButton.setText(getResources().getString(R.string.followed_btn));
                        }
                        else {
                            followButton.setBackgroundResource(R.drawable.unfollowed_btn_shape);
                            followButton.setTextColor(getResources().getColor(R.color.actionButtons));
                            followButton.setText(getResources().getString(R.string.follow_btn));
                        }
                        jsonArray = jObj.getJSONArray("infographics");
                        if (jsonArray.length()==0)
                        {
                            lastItem = true;

                        }

                        else {



                            for (int i = 0; i < jsonArray.length(); i++) {

                                infographic = jsonArray.getJSONObject(i);

                                arrayList.add(new CatgInfoDataModel(infographic.getInt("id"), infographic.getString("name"), infographic.getString("source_name"), infographic.getString("type_icon_url"), infographic.getString("image_url"), infographic.getInt("like_counter"), infographic.getInt("seen")));


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
                    getInfographics(nPage, user_id, api_key, categoryId);
                    repeateConnection++;
                }
                else
                {

                    String msg = "خطأ في الإتصال برجاء المحاولة لاحقا";
                    snackbar = Snackbar
                            .make(coordinatorLayout, "تعذر تحديث البيانات! تحقق من اتصالك بالشبكة", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    //showAlertDialog(msg);


                }



            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("page", nPage.toString());
                params.put("user_id", user_id);
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

    private void followAction(final String user_id,final String category_id, final String api_key) {
        // Tag used to cancel the request

        String tag_string_req = "req_follow_action";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_Follow_Action, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {


                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");


                    // Check for error node in json
                    if (!error) {

                        Boolean isFollowed = jObj.getBoolean("isFollowed");
                        if (isFollowed)
                        {
                            followButton.setBackgroundResource(R.drawable.followed_btn_shape);
                            followButton.setTextColor(getResources().getColor(R.color.white));
                            followButton.setText(getResources().getString(R.string.followed_btn));

                      /*      Integer pos = position;
                            Toast.makeText(context, "تمت متابعة القسم " + currentCategory.getCategoryName(),Toast.LENGTH_SHORT).show();
                            FollowinActivity.categoriesList.remove(position);

                            notifyDataSetChanged(); */


                            snackbar = Snackbar
                                    .make(coordinatorLayout, "تمت متابعة القسم", Snackbar.LENGTH_LONG);
                            snackbar.show();

                        }
                        else
                        {

                            followButton.setBackgroundResource(R.drawable.unfollowed_btn_shape);
                            followButton.setTextColor(getResources().getColor(R.color.actionButtons));
                            followButton.setText(getResources().getString(R.string.follow_btn));


                            snackbar = Snackbar
                                    .make(coordinatorLayout, "تم إلغاء المتابعة", Snackbar.LENGTH_LONG);
                            snackbar.show();

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



                String msg = "خطأ في الإتصال برجاء المحاولة لاحقا";
                //showAlertDialog(msg);

                Toast.makeText(CatgInfographicsActivity.this,msg,Toast.LENGTH_SHORT).show();
                snackbar = Snackbar
                        .make(coordinatorLayout, "خطأ في الإتصال تأكد من اتصالك بالشبكة", Snackbar.LENGTH_LONG);
                snackbar.show();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", user_id);
                params.put("category_id",category_id);


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



    private void implementScrollListener(LinearLayoutManager mainLayoutManager) {

        catgInfoRecyclerView.addOnScrollListener(new EndLessScrollListener(mainLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                loadMoreVideos();
            }
        });


    }

    private void implementScrollListener() {

        catgInfoRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                        int visibleItemCount = catgInfoRecyclerView.getLayoutManager().getChildCount();
                        int  totalItemCount = catgInfoRecyclerView.getLayoutManager().getItemCount();
                        int  pastVisiblesItems = ((LinearLayoutManager) catgInfoRecyclerView.getLayoutManager()).findFirstVisibleItemPosition();

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
            getInfographics(page, user_id, apiKey, category_id);


        } else {

        }
    }

}
