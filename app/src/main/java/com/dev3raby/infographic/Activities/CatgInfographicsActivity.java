package com.dev3raby.infographic.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Button;

import com.dev3raby.infographic.R;
import com.dev3raby.infographic.Adapters.CatgInfoRecyclerAdapter;
import com.dev3raby.infographic.DataModels.CatgInfoDataModel;

import java.util.ArrayList;

public class CatgInfographicsActivity extends AppCompatActivity {

    private static RecyclerView catgInfoRecyclerView;
    CatgInfoRecyclerAdapter adapter;
    private static boolean followed = false;
    private Button followButton;

    public static final String[] SOURCE_NAME= {"Omar Yehya", "أحمد العربي", "maloomaday.com", "Ali.Kindy", "arinfographic","arinfographic"};
    public static final String[] INFOGRAPHIC_NAME= {"مراحل عملية التسويق", "التخزين السحابي", "كيف يمكن تحفيز الناس", "8 مؤشرات مرتبطة بمنفذي العمليات الانتحارية", "احصائيات العاب الفيديو حول العالم", "القادة والمدراء التربويين"};
    public static final String[] INFOGRAPHIC_IMAGE = {"https://shamssona.files.wordpress.com/2015/08/img_20150814_162450.jpg", "http://everyleader.net/sites/default/files/Infographic-every-leader-confrontations.jpg.png", "http://www.alarabyanews.com/upload/photo/gallery/9/7/960.jpg", "https://www.graphic-academy.com/wp-content/uploads/2016/05/image-11.jpeg", "https://s-media-cache-ak0.pinimg.com/736x/71/63/64/71636447376825c06dba6426110a2d91.jpg","https://mir-s3-cdn-cf.behance.net/project_modules/disp/741fe716989131.562b415c27a27.jpg"};
    public static final String[] SOURSE_ICON = {"https://www.iconfinder.com/data/icons/logotypes/32/twitter-128.png", "https://vignette2.wikia.nocookie.net/drunken-peasants-podcast/images/e/e0/Tumblr_icon.png", "http://beadage.net/wp-content/themes/beadage/images/icon-url.png", "aboveblue.gr/wp-content/uploads/2017/03/instagram_yeni_logo_app_2-300x300.png", "http://market.designmodo.com/wp-content/uploads/2015/06/behance-logo.png","http://diylogodesigns.com/blog/wp-content/uploads/2016/05/Instagram-logo-png-icon.png"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catg_infographics);
        initView();
        isFollowed();
        followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               followButtonClick();
            }
        });
        initRecyclerViews();
        populatRecyclerView();


    }

    public void initView()
    {
        followButton = (Button) findViewById(R.id.btn_follow);
    }

    public boolean isFollowed ()
    {
        if (!followed)
        {
            followButton.setBackgroundResource(R.drawable.unfollowed_btn_shape);
            followButton.setTextColor(getResources().getColor(R.color.actionButtons));
            followButton.setText(getResources().getString(R.string.follow_btn));
            return false;
        }
        else {
            followButton.setBackgroundResource(R.drawable.followed_btn_shape);
            followButton.setTextColor(getResources().getColor(R.color.white));
            followButton.setText(getResources().getString(R.string.followed_btn));
            return true;
        }
    }

    public void followButtonClick() {
        if (!followed)
        {
            followButton.setBackgroundResource(R.drawable.followed_btn_shape);
            followButton.setTextColor(getResources().getColor(R.color.white));
            followButton.setText(getResources().getString(R.string.followed_btn));
            followed = true;
        }
        else {

            followButton.setBackgroundResource(R.drawable.unfollowed_btn_shape);
            followButton.setTextColor(getResources().getColor(R.color.actionButtons));
            followButton.setText(getResources().getString(R.string.follow_btn));
            followed = false;
        }
    }

    private void initRecyclerViews() {
        // for infographics recyclerView
        catgInfoRecyclerView = (RecyclerView)
                findViewById(R.id.catginfo_recycler_view);
        catgInfoRecyclerView.setHasFixedSize(true);

        //Set RecyclerView type according to intent value
        StaggeredGridLayoutManager mainLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        //  mainLayoutManager.setAutoMeasureEnabled(true);
        //  mainRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));// Here 2 is no. of columns to be displayed

        catgInfoRecyclerView
                .setLayoutManager(mainLayoutManager);
    }

    private void populatRecyclerView() {
        ArrayList<CatgInfoDataModel> arrayList = new ArrayList<>();
        for (int i = 0; i < SOURCE_NAME.length; i++) {
            arrayList.add(new CatgInfoDataModel(INFOGRAPHIC_NAME[i], SOURCE_NAME[i], SOURSE_ICON[i], INFOGRAPHIC_IMAGE[i]));
        }
        adapter = new CatgInfoRecyclerAdapter(CatgInfographicsActivity.this, arrayList);
        catgInfoRecyclerView.setAdapter(adapter);// set adapter on recyclerview
        adapter.notifyDataSetChanged();

    }
}
