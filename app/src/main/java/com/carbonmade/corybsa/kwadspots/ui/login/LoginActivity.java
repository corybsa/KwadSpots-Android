package com.carbonmade.corybsa.kwadspots.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.carbonmade.corybsa.kwadspots.ui.main.MainActivity;
import com.carbonmade.corybsa.kwadspots.R;
import com.carbonmade.corybsa.kwadspots.ui.password_reset.PasswordResetActivity;
import com.carbonmade.corybsa.kwadspots.ui.sign_up.SignUpActivity;
import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;

public class LoginActivity extends DaggerAppCompatActivity implements LoginContract.View {
    private static final String KEY_EMAIL = "Log in email";
    private static final String KEY_PASSWORD = "Log in password";

    @BindView(R.id.email) EditText mEmail;
    @BindView(R.id.password) EditText mPassword;
    @BindView(R.id.login) Button mLogin;
    @BindView(R.id.sign_up) TextView mSignUp;

    @Inject FirebaseAuth mAuth;
    @Inject LoginPresenter mPresenter;

    @OnClick(R.id.login)
    void onLoginClick() {
        mPresenter.onLoginClicked(mEmail.getText().toString(), mPassword.getText().toString());
    }

    @OnClick(R.id.sign_up)
    void onSignUpClick() {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.forgot_password)
    void onForgotClick() {
        Intent intent = new Intent(this, PasswordResetActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        mPresenter.takeView(this);

        mPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if(keyEvent.getAction() == KeyEvent.ACTION_UP) {
                    switch(keyCode) {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            onLoginClick();
                            return true;
                        default:
                            return false;
                    }
                }

                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mEmail.setText(savedInstanceState.getString(KEY_EMAIL));
        mPassword.setText(savedInstanceState.getString(KEY_PASSWORD));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_EMAIL, mEmail.getText().toString());
        outState.putString(KEY_PASSWORD, mPassword.getText().toString());
    }

    @Override
    public void onLoginSuccess() {
        mPresenter.dropView();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onLoginFailure(String message) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("Ok", null)
                .create()
                .show();
    }
}
