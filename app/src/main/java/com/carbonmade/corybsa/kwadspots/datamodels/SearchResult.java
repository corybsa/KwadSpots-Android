package com.carbonmade.corybsa.kwadspots.datamodels;

public class SearchResult {
    private String mImage;
    private String mName;
    private String mType;

    public SearchResult(String image, String name, String type) {
        mImage = image;
        mName = name;
        mType = type;
    }

    public String getImage() {
        return "gs://kwad-spots.appspot.com/images/" + mImage;
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

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }
}
