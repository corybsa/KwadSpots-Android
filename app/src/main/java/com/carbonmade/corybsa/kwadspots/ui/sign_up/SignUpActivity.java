package com.carbonmade.corybsa.kwadspots.ui.sign_up;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
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
    @BindView(R.id.toolbar) Toolbar mToolbar;

    @Inject FirebaseAuth mAuth;
    @Inject SignUpPresenter mPresenter;

    @OnClick(R.id.sign_up)
    void onSignUpClick() {
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

        setSupportActionBar(mToolbar);

        ActionBar actionBar = getSupportActionBar();

        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Create Account");
        }

        mPresenter.takeView(this);

        mPasswordVerify.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if(keyEvent.getAction() == KeyEvent.ACTION_UP) {
                    switch(keyCode) {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            onSignUpClick();
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
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
