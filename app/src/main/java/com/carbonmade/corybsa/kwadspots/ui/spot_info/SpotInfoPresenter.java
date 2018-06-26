package com.carbonmade.corybsa.kwadspots.ui.spot_info;

import android.support.annotation.NonNull;

import com.carbonmade.corybsa.kwadspots.datamodels.SpotComment;
import com.carbonmade.corybsa.kwadspots.di.ActivityScoped;
import com.carbonmade.corybsa.kwadspots.services.SpotService;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.List;

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
                        List<SpotComment> comments = new ArrayList<>();

                        for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            comments.add(new SpotComment(documentSnapshot));
                        }

                        mView.loadComments(comments);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mView.showError(e.getMessage());
                    }
                });
    }

    @Override
    public void loadMore(String spotId) {
        // TODO: figure this out...
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
