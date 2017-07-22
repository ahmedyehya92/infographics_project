package com.dev3raby.infographic.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.dev3raby.infographic.Adapters.CategoryAdapter;
import com.dev3raby.infographic.App.AppConfig;
import com.dev3raby.infographic.App.AppController;
import com.dev3raby.infographic.DataModels.Category;
import com.dev3raby.infographic.Helper.SQLiteHandler;
import com.dev3raby.infographic.Helper.SessionManager;
import com.dev3raby.infographic.R;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FollowinActivity extends AppCompatActivity {

    public static ListView mListView;
    public static ArrayList<Category> categoriesList;
    private SQLiteHandler db;
    private  String api_key;
    private  String user_id;
    private String email;
    private String password;
    final String idKey = "idKey";
    final String apiKey = "apiKey";
    final String email_key = "emailKey";
    final String password_key = "passwordKey";
    CategoryAdapter adapter;
    private static JSONArray jsonArray;
    private static JSONObject category;
    Integer repeateConnection=0;
    public static ArrayList<Integer> arrayListFollowed = new ArrayList<>();
    RelativeLayout relativeLayout;
    Snackbar snackbar;
    private SessionManager session;
    private ProgressDialog pDialog;
    private String token;
    SharedPreferences sharedpreferences;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followin);
        token = FirebaseInstanceId.getInstance().getToken();
        sharedpreferences = PreferenceManager.getDefaultSharedPreferences(this);



        categoriesList = new ArrayList<Category>();
        db = new SQLiteHandler(FollowinActivity.this);
        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = db.getUserDetails();


     //   user_id = user.get("user_id");
    //    api_key = user.get("uid");

        Intent idIntent = getIntent();
        user_id = idIntent.getStringExtra(idKey);
        api_key = idIntent.getStringExtra(apiKey);
        email = idIntent.getStringExtra(email_key);
        password = idIntent.getStringExtra(password_key);


        relativeLayout = (RelativeLayout) findViewById(R.id.relativelayout);
        pDialog = new ProgressDialog(this,ProgressDialog.THEME_HOLO_DARK);
        pDialog.setCancelable(false);

        FloatingActionButton FAB = (FloatingActionButton) findViewById(R.id.fab);
        FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(arrayListFollowed.isEmpty())
                {
                    snackbar = Snackbar
                            .make(relativeLayout, "قم بمتابعة بعض الأقسام أولا", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
                else
                {
                    checkLogin(email,password,token);
                }

            }
        });
        mListView = (ListView) findViewById(R.id.list);
        adapter = new CategoryAdapter(getApplicationContext(),categoriesList, api_key, user_id);
        mListView.setAdapter(adapter);
        categoriesList.clear();
        getCategories(api_key);





    }

    @Override
    protected void onStop() {
        arrayListFollowed.clear();
        super.onStop();
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

                            categoriesList.add(new Category(category.getInt("id"), category.getString("category"), category.getInt("follow_counter")));


                            FollowinActivity.this.runOnUiThread(new Runnable() {

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
                    getCategories(api_key);
                    snackbar = Snackbar
                            .make(relativeLayout, "تحقق من اتصالك بالشبكة", Snackbar.LENGTH_LONG);
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

    private void checkLogin(final String email, final String password, final String token) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        pDialog.setMessage("تسجيل الدخول ...");

        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                hideDialog();
                repeateConnection=0;
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        // user successfully logged in
                        // Create login session
                        session.setLogin(true);

                        // Now store the user in SQLite
                        String uid = jObj.getString("apiKey");

                        JSONObject user = jObj.getJSONObject("user");
                        String user_id = user.getString("id");
                        String name = user.getString("name");
                        String email = user.getString("email");
                        String created_at = user
                                .getString("created_at");

                        // Inserting row in users table
                        db.addUser(user_id,name, email, uid, created_at);
                        sharedpreferences.edit().putBoolean("token_sent", true).apply();
                        // Launch main activity
                        Intent intent = new Intent(FollowinActivity.this,
                                HomeActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        sharedpreferences.edit().putBoolean("token_sent", false).apply();
                        // Error in login. Get the error message
                        final String errorMsg = jObj.getString("message");
                        snackbar = Snackbar
                                .make(relativeLayout, errorMsg, Snackbar.LENGTH_LONG);
                        snackbar.show();
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
                    checkLogin(email, password, token);
                    repeateConnection++;
                }
                else
                {
                    hideDialog();
                    snackbar = Snackbar
                            .make(relativeLayout, "تحقق من اتصالك بالشبكة", Snackbar.LENGTH_LONG);
                    snackbar.show();


                }



            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", password);
                params.put("token", token);
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
