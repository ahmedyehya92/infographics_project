package com.dev3raby.infographic.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.dev3raby.infographic.DataModels.MainDataModel;
import com.dev3raby.infographic.R;
import com.dev3raby.infographic.ViewHolders.SearchRecyclerViewHolder;

import java.util.ArrayList;

/**
 * Created by Ahmed Yehya on 07/07/2017.
 */

public class SearchRecyclerViewAdapter extends
        RecyclerView.Adapter<SearchRecyclerViewHolder>{

    private ArrayList<MainDataModel> arrayList;
    private Context context;

    public SearchRecyclerViewAdapter (Context context,
                                        ArrayList<MainDataModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;

    }

    @Override
    public int getItemCount() {
        return (null != arrayList ? arrayList.size() : 0);

    }

    @Override
    public void onBindViewHolder(SearchRecyclerViewHolder holder, int position) {
        final MainDataModel model = arrayList.get(position);

        SearchRecyclerViewHolder mainHolder = (SearchRecyclerViewHolder) holder;// holder

        Glide.with(context)
                .load(model.getSourceIcon().toString())
                .into(mainHolder.sourceIcon);
        Glide.with(context)
                .load(model.getInfographicImage().toString())
                .into(mainHolder.infographicImage);



        // setting title
        mainHolder.sourceName.setText(model.getSourceName());

        mainHolder.infographicName.setText(model.getInfographicName());

        mainHolder.like_counter.setText(model.getLike_counter());

        mainHolder.seen_counter.setText(model.getSeen_counter());


    }
    @Override
    public SearchRecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        // This method will inflate the custom layout and return as viewholder
        LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());

        ViewGroup mainGroup = (ViewGroup) mInflater.inflate(
                R.layout.test_card_layout, viewGroup, false);
        SearchRecyclerViewHolder listHolder = new SearchRecyclerViewHolder(mainGroup,context,arrayList);
        return listHolder;

    }

}
