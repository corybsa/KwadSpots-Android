package com.carbonmade.corybsa.kwadspots.helpers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

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
}
