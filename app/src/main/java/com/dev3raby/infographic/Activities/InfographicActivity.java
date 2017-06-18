package com.dev3raby.infographic.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.dev3raby.infographic.R;

import uk.co.senab.photoview.PhotoViewAttacher;

public class InfographicActivity extends AppCompatActivity {

    Toolbar toolbar;
    RelativeLayout bottomBar;
    ImageView infographicView;
    PhotoViewAttacher mAttacher;
    boolean isDetailVisible = true;
    private static boolean liked = false;
    private static boolean bookmarked = false;
    private Button likeButton, bookmarkButton, backButton;
    private Animation bottomBarAnimShow, bottomBarAnimHide, toolbarAnimationShow, toolbarAnimationHide;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infographic);
        initViews();
        isLiked();
        isBookmarked();
        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likeButtonClick();
            }
        });

        bookmarkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookmarkButtonClick();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    public void initViews()
    {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bottomBar = (RelativeLayout)findViewById(R.id.bottom_bar);

        infographicView = (ImageView)findViewById(R.id.image_infographic);

        backButton = (Button) findViewById(R.id.btn_back);

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
}
