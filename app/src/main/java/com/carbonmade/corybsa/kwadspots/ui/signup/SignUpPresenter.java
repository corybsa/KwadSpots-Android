package com.carbonmade.corybsa.kwadspots.ui.signup;

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
    private SignUpContract.View mSignUpView;
    private FirebaseAuth mAuth;

    @Inject
    SignUpPresenter(FirebaseAuth auth) {
        mAuth = auth;
    }

    @Override
    public void onSignUpClicked(String email, String password, String passwordVerify) {
        if(email.isEmpty()) {
            mSignUpView.onAccountFailed("You need to provide an email.");
            return;
        }

        if(password.isEmpty() || password.length() < 8) {
            mSignUpView.onAccountFailed("You need to provide a password that is at least 8 characters long.");
            return;
        }

        if(!password.equals(passwordVerify)) {
            mSignUpView.onAccountFailed("Passwords did not match.");
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            mSignUpView.onAccountCreated();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mSignUpView.onAccountFailed(e.getMessage());
                    }
                }
        );
    }

    @Override
    public void takeView(SignUpContract.View view) {
        mSignUpView = view;
    }

    @Override
    public void dropView() {
        mSignUpView = null;
    }
}
