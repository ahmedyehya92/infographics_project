package com.dev3raby.infographic.RecyclerViewAdapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.dev3raby.infographic.R;
import com.dev3raby.infographic.RecyclerViewHolders.MainRecyclerViewHolder;
import com.dev3raby.infographic.RecyclerViewModels.MainDataModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Ahmed Yehya on 05/06/2017.
 */

public class MainRecyclerViewAdapter extends
        RecyclerView.Adapter<MainRecyclerViewHolder> {




    private ArrayList<MainDataModel> arrayList;
    private Context context;

    public MainRecyclerViewAdapter(Context context,
                                ArrayList<MainDataModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;

    }

    @Override
    public int getItemCount() {
        return (null != arrayList ? arrayList.size() : 0);

    }

    @Override
    public void onBindViewHolder(MainRecyclerViewHolder holder, int position) {
        final MainDataModel model = arrayList.get(position);

        MainRecyclerViewHolder mainHolder = (MainRecyclerViewHolder) holder;// holder

        if (model.getSourceIcon().toString() != "") {
            Picasso.with(context)
                    .load(model.getSourceIcon().toString())
                    .into(mainHolder.sourceIcon);
        }
        Picasso.with(context)
                .load(model.getInfographicImage().toString())
                .into(mainHolder.infographicImage);



        // setting title
        mainHolder.sourceName.setText(model.getSourceName());

        mainHolder.infographicName.setText(model.getInfographicName());



    }
    @Override
    public MainRecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        // This method will inflate the custom layout and return as viewholder
        LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());

        ViewGroup mainGroup = (ViewGroup) mInflater.inflate(
                R.layout.test_card_layout, viewGroup, false);
        MainRecyclerViewHolder listHolder = new MainRecyclerViewHolder(mainGroup,context,arrayList);
        return listHolder;

    }

}
