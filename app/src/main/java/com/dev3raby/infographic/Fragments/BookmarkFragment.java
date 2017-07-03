package com.dev3raby.infographic.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.dev3raby.infographic.App.AppConfig;
import com.dev3raby.infographic.App.AppController;
import com.dev3raby.infographic.Helper.SQLiteHandler;
import com.dev3raby.infographic.R;
import com.dev3raby.infographic.Adapters.BookMarkRecyclerViewAdapter;
import com.dev3raby.infographic.DataModels.MainDataModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ahmed Yehya on 11/06/2017.
 */

public class BookmarkFragment extends Fragment {
    public static final String ARG_EXAMPLE = "this is a constant";
    private String example_data;
    private boolean loading = true;
    Integer page = 1;
    private static JSONArray jsonArray;
    private SQLiteHandler db;
    private static String apiKey;
    private static String user_id;
    private static JSONObject infographic;
    private static boolean lastItem;
    Integer repeateConnection=0;
    ArrayList<MainDataModel> arrayList = new ArrayList<>();
    BookMarkRecyclerViewAdapter adapter;
    SwipeRefreshLayout swipLayout;



    private static RecyclerView mainRecyclerView;
    public static final String[] SOURCE_NAME= {"Omar Yehya", "أحمد العربي", "seha.com", "Ali.Kindy", "arinfographic","arinfographic"};
    public static final String[] INFOGRAPHIC_NAME= {"مراحل عملية التسويق", "التخزين السحابي", "كيف يمكن تحفيز الناس", "8 مؤشرات مرتبطة بمنفذي العمليات الانتحارية", "احصائيات العاب الفيديو حول العالم", "القادة والمدراء التربويين"};
    public static final String[] INFOGRAPHIC_IMAGE = {"https://shamssona.files.wordpress.com/2015/08/img_20150814_162450.jpg", "http://everyleader.net/sites/default/files/Infographic-every-leader-confrontations.jpg.png", "http://www.alarabyanews.com/upload/photo/gallery/9/7/960.jpg", "https://www.graphic-academy.com/wp-content/uploads/2016/05/image-11.jpeg", "https://s-media-cache-ak0.pinimg.com/736x/71/63/64/71636447376825c06dba6426110a2d91.jpg","https://mir-s3-cdn-cf.behance.net/project_modules/disp/741fe716989131.562b415c27a27.jpg"};
    public static final String[] SOURSE_ICON = {"https://www.iconfinder.com/data/icons/logotypes/32/twitter-128.png", "https://vignette2.wikia.nocookie.net/drunken-peasants-podcast/images/e/e0/Tumblr_icon.png", "http://beadage.net/wp-content/themes/beadage/images/icon-url.png", "aboveblue.gr/wp-content/uploads/2017/03/instagram_yeni_logo_app_2-300x300.png", "http://market.designmodo.com/wp-content/uploads/2015/06/behance-logo.png","http://diylogodesigns.com/blog/wp-content/uploads/2016/05/Instagram-logo-png-icon.png"};


    public BookmarkFragment() {

    }

    public static BookmarkFragment newInstance (String example_args)
    {
        BookmarkFragment homeFragment = new BookmarkFragment();
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
        getBookmark(page,user_id,apiKey);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_bookmarks,container,false);
        mainRecyclerView = (RecyclerView)
                rootView.findViewById(R.id.main_recycler_view);
        swipLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container);

        implementScrollListener();


        return rootView;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        LinearLayoutManager testLayoutManager = new LinearLayoutManager(getContext());
        mainRecyclerView
                .setLayoutManager(testLayoutManager);
        populatRecyclerView();
        swipLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                RequestQueue queue = AppController.getInstance().getRequestQueue();

                if (queue!=null)
                {
                    queue.cancelAll("req_get_bookmark");

                }
                arrayList.clear();
                page = 1;
                lastItem=false;
                getBookmark(page,user_id,apiKey);

            }
        });



        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStop() {

        lastItem = false;

        super.onStop();
    }

    private void populatRecyclerView() {

        adapter = new BookMarkRecyclerViewAdapter(getActivity(), arrayList);
        mainRecyclerView.setAdapter(adapter);// set adapter on recyclerview
        adapter.notifyDataSetChanged();
    }

    private void getBookmark(final Integer nPage, final String user_id, final String api_key) {
        // Tag used to cancel the request
        String tag_string_req = "req_get_bookmark";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_GET_BOOKMARK, new Response.Listener<String>() {

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

                                arrayList.add(new MainDataModel(infographic.getInt("id"), infographic.getString("name"), infographic.getString("source_name"), infographic.getString("type_icon_url"), infographic.getString("image_url")));


                                getActivity().runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {
                                        // your stuff to update the UI

                                        adapter.notifyDataSetChanged();
                                    }
                                });
                                if (swipLayout.isRefreshing())
                                {
                                    swipLayout.setRefreshing(false);
                                }
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
                    Toast.makeText(getContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();


                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {


                if (repeateConnection <= 3)
                {
                    getBookmark(nPage, user_id, api_key);
                    Toast.makeText(getActivity(),"error",Toast.LENGTH_SHORT).show();
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
                params.put("user_id", user_id);

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
            getBookmark(page,user_id,apiKey);


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
