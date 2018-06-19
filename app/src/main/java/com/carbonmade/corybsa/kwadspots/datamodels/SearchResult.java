package com.carbonmade.corybsa.kwadspots.datamodels;

public class SearchResult {
    private String mImage;
    private String mName;
    private String mDescription;

    public SearchResult(String image, String name, String description) {
        mImage = image;
        mName = name;
        mDescription = description;
    }

    public String getImage() {
        return mImage;
    }

    public void setImage(String image) {
        mImage = image;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }
}
