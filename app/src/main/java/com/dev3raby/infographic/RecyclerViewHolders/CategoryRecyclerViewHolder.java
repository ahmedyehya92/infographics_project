package com.dev3raby.infographic.RecyclerViewHolders;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dev3raby.infographic.Activities.CatgInfographicsActivity;
import com.dev3raby.infographic.R;
import com.dev3raby.infographic.RecyclerViewModels.CategoryDataModel;

import java.util.ArrayList;

/**
 * Created by Ahmed Yehya on 05/06/2017.
 */

public class CategoryRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView categoryName;
    public ImageView categoryIcon;

    ArrayList<CategoryDataModel> categoriesList = new ArrayList<CategoryDataModel>();
    Context contextA;

    public CategoryRecyclerViewHolder(View view , Context contextA, ArrayList<CategoryDataModel> categoriesList) {
        super(view);

        this.categoriesList = categoriesList;
        this.contextA = contextA;

        view.setOnClickListener(this);

        // Find all views ids
        this.categoryName = (TextView) view
                .findViewById(R.id.tv_category_name);

        this.categoryIcon = (ImageView) view
                .findViewById(R.id.im_category);
    }

    @Override
    public void onClick(View v) {


        int position = getAdapterPosition();
        CategoryDataModel item = this.categoriesList.get(position);

        Toast.makeText(contextA,item.getCategoryName(),Toast.LENGTH_LONG).show();

        Intent intent = new Intent(this.contextA, CatgInfographicsActivity.class);
        this.contextA.startActivity(intent);

    }
    }
