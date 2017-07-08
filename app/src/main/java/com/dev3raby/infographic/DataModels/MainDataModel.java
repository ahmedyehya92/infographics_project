package com.dev3raby.infographic.DataModels;

/**
 * Created by Ahmed Yehya on 05/06/2017.
 */

public class MainDataModel {
    private String infographicName;
    private String sourceName;
    private String sourceIcon;
    private String infographicImage;
    private Integer id;
    private Integer like_counter;
    private Integer seen_counter;

    public MainDataModel(Integer id, String infographicName, String sourceName, String sourceIcon, String infographicImage, Integer like_counter, Integer seen_counter) {
        this.infographicName = infographicName;
        this.sourceName = sourceName;
        this.sourceIcon = sourceIcon;
        this.infographicImage = infographicImage;
        this.id = id;
        this.like_counter = like_counter;
        this.seen_counter = seen_counter;
    }

    public String getSeen_counter() {
        return seen_counter.toString();
    }

    public String getLike_counter() {
        return like_counter.toString();
    }

    public String getId(){return id.toString();}

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
}
