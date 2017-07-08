package com.dev3raby.infographic.Activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
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
import com.dev3raby.infographic.R;

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
    final String idKey = "idKey";
    final String apiKey = "apiKey";
    CategoryAdapter adapter;
    private static JSONArray jsonArray;
    private static JSONObject category;
    Integer repeateConnection=0;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followin);

        FloatingActionButton FAB = (FloatingActionButton) findViewById(R.id.fab);
        FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        FollowinActivity.this,
                        LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });


        categoriesList = new ArrayList<Category>();
        db = new SQLiteHandler(FollowinActivity.this);
        HashMap<String, String> user = db.getUserDetails();


     //   user_id = user.get("user_id");
    //    api_key = user.get("uid");

        Intent idIntent = getIntent();
        user_id = idIntent.getStringExtra(idKey);
        api_key = idIntent.getStringExtra(apiKey);

        mListView = (ListView) findViewById(R.id.list);
        adapter = new CategoryAdapter(getApplicationContext(),categoriesList, api_key, user_id);
        mListView.setAdapter(adapter);
        categoriesList.clear();
        getCategories(api_key);





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
                    Toast.makeText(FollowinActivity.this, "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();


                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {


                if (repeateConnection <= 3)
                {
                    getCategories(api_key);
                    Toast.makeText(FollowinActivity.this,"error",Toast.LENGTH_SHORT).show();
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
