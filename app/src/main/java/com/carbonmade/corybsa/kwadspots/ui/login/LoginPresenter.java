package com.carbonmade.corybsa.kwadspots.ui.login;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginPresenter implements LoginContract.Presenter {
    private LoginActivity mActivity;
    private FirebaseAuth mAuth;

    LoginPresenter(LoginActivity activity) {
        mActivity = activity;
        mAuth = mActivity.getAuth();

        checkUserLoggedIn();
    }

    @Override
    public void onResume() {
        checkUserLoggedIn();
    }

    private void checkUserLoggedIn() {
        FirebaseUser user = mAuth.getCurrentUser();

        if(user != null) {
            mActivity.onLoginSuccess();
        }
    }

    @Override
    public void onLoginClicked(String email, String password) {
        if(email.isEmpty()) {
            mActivity.onLoginFailure("If you want to get in, you're going to have to show some credentials.");
            return;
        }

        if(password.isEmpty()) {
            mActivity.onLoginFailure("If you want to get in, you're going to have to show some credentials.");
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            mActivity.onLoginSuccess();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mActivity.onLoginFailure(e.getLocalizedMessage());
                    }
                });
    }
}
