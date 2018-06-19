package com.carbonmade.corybsa.kwadspots.ui.login;

import android.support.annotation.NonNull;

import com.carbonmade.corybsa.kwadspots.di.ActivityScoped;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import javax.inject.Inject;

@ActivityScoped
final public class LoginPresenter implements LoginContract.Presenter {
    private LoginContract.View mView;
    private FirebaseAuth mAuth;

    @Inject
    LoginPresenter(FirebaseAuth auth) {
        mAuth = auth;
    }

    @Override
    public void onResume() {
        checkUserLoggedIn();
    }

    private void checkUserLoggedIn() {
        FirebaseUser user = mAuth.getCurrentUser();

        if(user != null) {
            mView.onLoginSuccess();
        }
    }

    @Override
    public void onLoginClicked(String email, String password) {
        if(email.isEmpty()) {
            mView.onLoginFailure("If you want to get in, you're going to have to show some credentials.");
            return;
        }

        if(password.isEmpty()) {
            mView.onLoginFailure("If you want to get in, you're going to have to show some credentials.");
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            mView.onLoginSuccess();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mView.onLoginFailure(e.getLocalizedMessage());
                    }
                });
    }

    @Override
    public void takeView(LoginContract.View view) {
        mView = view;
        checkUserLoggedIn();
    }

    @Override
    public void dropView() {
        mView = null;
    }
}
