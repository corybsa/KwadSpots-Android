package com.carbonmade.corybsa.kwadspots.ui.spot_info;

import android.support.annotation.NonNull;

import com.carbonmade.corybsa.kwadspots.di.ActivityScoped;
import com.carbonmade.corybsa.kwadspots.services.SpotService;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.QuerySnapshot;

import javax.inject.Inject;

@ActivityScoped
final public class SpotInfoPresenter implements SpotInfoContract.Presenter {
    private SpotInfoContract.View mView;

    @Inject SpotService mSpotService;

    @Inject
    SpotInfoPresenter() {
    }

    @Override
    public void loadComments(String spotId) {
        mSpotService.getSpotComments(spotId)
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        // TODO: tell the view the comments are loaded
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // TODO: tell the view there was an error
                    }
                });
    }

    @Override
    public void takeView(SpotInfoContract.View view) {
        mView = view;
    }

    @Override
    public void dropView() {
        mView = null;
    }
}
