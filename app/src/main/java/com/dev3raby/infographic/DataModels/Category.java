package com.dev3raby.infographic.DataModels;

/**
 * Created by Ahmed Yehya on 02/07/2017.
 */

public class Category {
private String categoryName;
private Integer id;
    private Integer numFollowers;


    public Category(Integer id, String categoryName, Integer numFollowers ) {
        this.categoryName = categoryName;
        this.id = id;
        this.numFollowers = numFollowers;

    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getCatId() {
        return id.toString();
    }
    public String getNumFollowers() {
        return numFollowers.toString();
    }
}
