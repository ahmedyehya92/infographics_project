package com.dev3raby.infographic.RecyclerViewModels;

/**
 * Created by Ahmed Yehya on 05/06/2017.
 */

public class CategoryDataModel {
    private String categoryName;
    private String categoryIcon;

    public CategoryDataModel(String categoryName, String categoryIcon) {
        this.categoryName = categoryName;
        this.categoryIcon = categoryIcon;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getCategoryIcon() {
        return categoryIcon;
    }
}
