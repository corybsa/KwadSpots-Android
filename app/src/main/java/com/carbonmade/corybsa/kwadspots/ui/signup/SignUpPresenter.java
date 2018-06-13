package com.carbonmade.corybsa.kwadspots.ui.signup;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpPresenter implements SignUpContract.Presenter {
    private SignUpActivity mActivity;

    SignUpPresenter(SignUpActivity activity) {
        mActivity = activity;
    }

    @Override
    public void onSignUpClicked(String email, String password) {
        FirebaseAuth auth = mActivity.getAuth();
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            mActivity.onAccountCreated();
                        } else {
                            mActivity.onAccountFailed("Auth failed");
                        }
                    }
                });
    }
}
