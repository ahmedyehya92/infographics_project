package com.dev3raby.infographic.Helper;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.dev3raby.infographic.Activities.HomeActivity;
import com.dev3raby.infographic.App.AppConfig;
import com.dev3raby.infographic.App.AppController;
import com.dev3raby.infographic.R;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ahmed Yehya on 04/07/2017.
 */

public class FCMRegistrationService extends IntentService {

    SharedPreferences sharedpreferences;
    private  String apiKey;
    private  String user_id;
    private String token;

    private SQLiteHandler db;



    public FCMRegistrationService() {
        super("FCM");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        apiKey = "61dfee3914dbb0d77199cbc5d9d483a3";
        token = FirebaseInstanceId.getInstance().getToken();
        sharedpreferences = PreferenceManager.getDefaultSharedPreferences(this);

        if (intent.getExtras() != null) {
            boolean refreshed = intent.getExtras().getBoolean("refreshed");
            if (refreshed) sharedpreferences.edit().putBoolean("token_sent", false).apply();
        }
            if (!sharedpreferences.getBoolean("token_sent", false)) {
                addToken(token, apiKey);
            }

    }

    private void addToken(final String token, final String apiKey) {
        // Tag used to cancel the request

        String tag_string_req = "req_add_token";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_ADD_TOKEN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {


                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");


                    // Check for error node in json
                    if (!error) {


                        sharedpreferences.edit().putBoolean("token_sent", true).apply();
                        Toast.makeText(getApplicationContext(),"token is sent",Toast.LENGTH_SHORT).show();
                        stopSelf();


                    } else {
                        sharedpreferences.edit().putBoolean("token_sent", false).apply();
                        stopSelf();

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


                sharedpreferences.edit().putBoolean("token_sent", false).apply();
                stopSelf();


            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();

                params.put("token",token);


                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();

                headers.put("Authorization", apiKey);
                return headers;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
}
