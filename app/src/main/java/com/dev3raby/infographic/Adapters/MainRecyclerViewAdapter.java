package com.dev3raby.infographic.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dev3raby.infographic.Activities.InfographicActivity;
import com.dev3raby.infographic.R;
import com.dev3raby.infographic.DataModels.LayoutTypeModel;
import com.dev3raby.infographic.DataModels.MainDataModel;
import com.google.android.gms.ads.NativeExpressAdView;

import java.util.List;

/**
 * Created by Ahmed Yehya on 05/06/2017.
 */

public class MainRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<Object> mRecyclerViewItems;
    private List<Object> mItemType;
    // A menu item view type.
    private static final int MENU_ITEM_VIEW_TYPE = 0;

    // The Native Express ad view type.
    private static final int NATIVE_EXPRESS_AD_VIEW_TYPE = 1;



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

        if (modelType.getLayoutType()==1)
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

            case MENU_ITEM_VIEW_TYPE:
                MainDataModel model = (MainDataModel) mRecyclerViewItems.get(position);
            MenuItemViewHolder mainHolder = (MenuItemViewHolder) holder;// holder

            if (model.getSourceIcon().toString() != "") {
                Glide.with(context)
                        .load(model.getSourceIcon().toString())
                        .into(mainHolder.sourceIcon);
            }
            Glide.with(context)
                    .load(model.getInfographicImage().toString())
                    .into(mainHolder.infographicImage);



            // setting title
            mainHolder.sourceName.setText(model.getSourceName());

            mainHolder.infographicName.setText(model.getInfographicName());
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
    public class MenuItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView infographicName;
        public TextView sourceName;
        public ImageView sourceIcon;
        public ImageView infographicImage;


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
