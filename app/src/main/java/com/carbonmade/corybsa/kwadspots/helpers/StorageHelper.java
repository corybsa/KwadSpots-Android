package com.carbonmade.corybsa.kwadspots.helpers;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

// TODO: keep researching file upload/download [https://firebase.google.com/docs/storage/android/upload-files]
public class StorageHelper {
    private static final long MEGABYTE = 1024 * 1024;

    private FirebaseStorage mFirebaseStorage;
    private OnSuccessListener<byte[]> mOnDownloadSuccessListener;
    private OnFailureListener mOnDownloadFailureListener;
    private OnSuccessListener<UploadTask.TaskSnapshot> mOnUploadSuccessListener;

    private OnFailureListener mOnUploadFailureListener;

    public StorageHelper(FirebaseStorage firebaseStorage) {
        mFirebaseStorage = firebaseStorage;
        mOnDownloadSuccessListener = null;
        mOnDownloadFailureListener = null;
    }

    public void upload(byte[] data) {
        UploadTask uploadTask = mFirebaseStorage.getReference().child("images/test.jpg").putBytes(data);
        uploadTask.addOnSuccessListener(mOnUploadSuccessListener);
        uploadTask.addOnFailureListener(mOnUploadFailureListener);
    }

    public void download(String path) {
        StorageReference gsReference = mFirebaseStorage.getReferenceFromUrl(path);
        gsReference.getBytes(MEGABYTE * 50)
                .addOnSuccessListener(mOnDownloadSuccessListener)
                .addOnFailureListener(mOnDownloadFailureListener);
    }

    public void setOnDownloadSuccessListener(OnSuccessListener<byte[]> listener) {
        mOnDownloadSuccessListener = listener;
    }

    public void setOnDownloadFailureListener(OnFailureListener listener) {
        mOnDownloadFailureListener = listener;
    }

    public void setOnUploadSuccessListener(OnSuccessListener<UploadTask.TaskSnapshot> listener) {
        mOnUploadSuccessListener = listener;
    }

    public void setOnUploadFailureListener(OnFailureListener listener) {
        mOnUploadFailureListener = listener;
    }
}
