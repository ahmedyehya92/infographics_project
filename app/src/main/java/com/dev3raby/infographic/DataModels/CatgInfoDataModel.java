package com.dev3raby.infographic.DataModels;

/**
 * Created by Ahmed Yehya on 13/06/2017.
 */

public class CatgInfoDataModel {
    private String infographicName;
    private String sourceName;
    private String sourceIcon;
    private String infographicImage;

    public CatgInfoDataModel(String infographicName, String sourceName, String sourceIcon, String infographicImage) {
        this.infographicName = infographicName;
        this.sourceName = sourceName;
        this.sourceIcon = sourceIcon;
        this.infographicImage = infographicImage;
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
}
