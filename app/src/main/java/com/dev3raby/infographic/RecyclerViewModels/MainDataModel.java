package com.dev3raby.infographic.RecyclerViewModels;

/**
 * Created by Ahmed Yehya on 05/06/2017.
 */

public class MainDataModel {
    private String infographicName;
    private String sourceName;
    private String sourceIcon;
    private String infographicImage;

    public MainDataModel(String infographicName, String sourceName, String sourceIcon, String infographicImage) {
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
