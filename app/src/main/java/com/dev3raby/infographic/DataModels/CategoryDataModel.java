package com.dev3raby.infographic.DataModels;

/**
 * Created by Ahmed Yehya on 05/06/2017.
 */

public class CategoryDataModel {
    private String categoryName;
    private String categoryIcon;
    private Integer id;

    public CategoryDataModel(Integer id,String categoryName, String categoryIcon) {
        this.id = id;
        this.categoryName = categoryName;
        this.categoryIcon = categoryIcon;

    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getCategoryIcon() {
        return categoryIcon;
    }

    public String getId() {
        return id.toString();
    }
}
