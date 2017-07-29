package com.dev3raby.infographic.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.dev3raby.infographic.R;
import com.dev3raby.infographic.Adapters.CategoryRecyclerViewAdapter;
import com.dev3raby.infographic.DataModels.CategoryDataModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ahmed Yehya on 11/06/2017.
 */

public class ExploreFragment extends Fragment {
    public static final String ARG_EXAMPLE = "this is a constant";
    private String example_data;
    private static JSONArray jsonArray;
    private SQLiteHandler db;
    private static String apiKey;
    private static JSONObject category;
    Integer repeateConnection=0;
    ArrayList<CategoryDataModel> arrayList = new ArrayList<>();

    private static RecyclerView catgRecyclerView;
    public static final String[] CATEGORY_NAME= {"أحداث", "رياضة", "صحة", "نمط حياة"};
    public static final String[] CATEGORY_IMAGE = {"https://pbs.twimg.com/profile_images/694449140269518848/57ZmXva0.jpg", "http://richardhusovsky.com/wp-content/uploads/2015/06/ranni-beh.jpg", "http://www.fitnesstrend.com/sites/default/files/Stetoscopio%20WEB_1.jpg", "https://blog.holidaylettings.co.uk/wp-content/uploads/2015/04/lifestyle.jpg"};
    CategoryRecyclerViewAdapter catgAdapter;

    RelativeLayout relativeLayout;
    Snackbar snackbar;

    public ExploreFragment() {

    }

    public static ExploreFragment newInstance (String example_args)
    {
        ExploreFragment homeFragment = new ExploreFragment();
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
        apiKey = user.get("uid");
        getCategories(apiKey);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_explore,container,false);
        catgRecyclerView = (RecyclerView)
                rootView.findViewById(R.id.catg_recycler_view);
        relativeLayout = (RelativeLayout) rootView.findViewById(R.id.relativelayout);
        return rootView;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        StaggeredGridLayoutManager catgLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        catgRecyclerView
                .setLayoutManager(catgLayoutManager);
        populatRecyclerView();
        super.onViewCreated(view, savedInstanceState);
    }

    private void populatRecyclerView() {
        // for category recyclerView

        catgAdapter = new CategoryRecyclerViewAdapter(getActivity(), arrayList);
        catgRecyclerView.setAdapter(catgAdapter);// set adapter on recyclerview
        catgAdapter.notifyDataSetChanged();// Notify the adapter
    }

    private void getCategories(final String api_key) {
        // Tag used to cancel the request
        String tag_string_req = "req_get_categories";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_GET_CATEGORIES, new Response.Listener<String>() {

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

                        jsonArray = jObj.getJSONArray("categories");




                            for (int i = 0; i < jsonArray.length(); i++) {

                                category = jsonArray.getJSONObject(i);

                                arrayList.add(new CategoryDataModel(category.getInt("id"), category.getString("category"), category.getString("icon")));


                                getActivity().runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {
                                        // your stuff to update the UI

                                        catgAdapter.notifyDataSetChanged();
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
                    getCategories(api_key);
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
                params.put("categories", "");


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
