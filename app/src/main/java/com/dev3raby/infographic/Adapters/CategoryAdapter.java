package com.dev3raby.infographic.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.dev3raby.infographic.Activities.FollowinActivity;
import com.dev3raby.infographic.App.AppConfig;
import com.dev3raby.infographic.App.AppController;
import com.dev3raby.infographic.DataModels.Category;
import com.dev3raby.infographic.Helper.SQLiteHandler;
import com.dev3raby.infographic.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ahmed Yehya on 02/07/2017.
 */

public class CategoryAdapter extends ArrayAdapter<Category> {
    Context context;
    String apiKey;
    String userId;
    ViewHolder viewHolder;
    private static boolean followed = false;


    public CategoryAdapter(Context context, ArrayList<Category> words, String api_key, String user_id) {

        super(context,0, words);
        this.context =context;
        this.apiKey = api_key;
        this.userId = user_id;

    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.category_list_item, parent, false);
        }

        final Category currentCategory = getItem(position);
        viewHolder = (ViewHolder)listItemView.getTag();

        if (viewHolder==null) {
            viewHolder = new ViewHolder();
            viewHolder.catNametx = (TextView)listItemView.findViewById(R.id.tx_category);
            viewHolder.numFollowers = (TextView)listItemView.findViewById(R.id.tx_num_Followers);
            viewHolder.followButton = (Button)listItemView.findViewById(R.id.btn_follow);
            listItemView.setTag(viewHolder);
        }
        else {
            viewHolder =(ViewHolder)listItemView.getTag();
        }

        viewHolder.catNametx.setText(currentCategory.getCategoryName());

        viewHolder.numFollowers.setText(currentCategory.getNumFollowers()+"متابع");


        viewHolder.followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           /*     if (!followed)
                {
                    viewHolder.followButton.setBackgroundResource(R.drawable.followed_btn_shape);
                    viewHolder.followButton.setTextColor(context.getResources().getColor(R.color.white));
                    viewHolder.followButton.setText(context.getResources().getString(R.string.followed_btn));
                    followed = true;
                }
                else {

                    viewHolder.followButton.setBackgroundResource(R.drawable.unfollowed_btn_shape);
                    viewHolder.followButton.setTextColor(context.getResources().getColor(R.color.actionButtons));
                    viewHolder.followButton.setText(context.getResources().getString(R.string.follow_btn));
                    followed = false;
                } */
                bookmarkAction(userId,currentCategory.getCatId(),apiKey, new VolleyCallback(){
                    @Override
                    public void onSuccess(Boolean isFollowed){
                        if (isFollowed)
                        {

                            Integer pos = position;
                            Toast.makeText(context, "تمت متابعة القسم " + currentCategory.getCategoryName(),Toast.LENGTH_SHORT).show();
                           // FollowinActivity.categoriesList.remove(position);

                          //  notifyDataSetChanged();
                        }
                        else
                        {
                            Toast.makeText(context, "انت بالفعل تتابع هذا القسم",Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        });



        return listItemView;


    }



    class ViewHolder {
        TextView catNametx;
        TextView numFollowers;
        Button followButton;


    }

    private void bookmarkAction(final String user_id,final String category_id, final String api_key,  final VolleyCallback callback) {
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
                        callback.onSuccess(isFollowed);



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




                    String msg = "خطأ في الإتصال برجاء المحاولة لاحقا";
                    //showAlertDialog(msg);



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

    public interface VolleyCallback{
        void onSuccess(Boolean result);
    }

}
