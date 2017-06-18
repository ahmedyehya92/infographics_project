package com.dev3raby.infographic.RecyclerViewAdapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.dev3raby.infographic.R;
import com.dev3raby.infographic.RecyclerViewHolders.CategoryRecyclerViewHolder;
import com.dev3raby.infographic.RecyclerViewModels.CategoryDataModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Ahmed Yehya on 05/06/2017.
 */

public class CategoryRecyclerViewAdapter extends
        RecyclerView.Adapter<CategoryRecyclerViewHolder> {

    private ArrayList<CategoryDataModel> arrayList;
    private Context context;

    public CategoryRecyclerViewAdapter(Context context,
                                   ArrayList<CategoryDataModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;

    }

    @Override
    public int getItemCount() {
        return (null != arrayList ? arrayList.size() : 0);

    }

    @Override
    public void onBindViewHolder(CategoryRecyclerViewHolder holder, int position) {
        final CategoryDataModel model = arrayList.get(position);

        CategoryRecyclerViewHolder mainHolder = (CategoryRecyclerViewHolder) holder;// holder

        mainHolder.categoryName.setText(model.getCategoryName());

        Picasso.with(context)
                .load(model.getCategoryIcon().toString())
                .into(mainHolder.categoryIcon);

    }

    @Override
    public CategoryRecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        // This method will inflate the custom layout and return as viewholder
        LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());

        ViewGroup mainGroup = (ViewGroup) mInflater.inflate(
                R.layout.category_card_layout, viewGroup, false);
        CategoryRecyclerViewHolder listHolder = new CategoryRecyclerViewHolder(mainGroup,context,arrayList);
        return listHolder;

    }

}
