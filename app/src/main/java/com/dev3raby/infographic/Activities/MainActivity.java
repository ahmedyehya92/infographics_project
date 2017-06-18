package com.dev3raby.infographic.Activities;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.animation.OvershootInterpolator;

import com.dev3raby.infographic.R;
import com.dev3raby.infographic.RecyclerViewAdapters.CategoryRecyclerViewAdapter;
import com.dev3raby.infographic.RecyclerViewAdapters.MainRecyclerViewAdapter;
import com.dev3raby.infographic.RecyclerViewModels.CategoryDataModel;
import com.dev3raby.infographic.RecyclerViewModels.MainDataModel;
import com.nshmura.snappysmoothscroller.SnapType;
import com.nshmura.snappysmoothscroller.SnappyLinearLayoutManager;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static RecyclerView mainRecyclerView;
    private static RecyclerView catgRecyclerView;
    public static final String[] SOURCE_NAME= {"Saud Aljaber", "أحمد العربي", "maloomaday.com", "Ali.Kindy", "arinfographic","arinfographic"};
    public static final String[] INFOGRAPHIC_NAME= {"مراحل عملية التسويق", "التخزين السحابي", "كيف يمكن تحفيز الناس", "8 مؤشرات مرتبطة بمنفذي العمليات الانتحارية", "احصائيات العاب الفيديو حول العالم", "القادة والمدراء التربويين"};
    public static final String[] INFOGRAPHIC_IMAGE = {"https://shamssona.files.wordpress.com/2015/08/img_20150814_162450.jpg", "http://everyleader.net/sites/default/files/Infographic-every-leader-confrontations.jpg.png", "http://www.alarabyanews.com/upload/photo/gallery/9/7/960.jpg", "https://www.graphic-academy.com/wp-content/uploads/2016/05/image-11.jpeg", "https://s-media-cache-ak0.pinimg.com/736x/71/63/64/71636447376825c06dba6426110a2d91.jpg","https://mir-s3-cdn-cf.behance.net/project_modules/disp/741fe716989131.562b415c27a27.jpg"};
    public static final String[] SOURSE_ICON = {"https://www.iconfinder.com/data/icons/logotypes/32/twitter-128.png", "https://vignette2.wikia.nocookie.net/drunken-peasants-podcast/images/e/e0/Tumblr_icon.png", "http://beadage.net/wp-content/themes/beadage/images/icon-url.png", "aboveblue.gr/wp-content/uploads/2017/03/instagram_yeni_logo_app_2-300x300.png", "http://market.designmodo.com/wp-content/uploads/2015/06/behance-logo.png","http://diylogodesigns.com/blog/wp-content/uploads/2016/05/Instagram-logo-png-icon.png"};

    public static final String[] CATEGORY_NAME= {"أحداث", "رياضة", "صحة", "نمط حياة"};
    public static final String[] CATEGORY_IMAGE = {"https://pbs.twimg.com/profile_images/694449140269518848/57ZmXva0.jpg", "http://richardhusovsky.com/wp-content/uploads/2015/06/ranni-beh.jpg", "http://www.fitnesstrend.com/sites/default/files/Stetoscopio%20WEB_1.jpg", "https://blog.holidaylettings.co.uk/wp-content/uploads/2015/04/lifestyle.jpg"};

    CategoryRecyclerViewAdapter catgAdapter;


    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_test);
       initCollapsingToolbar();
        initToolbar();
        initRecyclerViews();
        populatRecyclerView();
    }

    private void initRecyclerViews() {
        // for infographics recyclerView
        mainRecyclerView = (RecyclerView)
                findViewById(R.id.main_recycler_view);
        mainRecyclerView.setHasFixedSize(true);

        //Set RecyclerView type according to intent value
        StaggeredGridLayoutManager mainLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
      //  mainLayoutManager.setAutoMeasureEnabled(true);
          //  mainRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));// Here 2 is no. of columns to be displayed

        mainRecyclerView
                .setLayoutManager(mainLayoutManager);
        // for categories recyclerView
       catgRecyclerView = (RecyclerView) findViewById(R.id.catg_recycler_view);
        catgRecyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this,LinearLayoutManager.HORIZONTAL,false);
        SnappyLinearLayoutManager lay2outmanager= new SnappyLinearLayoutManager(MainActivity.this,SnappyLinearLayoutManager.HORIZONTAL,false);
        lay2outmanager.setSnapType(SnapType.CENTER);
        lay2outmanager.setSnapPadding(getResources().getDimensionPixelSize(R.dimen.demo_snap_padding));
        lay2outmanager.setSnapInterpolator(new OvershootInterpolator());

        // layoutManager.setAutoMeasureEnabled(true);
      //  catgRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
        catgRecyclerView.setLayoutManager(lay2outmanager);




    }

    private void populatRecyclerView() {
        ArrayList<MainDataModel> arrayList = new ArrayList<>();
        for (int i = 0; i < SOURCE_NAME.length; i++) {
            arrayList.add(new MainDataModel(INFOGRAPHIC_NAME[i],SOURCE_NAME[i], SOURSE_ICON[i], INFOGRAPHIC_IMAGE[i]));
        }
        MainRecyclerViewAdapter adapter = new MainRecyclerViewAdapter(MainActivity.this, arrayList);
        mainRecyclerView.setAdapter(adapter);// set adapter on recyclerview
        adapter.notifyDataSetChanged();

        // Notify the adapter


        // for category recyclerView
        ArrayList<CategoryDataModel> catgArrayList = new ArrayList<>();
        for (int i = 0; i < CATEGORY_NAME.length; i++) {
            catgArrayList.add(new CategoryDataModel(CATEGORY_NAME[i],CATEGORY_IMAGE[i]));
        }
        catgAdapter = new CategoryRecyclerViewAdapter(MainActivity.this, catgArrayList);
        catgRecyclerView.setAdapter(catgAdapter);// set adapter on recyclerview
        catgAdapter.notifyDataSetChanged();// Notify the adapter

    }

    private void initToolbar()
    {
        toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
    }

    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle("");
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }


}
