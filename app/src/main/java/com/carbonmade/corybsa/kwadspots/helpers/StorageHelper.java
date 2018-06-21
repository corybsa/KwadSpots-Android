package com.carbonmade.corybsa.kwadspots.helpers;

import android.app.Activity;
import android.os.Bundle;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

    public StorageHelper(FirebaseStorage firebaseStorage) {
        mFirebaseStorage = firebaseStorage;
    }

    public UploadTask upload(String filename, byte[] data) {
        mStorageReference = mFirebaseStorage.getReference();
        return mStorageReference.child(String.format("images/%s", filename)).putBytes(data);
    }

    public Task<byte[]> download(String path) {
        mStorageReference = mFirebaseStorage.getReferenceFromUrl(path);
        return mStorageReference.getBytes(MEGABYTE * 50);
    }

    public void onSaveInstanceState(Bundle outState) {
        if(mStorageReference != null) {
            outState.putString(KEY_REFERENCE, mStorageReference.toString());
        }
    }

    public UploadTask restoreUploadState(final Activity activity, Bundle savedInstanceState) {
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

    public FileDownloadTask restoreDownloadState(final Activity activity, Bundle savedInstanceState) {
        List<FileDownloadTask> downloadTasks = mStorageReference.getActiveDownloadTasks();

        if(downloadTasks.size() > 0) {
            return downloadTasks.get(0);
        }

        return null;
    }
}
