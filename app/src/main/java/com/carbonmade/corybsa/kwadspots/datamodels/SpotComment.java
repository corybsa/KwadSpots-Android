package com.carbonmade.corybsa.kwadspots.datamodels;

import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Date;

public class SpotComment {
    public static final String FIELD_USER = "user";
    public static final String FIELD_COMMENT = "comment";
    public static final String FIELD_DATE = "date";
    public static final String DATE_FORMAT = "MM/dd/yy hh:mm a";

    private String mId;
    private String mCreatedBy;
    private String mComment;
    private Date mCreatedDate;

    public SpotComment(QueryDocumentSnapshot document) {
        mId = document.getId();
        mCreatedBy = document.getString(FIELD_USER);
        mComment = document.getString(FIELD_COMMENT);
        mCreatedDate = document.getDate(FIELD_DATE);
    }

    public String getCreatedBy() {
        return mCreatedBy;
    }

    public String getComment() {
        return mComment;
    }

    public Date getCreatedDate() {
        return mCreatedDate;
    }
}
