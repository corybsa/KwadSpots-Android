package com.carbonmade.corybsa.kwadspots.services;

import android.os.Bundle;

import com.carbonmade.corybsa.kwadspots.datamodels.Spot;
import com.carbonmade.corybsa.kwadspots.datamodels.SpotComment;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

public class SpotService {
    private static final String KEY_REFERENCE = "Storage Reference";
    private static final String COLLECTION_SPOTS = "spots";
    private static final String COLLECTION_COMMENTS = "comments";

    private StorageReference mStorageReference;

    @Inject FirebaseStorage mFirebaseStorage;
    @Inject FirebaseFirestore mFirestore;

    @Inject
    public SpotService() {
    }

    public UploadTask upload(String filename, byte[] data) {
        mStorageReference = mFirebaseStorage.getReference();
        return mStorageReference.child(String.format("images/%s", filename)).putBytes(data);
    }

    public void onSaveInstanceState(Bundle outState) {
        if(mStorageReference != null) {
            outState.putString(KEY_REFERENCE, mStorageReference.toString());
        }
    }

    public UploadTask restoreUploadState(Bundle savedInstanceState) {
        final String stringRef = savedInstanceState.getString(KEY_REFERENCE);

        if(stringRef == null) {
            return null;
        }

        mStorageReference = FirebaseStorage.getInstance().getReferenceFromUrl(stringRef);
        List<UploadTask> uploadTasks = mStorageReference.getActiveUploadTasks();

        if(uploadTasks.size() > 0) {
            return uploadTasks.get(0);
        }

        return null;
    }

    public Task<DocumentReference> putSpot(HashMap<String, Object> spot) {
        return mFirestore.collection(COLLECTION_SPOTS).add(spot);
    }

    public Task<QuerySnapshot> getSpots(LatLngBounds bounds) {
        double top = bounds.northeast.latitude;
        double bottom = bounds.southwest.latitude;
        double right = bounds.northeast.longitude;
        double left = bounds.southwest.longitude;

        if(top == bottom) {
            return null;
        }

        Query ref = mFirestore.collection(COLLECTION_SPOTS)
                .whereGreaterThan(Spot.FIELD_LONGITUDE, left)
                .whereLessThan(Spot.FIELD_LONGITUDE, right);

        return ref.get();
    }

    public Task<Void> updateSpot(String spotId, HashMap<String, Object> spot) {
        return mFirestore.collection(COLLECTION_SPOTS).document(spotId).update(spot);
    }

    public Task<Void> deleteSpot(String spotId) {
        return mFirestore.collection(COLLECTION_SPOTS).document(spotId).delete();
    }

    public Task<DocumentReference> putSpotComment(String spotId, HashMap<String, Object> comment) {
        return mFirestore.collection(COLLECTION_SPOTS)
                .document(spotId)
                .collection(COLLECTION_COMMENTS)
                .add(comment);
    }

    public Task<QuerySnapshot> getSpotComments(String spotId) {
        Query ref = mFirestore.collection(COLLECTION_SPOTS)
                .document(spotId)
                .collection(COLLECTION_COMMENTS);

        return ref.get();
    }
}
