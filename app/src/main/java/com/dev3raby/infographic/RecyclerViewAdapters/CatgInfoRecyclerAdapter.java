package com.dev3raby.infographic.RecyclerViewAdapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.dev3raby.infographic.R;
import com.dev3raby.infographic.RecyclerViewHolders.CatgInfoRecyclerViewHolder;
import com.dev3raby.infographic.RecyclerViewModels.CatgInfoDataModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Ahmed Yehya on 13/06/2017.
 */

public class CatgInfoRecyclerAdapter extends
        RecyclerView.Adapter<CatgInfoRecyclerViewHolder> {

    private ArrayList<CatgInfoDataModel> arrayList;
    private Context context;

    public CatgInfoRecyclerAdapter(Context context,
                                   ArrayList<CatgInfoDataModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;

    }

    @Override
    public int getItemCount() {
        return (null != arrayList ? arrayList.size() : 0);

    }

    @Override
    public void onBindViewHolder(CatgInfoRecyclerViewHolder holder, int position) {
        final CatgInfoDataModel model = arrayList.get(position);

        CatgInfoRecyclerViewHolder mainHolder = (CatgInfoRecyclerViewHolder) holder;// holder

        Picasso.with(context)
                .load(model.getSourceIcon().toString())
                .into(mainHolder.sourceIcon);
        Picasso.with(context)
                .load(model.getInfographicImage().toString())
                .into(mainHolder.infographicImage);



        // setting title
        mainHolder.sourceName.setText(model.getSourceName());

        mainHolder.infographicName.setText(model.getInfographicName());



    }
    @Override
    public CatgInfoRecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        // This method will inflate the custom layout and return as viewholder
        LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());

        ViewGroup mainGroup = (ViewGroup) mInflater.inflate(
                R.layout.card_layout, viewGroup, false);
        CatgInfoRecyclerViewHolder listHolder = new CatgInfoRecyclerViewHolder(mainGroup,context,arrayList);
        return listHolder;

    }

}
