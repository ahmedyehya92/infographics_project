package com.dev3raby.infographic.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.dev3raby.infographic.Activities.InfographicActivity;
import com.dev3raby.infographic.R;
import com.dev3raby.infographic.DataModels.LayoutTypeModel;
import com.dev3raby.infographic.DataModels.MainDataModel;
import com.google.android.gms.ads.NativeExpressAdView;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Ahmed Yehya on 05/06/2017.
 */

public class MainRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<Object> mRecyclerViewItems;
    private List<Object> mItemType;
    private Bitmap newBitmap;
    // A menu item view type.
    private static final int MENU_ITEM_VIEW_TYPE = 0;

    // The Native Express ad view type.
    private static final int NATIVE_EXPRESS_AD_VIEW_TYPE = 1;
    private static final int FIRST_ITEM_VIEWTYPE = 2;



    private Context context;

    public MainRecyclerViewAdapter(Context context,
                                List<Object> recyclerViewItems, List<Object> itemType) {
        this.context = context;
        this.mRecyclerViewItems = recyclerViewItems;
        this.mItemType = itemType;
    }
    public class NativeExpressAdViewHolder extends RecyclerView.ViewHolder {

        NativeExpressAdViewHolder(View view) {
            super(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
    /*   return (((position+1) % HomeFragment.ITEMS_PER_AD == 0)&& (HomeFragment.isThereAds==1)) ? NATIVE_EXPRESS_AD_VIEW_TYPE
                : MENU_ITEM_VIEW_TYPE; */

        final LayoutTypeModel modelType = (LayoutTypeModel) mItemType.get(position);

        if (modelType.getLayoutType()==2)
        {
            return FIRST_ITEM_VIEWTYPE;
        }

        else if (modelType.getLayoutType()==1)
        {
            return MENU_ITEM_VIEW_TYPE;
        }



        else {
            return NATIVE_EXPRESS_AD_VIEW_TYPE;
        }
  /*
        if (HomeFragment.adsArrayList.contains(position))
    {
        return NATIVE_EXPRESS_AD_VIEW_TYPE;
    }

    else
    {
        return MENU_ITEM_VIEW_TYPE;
    } */



    }

    @Override
    public int getItemCount() {

         return mRecyclerViewItems.size();

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        int viewType = getItemViewType(position);
        switch (viewType) {

            case FIRST_ITEM_VIEWTYPE:

            break;
            case MENU_ITEM_VIEW_TYPE:
                MainDataModel model = (MainDataModel) mRecyclerViewItems.get(position);
            final MenuItemViewHolder mainHolder = (MenuItemViewHolder) holder;// holder

            if (model.getSourceIcon().toString() != "") {
                Glide.with(context)
                        .load(model.getSourceIcon().toString()).diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(mainHolder.sourceIcon);
            }
            /*
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();

                //save scaled down image to cache dir
                try {
                    newBitmap = Glide.
                            with(context).
                            load(model.getInfographicImage().toString()).
                            asBitmap().
                            into(100, 100).get();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                newBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
*/
            /*
                Glide.with(context)
                        .load(model.getInfographicImage().toString())
                        .asBitmap()
                        .into(new BitmapImageViewTarget(mainHolder.infographicImage) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                //Play with bitmap
                                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                                resource.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                                super.setResource(resource);
                            }
                        });







*/
/*
                RequestListener<String, GlideDrawable> glideDrawableRequestListener = new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }
                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        Bitmap bitmap = ((GlideBitmapDrawable) resource).getBitmap();
                        if (bitmap != null) {
                            Context context = mainHolder.infographicImage.getContext();
                            PaletteLoader.with(context, model)
                                    .load(bitmap)
                                    .setPaletteRequest(new PaletteRequest(PaletteRequest.SwatchType.REGULAR_VIBRANT, PaletteRequest.SwatchColor.BACKGROUND))
                                    .into(mainHolder.infographicImage);

                        }
                        return false;
                    }
                };

*/






                Glide.with(context)
                    .load(model.getInfographicImage().toString())
                    .into(mainHolder.infographicImage);



            // setting title
            mainHolder.sourceName.setText(model.getSourceName());

            mainHolder.infographicName.setText(model.getInfographicName());

            mainHolder.like_counter.setText(model.getLike_counter());

            mainHolder.seen_counter.setText(model.getSeen_counter());
                break;

            case NATIVE_EXPRESS_AD_VIEW_TYPE:
                // fall through
            default:


                NativeExpressAdViewHolder nativeExpressHolder =
                        (NativeExpressAdViewHolder) holder;
                NativeExpressAdView adView =
                        (NativeExpressAdView) mRecyclerViewItems.get(position);
                ViewGroup adCardView = (ViewGroup) nativeExpressHolder.itemView;
                // The NativeExpressAdViewHolder recycled by the RecyclerView may be a different
                // instance than the one used previously for this position. Clear the
                // NativeExpressAdViewHolder of any subviews in case it has a different
                // AdView associated with it, and make sure the AdView for this position doesn't
                // already have a parent of a different recycled NativeExpressAdViewHolder.
                if (adCardView.getChildCount() > 0) {
                    adCardView.removeAllViews();
                }
                if (adView.getParent() != null) {
                    ((ViewGroup) adView.getParent()).removeView(adView);
                }

                // Add the Native Express ad to the native express ad view.
                adCardView.addView(adView);
        }




    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        switch (viewType) {



            case MENU_ITEM_VIEW_TYPE:
                LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());

                ViewGroup mainGroup = (ViewGroup) mInflater.inflate(
                        R.layout.test_card_layout, viewGroup, false);
                MenuItemViewHolder listHolder = new MenuItemViewHolder(mainGroup, context,  mRecyclerViewItems);
                return listHolder;

            case FIRST_ITEM_VIEWTYPE:
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

                ViewGroup maingroup = (ViewGroup) inflater.inflate(
                        R.layout.title_item, viewGroup, false);
                FirstItemViewHolder listholder = new FirstItemViewHolder(maingroup);
                return listholder;

            case NATIVE_EXPRESS_AD_VIEW_TYPE:
                // fall through
            default:
                View nativeExpressLayoutView = LayoutInflater.from(
                        viewGroup.getContext()).inflate(R.layout.native_express_ad_container,
                        viewGroup, false);
                return new NativeExpressAdViewHolder(nativeExpressLayoutView);
        }

        // This method will inflate the custom layout and return as viewholder


    }

    public class FirstItemViewHolder extends RecyclerView.ViewHolder {
        public TextView listTitle;




        public FirstItemViewHolder(View itemView) {
            super(itemView);
            this.listTitle = (TextView) itemView.findViewById(R.id.tx_list_title);

        }
    }


    public class MenuItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView infographicName;
        public TextView sourceName;
        public ImageView sourceIcon;
        public ImageView infographicImage;
        public TextView like_counter;
        public TextView seen_counter;


        private final List<Object> MainList ;

       // ArrayList<MainDataModel> MainList = new ArrayList<MainDataModel>();
        Context contextA;
        MenuItemViewHolder(View view, Context contextA, List<Object> MainList) {
            super(view);
            this.MainList = MainList;
            this.contextA = contextA;

            view.setOnClickListener(this);

            this.infographicName = (TextView) view
                    .findViewById(R.id.tv_infographic_name);

            this.sourceName = (TextView) view
                    .findViewById(R.id.tv_source_name);

            this.sourceIcon = (ImageView) view
                    .findViewById(R.id.im_source_icon);

            this.infographicImage = (ImageView) view
                    .findViewById(R.id.im_infographic);

            this.like_counter = (TextView) view.findViewById(R.id.tx_fav_counter);

            this.seen_counter = (TextView) view.findViewById(R.id.tx_seen_counter);


        }

        @Override
        public void onClick(View v) {

            final String id_key = "idKey";
            int position = getAdapterPosition();
            MainDataModel item = (MainDataModel) this.MainList.get(position);


            Intent intent = new Intent(this.contextA, InfographicActivity.class);
            intent.putExtra(id_key,item.getId());

            this.contextA.startActivity(intent);



        }
    }


}
