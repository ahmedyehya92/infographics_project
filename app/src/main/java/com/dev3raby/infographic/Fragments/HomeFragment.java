package com.dev3raby.infographic.Fragments;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.dev3raby.infographic.Activities.MainActivity;
import com.dev3raby.infographic.App.AppConfig;
import com.dev3raby.infographic.App.AppController;
import com.dev3raby.infographic.Helper.SQLiteHandler;
import com.dev3raby.infographic.Helper.SessionManager;
import com.dev3raby.infographic.R;
import com.dev3raby.infographic.RecyclerViewAdapters.MainRecyclerViewAdapter;
import com.dev3raby.infographic.RecyclerViewModels.MainDataModel;
import com.dev3raby.infographic.UI.SimpleDividerItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.support.v7.recyclerview.R.attr.layoutManager;

/**
 * Created by Ahmed Yehya on 11/06/2017.
 */

public class HomeFragment extends Fragment {
    public static final String ARG_EXAMPLE = "this is a constant";
    private String example_data;
    private boolean loading = true;
    boolean userScrolled = false;
    Integer page = 1;
    private static JSONArray jsonArray;
    private SQLiteHandler db;
    private SessionManager session;
    private static int status;
    private static String apiKey;
    private static String user_id;
    private static JSONObject infographic;
    private static boolean lastItem;
    private static Integer repeateConnection=0;

    ArrayList<MainDataModel> arrayList = new ArrayList<>();

    private static RecyclerView mainRecyclerView;
    MainRecyclerViewAdapter adapter;

    public static final String[] SOURCE_NAME= {"Saud Aljaber", "أحمد العربي", "maloomaday.com", "Ali.Kindy", "arinfographic","arinfographic"};
    public static final String[] INFOGRAPHIC_NAME= {"مراحل عملية التسويق", "التخزين السحابي", "كيف يمكن تحفيز الناس", "8 مؤشرات مرتبطة بمنفذي العمليات الانتحارية", "احصائيات العاب الفيديو حول العالم", "القادة والمدراء التربويين"};
    public static final String[] INFOGRAPHIC_IMAGE = {"https://shamssona.files.wordpress.com/2015/08/img_20150814_162450.jpg", "http://everyleader.net/sites/default/files/Infographic-every-leader-confrontations.jpg.png", "http://www.alarabyanews.com/upload/photo/gallery/9/7/960.jpg", "https://www.graphic-academy.com/wp-content/uploads/2016/05/image-11.jpeg", "https://s-media-cache-ak0.pinimg.com/736x/71/63/64/71636447376825c06dba6426110a2d91.jpg","https://mir-s3-cdn-cf.behance.net/project_modules/disp/741fe716989131.562b415c27a27.jpg"};
    public static final String[] SOURSE_ICON = {"https://www.iconfinder.com/data/icons/logotypes/32/twitter-128.png", "https://vignette2.wikia.nocookie.net/drunken-peasants-podcast/images/e/e0/Tumblr_icon.png", "http://beadage.net/wp-content/themes/beadage/images/icon-url.png", "aboveblue.gr/wp-content/uploads/2017/03/instagram_yeni_logo_app_2-300x300.png", "http://market.designmodo.com/wp-content/uploads/2015/06/behance-logo.png","http://diylogodesigns.com/blog/wp-content/uploads/2016/05/Instagram-logo-png-icon.png"};


    public HomeFragment () {

    }

    public static HomeFragment newInstance (String example_args)
    {
        HomeFragment homeFragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_EXAMPLE, example_args);
        homeFragment.setArguments(args);
        return homeFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        example_data = getArguments().getString(ARG_EXAMPLE);
        db = new SQLiteHandler(getContext());
        HashMap<String, String> user = db.getUserDetails();


        user_id = user.get("user_id");
        apiKey = user.get("uid");
        getInfographics(page.toString(),user_id,apiKey);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home,container,false);
        mainRecyclerView = (RecyclerView)
                rootView.findViewById(R.id.main_recycler_view);
        implementScrollListener();

        return rootView;


    }

    @Override
    public void onStop() {

        lastItem = false;
        super.onStop();
    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        StaggeredGridLayoutManager mainLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        LinearLayoutManager testLayoutManager = new LinearLayoutManager(getContext());
        mainRecyclerView
                .setLayoutManager(testLayoutManager);
        populatRecyclerView();

        super.onViewCreated(view, savedInstanceState);
    }

    private void populatRecyclerView() {

        adapter = new MainRecyclerViewAdapter(getActivity(), arrayList);
        mainRecyclerView.setAdapter(adapter);// set adapter on recyclerview
        adapter.notifyDataSetChanged();


    }

    private void getInfographics(final String nPage, final String user_id, final String api_key) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_GET_FLLOWED_INFOGRAPHICS, new Response.Listener<String>() {

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


                        for (int i = 0; i < jsonArray.length(); i++) {

                            infographic = jsonArray.getJSONObject(i);
                            arrayList.add(new MainDataModel(infographic.getInt("id"), infographic.getString("name"), infographic.getString("source_name"), infographic.getString("type_icon_url"), infographic.getString("image_url")));




                            getActivity().runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    // your stuff to update the UI
                                    adapter.notifyDataSetChanged();
                                }
                            });
                         loading=true;
                        }

                    } else {
                        // Error in login. Get the error message
                        //final String errorMsg = jObj.getString("message");

                        //showAlertDialog(errorMsg);
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();


                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {


                if (repeateConnection <= 3)
                {
                    getInfographics(nPage, user_id, api_key);
                   // Toast.makeText(LoginActivity.this,"error",Toast.LENGTH_SHORT).show();
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
                params.put("userid", user_id);

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
        if (lastItem!= true) {
            page++;
            getInfographics(page.toString(),user_id,apiKey);


        }

        else {

        }

        // notify adapter

        // Toast for task completion


        // After adding new data hide the view.

    /*    new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                // Loop for 3 items

                loadBar.setVisibility(View.GONE);

                // asynctask



            }
        }, 5000); */
    }
}
