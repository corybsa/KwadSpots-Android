package com.carbonmade.corybsa.kwadspots.services;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import javax.inject.Inject;

public class SpotService {
    private FirebaseStorage mFirebaseStorage;
    private StorageReference mStorageReference;

    @Inject
    SpotService(FirebaseStorage firebaseStorage) {
        mFirebaseStorage = firebaseStorage;
    }

    public UploadTask upload(String filename, byte[] data) {
        mStorageReference = mFirebaseStorage.getReference();
        return mStorageReference.child(String.format("images/%s", filename)).putBytes(data);
    }
}
