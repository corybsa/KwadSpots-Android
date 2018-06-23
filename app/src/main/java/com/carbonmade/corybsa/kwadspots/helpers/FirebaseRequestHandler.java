package com.carbonmade.corybsa.kwadspots.helpers;

import android.graphics.BitmapFactory;

import com.google.android.gms.tasks.Tasks;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StreamDownloadTask;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Request;
import com.squareup.picasso.RequestHandler;

import java.io.IOException;
import java.io.InputStream;

import javax.inject.Inject;

public class FirebaseRequestHandler extends RequestHandler {
    private static final String SCHEME_FIREBASE_STORAGE = "gs";
    private FirebaseStorage mFirebaseStorage;

    @Inject
    public FirebaseRequestHandler(FirebaseStorage firebaseStorage) {
        mFirebaseStorage = firebaseStorage;
    }

    @Override
    public boolean canHandleRequest(Request data) {
        String scheme = data.uri.getScheme();
        return (SCHEME_FIREBASE_STORAGE.equals(scheme));
    }

    @Override
    public Result load(Request request, int networkPolicy) throws IOException {
        StorageReference ref = mFirebaseStorage.getReferenceFromUrl(request.uri.toString());
        StreamDownloadTask streamTask;
        InputStream inputStream;
        streamTask = ref.getStream();

        try {
            inputStream = Tasks.await(streamTask).getStream();
            return new Result(BitmapFactory.decodeStream(inputStream), Picasso.LoadedFrom.NETWORK);
        } catch(Exception e) {
            throw new IOException();
        }
    }
}
