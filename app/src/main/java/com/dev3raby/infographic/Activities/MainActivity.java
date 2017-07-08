package com.dev3raby.infographic.Activities;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.animation.OvershootInterpolator;

import com.dev3raby.infographic.R;
import com.dev3raby.infographic.Adapters.CategoryRecyclerViewAdapter;
import com.dev3raby.infographic.Adapters.MainRecyclerViewAdapter;
import com.dev3raby.infographic.DataModels.CategoryDataModel;
import com.dev3raby.infographic.DataModels.MainDataModel;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.NativeExpressAdView;
import com.nshmura.snappysmoothscroller.SnapType;
import com.nshmura.snappysmoothscroller.SnappyLinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static RecyclerView mainRecyclerView;
    private static RecyclerView catgRecyclerView;
    public static final String[] SOURCE_NAME= {"Saud Aljaber", "أحمد العربي", "maloomaday.com", "Ali.Kindy", "arinfographic","arinfographic"};
    public static final String[] INFOGRAPHIC_NAME= {"مراحل عملية التسويق", "التخزين السحابي", "كيف يمكن تحفيز الناس", "8 مؤشرات مرتبطة بمنفذي العمليات الانتحارية", "احصائيات العاب الفيديو حول العالم", "القادة والمدراء التربويين"};
    public static final String[] INFOGRAPHIC_IMAGE = {"https://shamssona.files.wordpress.com/2015/08/img_20150814_162450.jpg", "http://everyleader.net/sites/default/files/Infographic-every-leader-confrontations.jpg.png", "http://www.alarabyanews.com/upload/photo/gallery/9/7/960.jpg", "https://www.graphic-academy.com/wp-content/uploads/2016/05/image-11.jpeg", "https://s-media-cache-ak0.pinimg.com/736x/71/63/64/71636447376825c06dba6426110a2d91.jpg","https://mir-s3-cdn-cf.behance.net/project_modules/disp/741fe716989131.562b415c27a27.jpg"};
    public static final String[] SOURSE_ICON = {"https://www.iconfinder.com/data/icons/logotypes/32/twitter-128.png", "https://vignette2.wikia.nocookie.net/drunken-peasants-podcast/images/e/e0/Tumblr_icon.png", "http://beadage.net/wp-content/themes/beadage/images/icon-url.png", "aboveblue.gr/wp-content/uploads/2017/03/instagram_yeni_logo_app_2-300x300.png", "http://market.designmodo.com/wp-content/uploads/2015/06/behance-logo.png","http://diylogodesigns.com/blog/wp-content/uploads/2016/05/Instagram-logo-png-icon.png"};
    public static final int[] ID = {1,2,3,4,5,6};
    private static final int NATIVE_EXPRESS_AD_HEIGHT = 150;
    private static final String AD_UNIT_ID = "ca-app-pub-3940256099942544/1072772517";
    public static final int ITEMS_PER_AD = 4;

    public static final String[] CATEGORY_NAME= {"أحداث", "رياضة", "صحة", "نمط حياة"};
    public static final String[] CATEGORY_IMAGE = {"https://pbs.twimg.com/profile_images/694449140269518848/57ZmXva0.jpg", "http://richardhusovsky.com/wp-content/uploads/2015/06/ranni-beh.jpg", "http://www.fitnesstrend.com/sites/default/files/Stetoscopio%20WEB_1.jpg", "https://blog.holidaylettings.co.uk/wp-content/uploads/2015/04/lifestyle.jpg"};

    CategoryRecyclerViewAdapter catgAdapter;

    private List<Object> mRecyclerViewItems = new ArrayList<>();

    public static List<Object> typeItemList = new ArrayList<>();


    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_test);
       initCollapsingToolbar();
        initToolbar();
        initRecyclerViews();
        populatRecyclerView();
        addNativeExpressAds();
        setUpAndLoadNativeExpressAds();
        RecyclerView.Adapter adapter = new MainRecyclerViewAdapter(this, mRecyclerViewItems, typeItemList);
        mainRecyclerView.setAdapter(adapter);
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
        LinearLayoutManager testLayoutManager = new LinearLayoutManager(MainActivity.this);
        mainRecyclerView
                .setLayoutManager(testLayoutManager);
        // for categories recyclerView
       catgRecyclerView = (RecyclerView) findViewById(R.id.catg_recycler_view);
        catgRecyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this,LinearLayoutManager.HORIZONTAL,false);
        SnappyLinearLayoutManager lay2outmanager= new SnappyLinearLayoutManager(MainActivity.this,SnappyLinearLayoutManager.HORIZONTAL,false);
        lay2outmanager.setSnapType(SnapType.START);
        lay2outmanager.setSnapPadding(getResources().getDimensionPixelSize(R.dimen.demo_snap_padding));
        lay2outmanager.setSnapInterpolator(new OvershootInterpolator());

        // layoutManager.setAutoMeasureEnabled(true);
      //  catgRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
        catgRecyclerView.setLayoutManager(lay2outmanager);




    }

    private void populatRecyclerView() {
        ArrayList<MainDataModel> arrayList = new ArrayList<>();

    /*    MainRecyclerViewAdapter adapter = new MainRecyclerViewAdapter(MainActivity.this, mRecyclerViewItems);
        mainRecyclerView.setAdapter(adapter);// set adapter on recyclerview
        adapter.notifyDataSetChanged();*/

        // Notify the adapter


        // for category recyclerView
        ArrayList<CategoryDataModel> catgArrayList = new ArrayList<>();

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

    private void addNativeExpressAds() {

        // Loop through the items array and place a new Native Express ad in every ith position in
        // the items List.
        for (int i = 0; i <= mRecyclerViewItems.size(); i += ITEMS_PER_AD) {
            final NativeExpressAdView adView = new NativeExpressAdView(MainActivity.this);
            mRecyclerViewItems.add(i, adView);
        }
    }

    private void setUpAndLoadNativeExpressAds() {
        // Use a Runnable to ensure that the RecyclerView has been laid out before setting the
        // ad size for the Native Express ad. This allows us to set the Native Express ad's
        // width to match the full width of the RecyclerView.
        mainRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                final float scale = MainActivity.this.getResources().getDisplayMetrics().density;
                // Set the ad size and ad unit ID for each Native Express ad in the items list.
                for (int i = 4; i <= mRecyclerViewItems.size(); i += ITEMS_PER_AD) {
                    final NativeExpressAdView adView =
                            (NativeExpressAdView) mRecyclerViewItems.get(i);
                    final CardView cardView = (CardView) findViewById(R.id.ad_card_view);
                    final int adWidth = cardView.getWidth() - cardView.getPaddingLeft()
                            - cardView.getPaddingRight();
                    AdSize adSize = new AdSize((int) (adWidth / scale), NATIVE_EXPRESS_AD_HEIGHT);
                    adView.setAdSize(adSize);
                    adView.setAdUnitId(AD_UNIT_ID);
                }

                // Load the first Native Express ad in the items list.
                loadNativeExpressAd(4);
            }
        });
    }

    private void loadNativeExpressAd(final int index) {

        if (index >= mRecyclerViewItems.size()) {
            return;
        }

        Object item = mRecyclerViewItems.get(index);
        if (!(item instanceof NativeExpressAdView)) {
            throw new ClassCastException("Expected item at index " + index + " to be a Native"
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
                loadNativeExpressAd(index + ITEMS_PER_AD);
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // The previous Native Express ad failed to load. Call this method again to load
                // the next ad in the items list.
                Log.e("MainActivity", "The previous Native Express ad failed to load. Attempting to"
                        + " load the next Native Express ad in the items list.");
                loadNativeExpressAd(index + ITEMS_PER_AD);
            }
        });

        // Load the Native Express ad.
        adView.loadAd(new AdRequest.Builder().build());
    }


}
