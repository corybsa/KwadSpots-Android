package com.carbonmade.corybsa.kwadspots.ui.sign_up;

import android.support.annotation.NonNull;

import com.carbonmade.corybsa.kwadspots.di.ActivityScoped;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Inject;

@ActivityScoped
public class SignUpPresenter implements SignUpContract.Presenter {
    private SignUpContract.View mView;
    private FirebaseAuth mAuth;

    @Inject
    SignUpPresenter(FirebaseAuth auth) {
        mAuth = auth;
    }

    @Override
    public void onSignUpClicked(String email, String password, String passwordVerify) {
        if(email.isEmpty()) {
            mView.onAccountFailed("You need to provide an email.");
            return;
        }

        if(password.isEmpty() || password.length() < 8) {
            mView.onAccountFailed("You need to provide a password that is at least 8 characters long.");
            return;
        }

        if(!password.equals(passwordVerify)) {
            mView.onAccountFailed("Passwords did not match.");
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            mView.onAccountCreated();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mView.onAccountFailed(e.getMessage());
                    }
                }
        );
    }

    @Override
    public void takeView(SignUpContract.View view) {
        mView = view;
    }

    @Override
    public void dropView() {
        mView = null;
    }
}
