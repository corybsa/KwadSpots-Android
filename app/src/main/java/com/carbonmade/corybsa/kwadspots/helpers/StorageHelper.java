package com.carbonmade.corybsa.kwadspots.helpers;

import android.app.Activity;
import android.os.Bundle;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.List;

public class StorageHelper {
    private static final long MEGABYTE = 1024 * 1024;
    private static final String KEY_REFERENCE = "Storage Reference";

    private FirebaseStorage mFirebaseStorage;
    private StorageReference mStorageReference;

    private OnFailureListener mOnDownloadFailureListener;
    private OnSuccessListener<byte[]> mOnDownloadSuccessListener;

    private OnSuccessListener<UploadTask.TaskSnapshot> mOnUploadSuccessListener;
    private OnFailureListener mOnUploadFailureListener;
    private OnProgressListener<UploadTask.TaskSnapshot> mOnUploadProgressListener;

    public StorageHelper(FirebaseStorage firebaseStorage) {
        mFirebaseStorage = firebaseStorage;
        mOnDownloadSuccessListener = null;
        mOnDownloadFailureListener = null;
        mOnUploadProgressListener = null;
    }

    public void upload(String filename, byte[] data) {
        mStorageReference = mFirebaseStorage.getReference();
        UploadTask uploadTask = mStorageReference.child(String.format("images/%s.jpg", filename)).putBytes(data);
        uploadTask.addOnSuccessListener(mOnUploadSuccessListener);
        uploadTask.addOnFailureListener(mOnUploadFailureListener);
        uploadTask.addOnProgressListener(mOnUploadProgressListener);
    }

    public void download(String path) {
        mStorageReference = mFirebaseStorage.getReferenceFromUrl(path);
        mStorageReference.getBytes(MEGABYTE * 50)
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

    public void setOnUploadProgressListener(OnProgressListener<UploadTask.TaskSnapshot> onUploadProgressListener) {
        mOnUploadProgressListener = onUploadProgressListener;
    }

    public void onSaveInstanceState(Bundle outState) {
        if(mStorageReference != null) {
            outState.putString(KEY_REFERENCE, mStorageReference.toString());
        }
    }

    public void onRestoreInstanceState(final Activity activity, Bundle savedInstanceState) {
        final String stringRef = savedInstanceState.getString(KEY_REFERENCE);

        if(stringRef == null) {
            return;
        }

        mStorageReference = FirebaseStorage.getInstance().getReferenceFromUrl(stringRef);
        List<UploadTask> uploadTasks = mStorageReference.getActiveUploadTasks();

        if(uploadTasks.size() > 0) {
            UploadTask uploadTask = uploadTasks.get(0);

            uploadTask.addOnSuccessListener(activity, mOnUploadSuccessListener);
        }

        List<FileDownloadTask> downloadTasks = mStorageReference.getActiveDownloadTasks();

        if(downloadTasks.size() > 0) {
            FileDownloadTask downloadTask = downloadTasks.get(0);

            downloadTask.addOnSuccessListener(activity, new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    if(mOnDownloadSuccessListener != null) {
                        mOnDownloadSuccessListener.onSuccess(null);
                    }
                }
            });
        }
    }
}
