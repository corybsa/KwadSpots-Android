package com.carbonmade.corybsa.kwadspots.helpers;

import com.carbonmade.corybsa.kwadspots.datamodels.Spot;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

public class FirestoreHelper {
    private static final String COLLECTION_SPOTS = "spots";

    private FirebaseFirestore mFirestore;

    public FirestoreHelper(FirebaseFirestore firestore) {
        mFirestore = firestore;
    }

    public Task<DocumentReference> putSpot(HashMap<String, Object> spot) {
        return mFirestore.collection(COLLECTION_SPOTS).add(spot);
    }

    public Task<Void> updateSpot(String documentId, HashMap<String, Object> spot) {
        Query spots = mFirestore.collection(COLLECTION_SPOTS);
        return ((CollectionReference)spots).document(documentId).update(spot);
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
}
