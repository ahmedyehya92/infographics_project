package com.dev3raby.infographic.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.dev3raby.infographic.App.AppConfig;
import com.dev3raby.infographic.App.AppController;
import com.dev3raby.infographic.Helper.SQLiteHandler;
import com.dev3raby.infographic.Helper.SessionManager;
import com.dev3raby.infographic.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements
        View.OnClickListener,
        GoogleApiClient.OnConnectionFailedListener {

    private static final int RC_SIGN_IN = 007;
    private GoogleApiClient mGoogleApiClient;
    private RelativeLayout btnSignIn;

    private RelativeLayout loginButton;
    private CallbackManager callbackManager;

    final String facebookSignType = "facebook";
    final String googleSignType = "google";
    final String subFakeEmail = "@facebook.com";

    private static final String TAG = RegisterActivity.class.getSimpleName();
    private Button btnLogin;
    private Button btnLinkToRegister;
    private EditText inputEmail;
    private EditText inputPassword;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;
    private static Integer repeateConnection=0;
    Snackbar snackbar;
    private LinearLayout linearLayout;
    private String token;
    SharedPreferences sharedpreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
       // token = FirebaseInstanceId.getInstance().getToken();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        sharedpreferences = PreferenceManager.getDefaultSharedPreferences(this);

        callbackManager = CallbackManager.Factory.create();
        prepareFacebookLogin();

        loginButton = (RelativeLayout) findViewById(R.id.facelogin_layout);
        btnSignIn = (RelativeLayout) findViewById(R.id.googlelogin_layout);

        btnSignIn.setOnClickListener(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();



        disconnectFromFacebook();


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                LoginManager.getInstance().logInWithReadPermissions(
                        LoginActivity.this,
                        Arrays.asList("email"));


            }
        });


        linearLayout = (LinearLayout) findViewById(R.id
                .linearLayout);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLinkToRegister = (Button) findViewById(R.id.btnLinkToRegisterScreen);

        // Progress dialog
        pDialog = new ProgressDialog(this,ProgressDialog.THEME_HOLO_DARK);
        pDialog.setCancelable(false);

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());


        // Session manager
        session = new SessionManager(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }

        // Login button Click Event
        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                // Check for empty data in the form
                if (!email.isEmpty() && !password.isEmpty()) {
                    // login user
                    checkLogin(email, password);
                } else {
                    // Prompt user to enter credentials

                    String msg = "الرجاء ادخال البيانات بشكل صحيح";
                    //showAlertDialog(msg);

                }
            }

        });

        // Link to Register Screen
        btnLinkToRegister.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void checkLogin(final String email, final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        pDialog.setMessage(getString(R.string.login_pdialog));

        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
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
                        Intent intent = new Intent(LoginActivity.this,
                                HomeActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        sharedpreferences.edit().putBoolean("token_sent", false).apply();
                        // Error in login. Get the error message
                        final String errorMsg = jObj.getString("message");
                        snackbar = Snackbar
                                .make(linearLayout, errorMsg, Snackbar.LENGTH_LONG);
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
                Log.e(TAG, "Login Error: " + error.getMessage());

                if (repeateConnection <= 3)
                {

                    checkLogin(email, password);
                    snackbar = Snackbar
                            .make(linearLayout, "تحقق من اتصالك بالشبكة", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    repeateConnection++;
                }
                else
                {
                    hideDialog();
                    String msg = "خطأ في الإتصال برجاء المحاولة لاحقا";
                    //showAlertDialog(msg);


                }



            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", password);
                params.put("token", FirebaseInstanceId.getInstance().getToken());
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

    private void prepareFacebookLogin() {
        final Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,gender,birthday");



        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {


                final AccessToken accessToken = loginResult.getAccessToken();

                GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject user, GraphResponse graphResponse) {






                        String name = user.optString("name");
                        String email = user.optString("email");
                        String id = user.optString("id");
                        String subId = id.substring(0,3);
                        String fakeEmai = subId+subFakeEmail;

                        if (name == null || name.equals(""))
                        {
                            Toast.makeText(LoginActivity.this,getString(R.string.faild_facebook_reg),Toast.LENGTH_SHORT);
                        }
                        else if (email == null || email.equals(""))
                        {
                            anotherWayRegisterUser(name,fakeEmai,facebookSignType,facebookSignType);
                        }

                        else
                        {

                            anotherWayRegisterUser(name,email,facebookSignType,facebookSignType);

                        }








                    }

                });



                request.setParameters(parameters);
                request.executeAsync();



            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), "cancelled", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onError(FacebookException e) {
                Toast.makeText(LoginActivity.this,getString(R.string.faild_facebook_reg),Toast.LENGTH_SHORT);

            }
        });

    }

    public void disconnectFromFacebook() {

        if (AccessToken.getCurrentAccessToken() == null) {
            return; // already logged out
        }

        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                .Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {

                LoginManager.getInstance().logOut();


            }
        }).executeAsync();
    }


    // google sign ----------------------------------------------------------------------------------

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            Log.e(TAG, "display name: " + acct.getDisplayName());

            String personName = acct.getDisplayName();
            String email = acct.getEmail();

            Log.e(TAG, "Name: " + personName + ", email: " + email);



            anotherWayRegisterUser(personName,email,googleSignType,googleSignType);


        } else {
            // Signed out, show unauthenticated UI.

        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.googlelogin_layout:
                signIn();
                break;

        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(LoginActivity.this,getString(R.string.faild_google_reg),Toast.LENGTH_SHORT);

    }

    private void anotherWayRegisterUser(final String name, final String email, final String password, final String signType) {
        // Tag used to cancel the request
        String tag_string_req = "another_req_register";



        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_REGISTER_ANOTHER_WAY, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());

                repeateConnection=0;

                try {
                    JSONObject jObj = new JSONObject(response);

                    boolean error = jObj.getBoolean("error");
                    String status = jObj.getString("status");

                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite


                        String uid = jObj.getString("apiKey");

                        JSONObject user = jObj.getJSONObject("user");
                        String user_id = user.getString("id");
                        String name = user.getString("name");
                        String email = user.getString("email");
                        String created_at = user
                                .getString("created_at");

                        // Inserting row in users table
                        db.addUser(user_id, name, email, uid, created_at);

                        // Launch login activity
                        final String id_key = "idKey";
                        final String api_key = "apiKey";
                        final String email_key = "emailKey";
                        final String password_key = "passwordKey";





                        Intent intent = new Intent(
                                LoginActivity.this,
                                FollowinActivity.class);
                        intent.putExtra(id_key,user_id);
                        intent.putExtra(api_key,uid);
                        intent.putExtra(email_key,email);
                        intent.putExtra(password_key,password);
                        startActivity(intent);
                        finish();
                    } else if (error == true && status.equals("existed")) {

                        checkLogin(email,password);

                        // Error occurred in registration. Get the error
                        // message

                        //  showAlertDialog(errorMsg);
                    }

                    else
                    {
                        String errorMsg = jObj.getString("message");
                        snackbar = Snackbar
                                .make(linearLayout, errorMsg, Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                if (repeateConnection <= 3)
                {

                    anotherWayRegisterUser(name, email, password,signType);
                    snackbar = Snackbar
                            .make(linearLayout, "تحقق من اتصالك بالشبكة", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    repeateConnection++;
                }
                else
                {



                }

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", name);
                params.put("email", email);
                params.put("password", password);
                params.put("sign_type",signType);
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

}
