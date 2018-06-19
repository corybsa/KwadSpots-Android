package com.carbonmade.corybsa.kwadspots.ui.password_reset;

import android.support.annotation.NonNull;

import com.carbonmade.corybsa.kwadspots.di.ActivityScoped;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Inject;

@ActivityScoped
final public class PasswordResetPresenter implements PasswordResetContract.Presenter {
    private PasswordResetContract.View mView;
    private FirebaseAuth mAuth;

    @Inject
    PasswordResetPresenter(FirebaseAuth auth) {
        mAuth = auth;
    }

    @Override
    public void onSubmitClicked(String email) {
        if(email.isEmpty()) {
            mView.onSubmitFailed("We can't send an email if you don't provide one.");
            return;
        }

        mAuth.sendPasswordResetEmail(email)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        mView.onSubmitSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mView.onSubmitFailed(e.getMessage());
                    }
                });
    }

    @Override
    public void takeView(PasswordResetContract.View view) {
        mView = view;
    }

    @Override
    public void dropView() {
        mView = null;
    }
}
