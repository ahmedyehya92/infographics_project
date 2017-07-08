package com.dev3raby.infographic.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.dev3raby.infographic.App.AppConfig;
import com.dev3raby.infographic.App.AppController;
import com.dev3raby.infographic.Helper.SQLiteHandler;
import com.dev3raby.infographic.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import uk.co.senab.photoview.PhotoViewAttacher;

public class InfographicActivity extends AppCompatActivity {

    Toolbar toolbar;
    RelativeLayout bottomBar;
    ImageView infographicView;
    PhotoViewAttacher mAttacher;
    boolean isDetailVisible = true;
    private boolean liked = false;
    private boolean bookmarked = false;
    private static Bitmap bitmapShare;
    private SQLiteHandler db;


    String id;
    final String idKey = "idKey";

    private  String apiKey;
    private  String user_id;

    private String link;
    Integer repeateConnection=0;
    private Button likeButton, bookmarkButton, backButton, visitButton, shareButton;
    private TextView likeText, seeText;
    private Animation bottomBarAnimShow, bottomBarAnimHide, toolbarAnimationShow, toolbarAnimationHide;
    View mDecorView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infographic);
        Intent idIntent = getIntent();
        id = idIntent.getStringExtra(idKey);
        db = new SQLiteHandler(InfographicActivity.this);
        HashMap<String, String> user = db.getUserDetails();


        user_id = user.get("user_id");
        apiKey = user.get("uid");

        initViews();
        toolbar.setVisibility(View.GONE);
        bottomBar.setVisibility(View.GONE);
        getInfographic(user_id,id,apiKey);

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startShare();
            }
        });

        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likeAction(user_id,id,apiKey);
            }
        });

        bookmarkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookmarkAction(user_id,id,apiKey);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        visitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(link));
                startActivity(i);
            }
        });
    }


    public void initViews()
    {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDecorView = getWindow().getDecorView();
        bottomBar = (RelativeLayout)findViewById(R.id.bottom_bar);

        infographicView = (ImageView)findViewById(R.id.image_infographic);

        likeText = (TextView)findViewById(R.id.tx_like_counter);
        seeText = (TextView) findViewById(R.id.tx_see_counter);

        visitButton = (Button) findViewById(R.id.btn_visit);

        backButton = (Button) findViewById(R.id.btn_back);
        shareButton = (Button) findViewById(R.id.btn_share);
        likeButton = (Button) findViewById(R.id.btn_favorite);
        likeButton.setSoundEffectsEnabled(false);

        bookmarkButton = (Button) findViewById(R.id.btn_bookmark);
        bookmarkButton.setSoundEffectsEnabled(false);

        bottomBarAnimShow = AnimationUtils.loadAnimation( this, R.anim.bottom_bar_show);
        bottomBarAnimHide = AnimationUtils.loadAnimation( this, R.anim.bottom_bar_hide);

        toolbarAnimationHide = AnimationUtils.loadAnimation(this, R.anim.toolbar_hide);
        toolbarAnimationShow = AnimationUtils.loadAnimation(this, R.anim.toolbar_show);


        mAttacher = new PhotoViewAttacher(infographicView);
        mAttacher.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float v, float v1) {
                isDetailVisible = !isDetailVisible;
                if( isDetailVisible ) {
                    toolbar.setVisibility(View.VISIBLE);
                    toolbar.startAnimation(toolbarAnimationShow);

                    bottomBar.setVisibility(View.VISIBLE);
                    bottomBar.startAnimation(bottomBarAnimShow);


                } else {
                    toolbar.setVisibility(View.GONE);
                    toolbar.startAnimation(toolbarAnimationHide);

                    bottomBar.setVisibility(View.GONE);
                    bottomBar.startAnimation(bottomBarAnimHide);


                }

            }
        });
    }
    public boolean isLiked ()
    {
        if (!liked)
        {
            likeButton.setBackgroundResource(R.drawable.ic_favorite_border);
            return false;
        }
        else {
            likeButton.setBackgroundResource(R.drawable.ic_favorite);
            return true;
        }
    }

    public void likeButtonClick() {
        if (!liked)
        {
            likeButton.setBackgroundResource(R.drawable.ic_favorite);
            liked = true;
        }
        else {

            likeButton.setBackgroundResource(R.drawable.ic_favorite_border);
            liked = false;
        }
    }

    public boolean isBookmarked ()
    {
        if (!bookmarked)
        {
            bookmarkButton.setBackgroundResource(R.drawable.ic_bookmark_border);
            return false;
        }
        else {
            bookmarkButton.setBackgroundResource(R.drawable.ic_bookmark);
            return true;
        }
    }

    public void bookmarkButtonClick() {
        if (!bookmarked)
        {
            bookmarkButton.setBackgroundResource(R.drawable.ic_bookmark);
            bookmarked = true;
        }
        else {

            bookmarkButton.setBackgroundResource(R.drawable.ic_bookmark_border);
            bookmarked = false;
        }
    }

    private void getInfographic(final String user_id,final String infographic_id, final String api_key) {
        // Tag used to cancel the request
        String tag_string_req = "req_getInfographic";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_GET_INFOGRAPHIC, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                repeateConnection=0;
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");


                    // Check for error node in json
                    if (!error) {

                        Boolean isLiked = jObj.getBoolean("isLiked");

                        if (isLiked)
                        {
                            liked = true;
                        }
                        else
                        {
                            liked = false;
                        }

                        Boolean isBookmarked = jObj.getBoolean("isBookmarked");
                        if (isBookmarked)
                        {
                            bookmarked = true;
                        }
                        else
                        {
                            bookmarked = false;
                        }

                        isLiked();
                        isBookmarked();

                        toolbar.setVisibility(View.VISIBLE);
                        toolbar.startAnimation(toolbarAnimationShow);

                        bottomBar.setVisibility(View.VISIBLE);
                        bottomBar.startAnimation(bottomBarAnimShow);

                        JSONObject infographic = jObj.getJSONObject("infographic");

                 /*       try {
                            Bitmap theBitmap = Glide.
                                    with(InfographicActivity.this).
                                    load(infographic.getString("image_url")).
                                    asBitmap().
                                    into(100, 100). // Width and height
                                    get();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } */

                        Glide
                                .with( InfographicActivity.this ) // could be an issue!
                                .load( infographic.getString("image_url" ))
                                .asBitmap()
                                .into( target );




               /*         Picasso.with(InfographicActivity.this)
                                .load(infographic.getString("image_url"))
                                .into(infographicView, new Callback.EmptyCallback() {
                                    @Override public void onSuccess() {
                                        mAttacher.update();

                                        toolbar.setVisibility(View.VISIBLE);
                                        toolbar.startAnimation(toolbarAnimationShow);

                                        bottomBar.setVisibility(View.VISIBLE);
                                        bottomBar.startAnimation(bottomBarAnimShow);

                                    }
                                });
*/



                        link = infographic.getString("source_url");

                        likeText.setText(infographic.getString("like_counter"));






                    } else {
                        // Error in login. Get the error message
                        //final String errorMsg = jObj.getString("message");

                        //showAlertDialog(errorMsg);
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(InfographicActivity.this, "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();


                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (repeateConnection == 0) {
                    toolbar.setVisibility(View.VISIBLE);
                    toolbar.startAnimation(toolbarAnimationShow);

                    bottomBar.setVisibility(View.VISIBLE);
                    bottomBar.startAnimation(bottomBarAnimShow);
                }
                if (repeateConnection <= 2)
                {
                    getInfographic(user_id,infographic_id, api_key);
                    Toast.makeText(InfographicActivity.this,"error",Toast.LENGTH_SHORT).show();
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
                params.put("user_id", user_id);
                params.put("infographic_id",id);


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

    private void likeAction(final String user_id,final String infographic_id, final String api_key) {
        // Tag used to cancel the request
        likeButton.setEnabled(false);
        String tag_string_req = "req_like_action";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_LIKE_ACTOIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                repeateConnection=0;
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");


                    // Check for error node in json
                    if (!error) {

                        Boolean isLiked = jObj.getBoolean("isLiked");


                        if (isLiked)
                        {
                            likeButton.setBackgroundResource(R.drawable.ic_favorite_border);
                            liked = false;
                        }
                        else
                        {
                            likeButton.setBackgroundResource(R.drawable.ic_favorite);
                            liked = true;
                        }


                      likeButton.setEnabled(true);



                    } else {
                        // Error in login. Get the error message
                        //final String errorMsg = jObj.getString("message");

                        //showAlertDialog(errorMsg);
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(InfographicActivity.this, "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();


                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {


                if (repeateConnection <= 3)
                {
                    likeAction(user_id,infographic_id, api_key);
                    Toast.makeText(InfographicActivity.this,"error",Toast.LENGTH_SHORT).show();
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
                params.put("user_id", user_id);
                params.put("infographic_id",id);


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

    private void bookmarkAction(final String user_id,final String infographic_id, final String api_key) {
        // Tag used to cancel the request
        bookmarkButton.setEnabled(false);
        String tag_string_req = "req_bookmark_action";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_BOOKMARK_ACTION, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                repeateConnection=0;
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");


                    // Check for error node in json
                    if (!error) {

                        Boolean isBookmarked = jObj.getBoolean("isBookmarked");


                        if (isBookmarked)
                        {
                            bookmarkButton.setBackgroundResource(R.drawable.ic_bookmark_border);
                            bookmarked = false;
                        }
                        else
                        {
                            bookmarkButton.setBackgroundResource(R.drawable.ic_bookmark);
                            bookmarked = true;
                        }


                        bookmarkButton.setEnabled(true);



                    } else {
                        // Error in login. Get the error message
                        //final String errorMsg = jObj.getString("message");

                        //showAlertDialog(errorMsg);
                        bookmarkButton.setEnabled(true);
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(InfographicActivity.this, "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();


                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {


                if (repeateConnection <= 3)
                {
                    bookmarkAction(user_id,infographic_id, api_key);
                    Toast.makeText(InfographicActivity.this,"error",Toast.LENGTH_SHORT).show();
                    repeateConnection++;
                }
                else
                {

                    String msg = "خطأ في الإتصال برجاء المحاولة لاحقا";
                    //showAlertDialog(msg);


                }
                bookmarkButton.setEnabled(true);




            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", user_id);
                params.put("infographic_id",id);


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
    private SimpleTarget target = new SimpleTarget<Bitmap>() {
        @Override
        public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
            // do something with the bitmap
            // for demonstration purposes, let's just set it to an ImageView


            infographicView.setImageBitmap( bitmap );

            mAttacher.update();

            bitmapShare = bitmap;

        }
    };

public void startShare()
{
    Intent shareIntent = new Intent(Intent.ACTION_SEND);
    shareIntent.setType("image/jpeg");
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    bitmapShare.compress(Bitmap.CompressFormat.JPEG,100, byteArrayOutputStream);
    File file = new File(Environment.getExternalStorageDirectory()+ File.separator+ "image.jpg");

    try {
        file.createNewFile();
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(byteArrayOutputStream.toByteArray());
    } catch (IOException e) {
        e.printStackTrace();
    }

    shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://sdcard/image.jpg"));
    startActivity(Intent.createChooser(shareIntent,"مشاركة الإنفوجرافيك"));
}


}
