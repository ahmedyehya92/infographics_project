package com.dev3raby.infographic.RecyclerViewHolders;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dev3raby.infographic.Activities.InfographicActivity;
import com.dev3raby.infographic.R;
import com.dev3raby.infographic.RecyclerViewModels.CatgInfoDataModel;

import java.util.ArrayList;

/**
 * Created by Ahmed Yehya on 13/06/2017.
 */

public class CatgInfoRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {

    public TextView infographicName;
    public TextView sourceName;
    public ImageView sourceIcon;
    public ImageView infographicImage;


    ArrayList<CatgInfoDataModel> MainList = new ArrayList<CatgInfoDataModel>();
    Context contextA;



    public CatgInfoRecyclerViewHolder(View view, Context contextA, ArrayList<CatgInfoDataModel> MainList) {
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



    }

    @Override
    public void onClick(View v) {


        int position = getAdapterPosition();
        CatgInfoDataModel item = this.MainList.get(position);

        Toast.makeText(contextA,item.getInfographicName(),Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this.contextA, InfographicActivity.class);
        this.contextA.startActivity(intent);




    }

}
