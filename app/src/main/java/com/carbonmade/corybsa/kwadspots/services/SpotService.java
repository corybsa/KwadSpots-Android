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

/**
 * A class to help with CRUD operations for both {@link Spot}s and {@link SpotComment}s.
 */
public class SpotService {
    public static final int COMMENT_PAGE_SIZE = 20;
    public static final int SEARCH_RESULT_PAGE_SIZE = 20;

    private static final String KEY_REFERENCE = "Storage Reference";
    private static final String COLLECTION_SPOTS = "spots";
    private static final String COLLECTION_COMMENTS = "comments";

    private StorageReference mStorageReference;

    @Inject FirebaseStorage mFirebaseStorage;
    @Inject FirebaseFirestore mFirestore;

    @Inject
    public SpotService() {
    }

    /**
     * Uploads the {@code data} with name {@code filename} to FireStorage.
     *
     * @param filename the name of the file to store in FireStorage
     * @param data the image data
     * @return returns an {@link UploadTask}
     */
    public UploadTask upload(String filename, byte[] data) {
        mStorageReference = mFirebaseStorage.getReference();
        return mStorageReference.child(String.format("images/%s", filename)).putBytes(data);
    }

    /**
     * Saves the current {@code StorageReference}.
     *
     * @param outState the state from the activity or fragment.
     */
    public void onSaveInstanceState(Bundle outState) {
        if(mStorageReference != null) {
            outState.putString(KEY_REFERENCE, mStorageReference.toString());
        }
    }

    /**
     * Restores the saved {@code StorageReference} and returns the {@link UploadTask} if there was
     * an upload in progress.
     *
     * @param savedInstanceState the state from the activity or fragment.
     * @return returns an {@link UploadTask}
     */
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

    /**
     * Adds the spot to the collection {@value COLLECTION_SPOTS}.
     *
     * @param spot the spot to store in the database
     * @return returns a {@link Task}
     */
    public Task<DocumentReference> putSpot(HashMap<String, Object> spot) {
        return mFirestore.collection(COLLECTION_SPOTS).add(spot);
    }

    /**
     * Gets all spots that are within the longitude bounds of {@code bounds}. Due to the limitations
     * of Firestore this method cannot filter on both latitude and longitude. Once the method has
     * returned it is up to the caller to filter latitude.
     *
     * @param bounds the current bounds of the {@link com.google.android.gms.maps.MapView}
     * @return returns a {@link Task}
     */
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

    /**
     * Updates a spot with id {@code spotId} with the data in the {@link HashMap} provided.
     *
     * @param spotId the document id of the spot to update
     * @param spot the data for the spot
     * @return returns a {@link Task}
     */
    public Task<Void> updateSpot(String spotId, HashMap<String, Object> spot) {
        return mFirestore.collection(COLLECTION_SPOTS).document(spotId).update(spot);
    }

    /**
     * Deletes the spot with id {@code spotId}.
     *
     * @param spotId the document id of the spot to delete
     * @return returns a {@link Task}
     */
    public Task<Void> deleteSpot(String spotId) {
        return mFirestore.collection(COLLECTION_SPOTS).document(spotId).delete();
    }

    /**
     * Inserts a {@code comment} for spot with id {@code spotId}.
     *
     * @param spotId the document id of the spot to insert a comment for
     * @param comment the data for the comment
     * @return returns a {@link Task}
     */
    public Task<DocumentReference> putSpotComment(String spotId, HashMap<String, Object> comment) {
        return mFirestore.collection(COLLECTION_SPOTS)
                .document(spotId)
                .collection(COLLECTION_COMMENTS)
                .add(comment);
    }

    /**
     * Gets all comments for the spot with id {@code spotId}.
     *
     * @param spotId the document id of the spot
     * @return returns a {@link Task}
     */
    public Task<QuerySnapshot> getSpotComments(String spotId) {
        Query ref = mFirestore.collection(COLLECTION_SPOTS)
                .document(spotId)
                .collection(COLLECTION_COMMENTS);

        return ref.get();
    }
}
