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

    public MainDataModel(Integer id, String infographicName, String sourceName, String sourceIcon, String infographicImage) {
        this.infographicName = infographicName;
        this.sourceName = sourceName;
        this.sourceIcon = sourceIcon;
        this.infographicImage = infographicImage;
        this.id = id;
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
