package com.carbonmade.corybsa.kwadspots.datamodels;

import com.google.firebase.firestore.QueryDocumentSnapshot;

public class Spot {
    public static final String FIELD_COMMENT = "comment";
    public static final String FIELD_LATITUDE = "latitude";
    public static final String FIELD_LONGITUDE = "longitude";
    public static final String FIELD_NAME = "name";
    public static final String FIELD_PICTURE = "picture";
    public static final String FIELD_RATING = "rating";
    public static final String FIELD_TYPE = "type";

    private String mComment;
    private double mLatitude;
    private double mLongitude;
    private String mName;
    private String mPicture;
    private double mRating;
    private long mType;

    public Spot(QueryDocumentSnapshot document) {
        mComment = document.getString(FIELD_COMMENT);
        mName = document.getString(FIELD_NAME);
        mPicture = document.getString(FIELD_PICTURE);

        if(document.contains(FIELD_LATITUDE)) {
            mLatitude = document.getDouble(FIELD_LATITUDE);
        }

        if(document.contains(FIELD_LONGITUDE)) {
            mLongitude = document.getDouble(FIELD_LONGITUDE);
        }

        if(document.contains(FIELD_RATING)) {
            mRating = document.getDouble(FIELD_RATING);
        }

        if(document.contains(FIELD_TYPE)) {
            mType = document.getLong(FIELD_TYPE);
        }
    }

    public String getComment() {
        return mComment;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public String getName() {
        return mName;
    }

    public String getPicture() {
        return mPicture;
    }

    public double getRating() {
        return mRating;
    }

    public long getType() {
        return mType;
    }
}
