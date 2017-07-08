package com.dev3raby.infographic.ViewHolders;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dev3raby.infographic.Activities.InfographicActivity;
import com.dev3raby.infographic.DataModels.MainDataModel;
import com.dev3raby.infographic.R;

import java.util.ArrayList;

/**
 * Created by Ahmed Yehya on 07/07/2017.
 */

public class SearchRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView infographicName;
    public TextView sourceName;
    public ImageView sourceIcon;
    public ImageView infographicImage;
    public TextView like_counter;
    public TextView seen_counter;

    ArrayList<MainDataModel> MainList = new ArrayList<MainDataModel>();
    Context contextA;

    public SearchRecyclerViewHolder(View view, Context contextA, ArrayList<MainDataModel> MainList) {
        super(view);
        // Find all views ids

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
        MainDataModel item = this.MainList.get(position);


        Intent intent = new Intent(this.contextA, InfographicActivity.class);
        intent.putExtra(id_key,item.getId());
        this.contextA.startActivity(intent);



    }

}
