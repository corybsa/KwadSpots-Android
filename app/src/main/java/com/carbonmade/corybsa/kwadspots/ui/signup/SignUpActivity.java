package com.carbonmade.corybsa.kwadspots.ui.signup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;

import com.carbonmade.corybsa.kwadspots.R;
import com.carbonmade.corybsa.kwadspots.ui.main.MainActivity;
import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;

public class SignUpActivity extends DaggerAppCompatActivity implements SignUpContract.View {
    private static final String KEY_EMAIL = "Sign up email";
    private static final String KEY_PASSWORD = "Sign up password";
    private static final String KEY_PASSWORD_VERIFY = "Sign up password verify";

    @BindView(R.id.email) EditText mEmail;
    @BindView(R.id.password) EditText mPassword;
    @BindView(R.id.password_verify) EditText mPasswordVerify;

    @Inject FirebaseAuth mAuth;
    @Inject SignUpPresenter mPresenter;

    @OnClick(R.id.signUp)
    void onSignUpClick(View view) {
        mPresenter.onSignUpClicked(
                mEmail.getText().toString(),
                mPassword.getText().toString(),
                mPasswordVerify.getText().toString()
        );
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);

        mPresenter.takeView(this);
    }

    @Override
    public void onAccountCreated() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onAccountFailed(String message) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("Ok", null)
                .create()
                .show();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mEmail.setText(savedInstanceState.getString(KEY_EMAIL));
        mPassword.setText(savedInstanceState.getString(KEY_PASSWORD));
        mPasswordVerify.setText(savedInstanceState.getString(KEY_PASSWORD_VERIFY));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_EMAIL, mEmail.getText().toString());
        outState.putString(KEY_PASSWORD, mPassword.getText().toString());
        outState.putString(KEY_PASSWORD_VERIFY, mPasswordVerify.getText().toString());
    }
}
