package com.dev3raby.infographic.Fragments;

import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.dev3raby.infographic.App.AppConfig;
import com.dev3raby.infographic.App.AppController;
import com.dev3raby.infographic.Helper.SQLiteHandler;
import com.dev3raby.infographic.Helper.SessionManager;
import com.dev3raby.infographic.R;
import com.dev3raby.infographic.Adapters.MainRecyclerViewAdapter;
import com.dev3raby.infographic.DataModels.LayoutTypeModel;
import com.dev3raby.infographic.DataModels.MainDataModel;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.NativeExpressAdView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ahmed Yehya on 11/06/2017.
 */

public class HomeFragment extends Fragment {
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
    private static final int NATIVE_EXPRESS_AD_HEIGHT = 150;
    private static final String AD_UNIT_ID = "ca-app-pub-3940256099942544/1072772517";
    public static int ITEMS_PER_AD ;
    public static boolean AD;
    public static int isThereAds = 1;

    private List<Object> mRecyclerViewItems = new ArrayList<>();
    private List<Object> typeItemList = new ArrayList<>();

    private static RecyclerView mainRecyclerView;
    MainRecyclerViewAdapter adapter;

    RelativeLayout relativeLayout;
    Snackbar snackbar;

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

        //list title item
        typeItemList.add(new LayoutTypeModel(2));
        mRecyclerViewItems.add(2);  // any thing just for reserve item in recyclerview

        getInfographics(page,user_id,apiKey);





    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home,container,false);
        mainRecyclerView = (RecyclerView)
                rootView.findViewById(R.id.main_recycler_view);


        relativeLayout = (RelativeLayout)rootView.findViewById(R.id.relativelayout);
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


       adapter = new MainRecyclerViewAdapter(getActivity(), mRecyclerViewItems, typeItemList);
       mainRecyclerView.setAdapter(adapter);// set adapter on recyclerview
       adapter.notifyDataSetChanged();


    }

    private void getInfographics(final Integer nPage, final String user_id, final String api_key) {
        // Tag used to cancel the request





        String tag_string_req = "req_getInfographics";

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

                        else {

                            ITEMS_PER_AD = jsonArray.length()+1;

                            for (int i = 0; i < jsonArray.length(); i++) {
                                 AD = false;
                                infographic = jsonArray.getJSONObject(i);

                                mRecyclerViewItems.add(new MainDataModel(infographic.getInt("id"), infographic.getString("name"), infographic.getString("source_name"), infographic.getString("type_icon_url"), infographic.getString("image_url"), infographic.getInt("like_counter"), infographic.getInt("seen")));
                                typeItemList.add(new LayoutTypeModel(1));

                                getActivity().runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {
                                        // your stuff to update the UI

                                        adapter.notifyDataSetChanged();
                                    }
                                });
                                loading = true;
                            }

                            addNativeAds();

                            typeItemList.add(new LayoutTypeModel(0));

                         //   adsArrayList.add(mRecyclerViewItems.size()-1);


                              //  addNativeExpressAds(mRecyclerViewItems.size()-2);









                            Integer s = mRecyclerViewItems.size();
                          //  Toast.makeText(getActivity(),s.toString(),Toast.LENGTH_SHORT).show();
                            Log.d("size =",s.toString());




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
                    getInfographics(nPage, user_id, api_key);
                    snackbar = Snackbar
                            .make(relativeLayout, "تعذر التحديث تحقق من اتصالك بالشبكة", Snackbar.LENGTH_LONG);
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
            getInfographics(page,user_id,apiKey);


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

    private void addNativeExpressAds() {

        // Loop through the items array and place a new Native Express ad in every ith position in
        // the items List.

            final NativeExpressAdView adView = new NativeExpressAdView(getActivity());
            mRecyclerViewItems.add(adView);

    }

    private void setUpAndLoadNativeExpressAds() {
        // Use a Runnable to ensure that the RecyclerView has been laid out before setting the
        // ad size for the Native Express ad. This allows us to set the Native Express ad's
        // width to match the full width of the RecyclerView.
        mainRecyclerView.post(new Runnable() {
            @Override
            public void run() {

                    final float scale = getActivity().getResources().getDisplayMetrics().density;
                    // Set the ad size and ad unit ID for each Native Express ad in the items list.

                        final NativeExpressAdView adView =
                                (NativeExpressAdView) mRecyclerViewItems.get(mRecyclerViewItems.size()-1);

                        final CardView cardView = (CardView) getActivity().findViewById(R.id.ad_card_view);
                        final int adWidth = cardView.getWidth() - cardView.getPaddingLeft()
                                - cardView.getPaddingRight();
                        AdSize adSize = new AdSize((int) (adWidth / scale), NATIVE_EXPRESS_AD_HEIGHT);
                        adView.setAdSize(adSize);
                        adView.setAdUnitId(AD_UNIT_ID);


                // Load the first Native Express ad in the items list.
                loadNativeExpressAd();

            }
        });
    }

    private void loadNativeExpressAd() {



        Object item = mRecyclerViewItems.get(mRecyclerViewItems.size()-1);
        if (!(item instanceof NativeExpressAdView)) {
            throw new ClassCastException("Expected item at index " + "nothing" + " to be a Native"
                    + " Express ad.");
        }

        final NativeExpressAdView adView = (NativeExpressAdView) item;

        // Set an AdListener on the NativeExpressAdView to wait for the previous Native Express ad
        // to finish loading before loading the next ad in the items list.
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                // The previous Native Express ad loaded successfully, call this method again to
                // load the next ad in the items list.
                loadNativeExpressAd();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // The previous Native Express ad failed to load. Call this method again to load
                // the next ad in the items list.
                Log.e("MainActivity", "The previous Native Express ad failed to load. Attempting to"
                        + " load the next Native Express ad in the items list.");
                loadNativeExpressAd();
            }
        });

        // Load the Native Express ad.
        adView.loadAd(new AdRequest.Builder().build());
    }
    private void addNativeAds()
    {

            NativeExpressAdView adView = new NativeExpressAdView(getActivity());
            adView.setAdSize(new AdSize(320,150));
            adView.setAdUnitId(AD_UNIT_ID);
            adView.loadAd(new AdRequest.Builder().build());
            AD= true;
            mRecyclerViewItems.add(adView);


    }
}
