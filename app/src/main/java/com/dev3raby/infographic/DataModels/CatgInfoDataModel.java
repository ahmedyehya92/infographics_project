package com.dev3raby.infographic.DataModels;

/**
 * Created by Ahmed Yehya on 13/06/2017.
 */

public class CatgInfoDataModel {
    private Integer id;
    private String infographicName;
    private String sourceName;
    private String sourceIcon;
    private String infographicImage;
    private Integer like_counter;
    private Integer seen_counter;


    public CatgInfoDataModel(Integer id, String infographicName, String sourceName, String sourceIcon, String infographicImage, Integer like_counter, Integer seen_counter) {
        this.infographicName = infographicName;
        this.sourceName = sourceName;
        this.sourceIcon = sourceIcon;
        this.infographicImage = infographicImage;
        this.id = id;
        this.like_counter = like_counter;
        this.seen_counter = seen_counter;
    }

    public String getLike_counter() {
        return like_counter.toString();
    }

    public String getSeen_counter() {
        return seen_counter.toString();
    }

    public String getInfographicName() {
        return infographicName;
    }

    public String getSourceName() {
        return sourceName;
    }

    public String getSourceIcon() {
        return sourceIcon;
    }

    public String getInfographicImage() {
        return infographicImage;
    }

    public String getCIId() {
        return id.toString();
    }
}
