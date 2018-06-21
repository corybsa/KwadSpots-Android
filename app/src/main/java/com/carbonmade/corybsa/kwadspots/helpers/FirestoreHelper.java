package com.carbonmade.corybsa.kwadspots.helpers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;

public class FirestoreHelper {
    private static final String COLLECTION_SPOTS = "spots";

    private FirebaseFirestore mFirestore;

    public FirestoreHelper() {
        mFirestore = FirebaseFirestore.getInstance();
    }

    public Task<DocumentReference> putSpot(HashMap<String, Object> spot) {
        return mFirestore.collection(COLLECTION_SPOTS).add(spot);
    }

    public Task<Void> updateSpot(String documentId, HashMap<String, Object> spot) {
        Query spots = mFirestore.collection(COLLECTION_SPOTS);
        return ((CollectionReference)spots).document(documentId).update(spot);
    }
}
