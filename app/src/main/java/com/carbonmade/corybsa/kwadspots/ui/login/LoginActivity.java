package com.carbonmade.corybsa.kwadspots.ui.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.carbonmade.corybsa.kwadspots.App;
import com.carbonmade.corybsa.kwadspots.ui.main.MainActivity;
import com.carbonmade.corybsa.kwadspots.R;
import com.carbonmade.corybsa.kwadspots.ui.signup.SignUpActivity;
import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements LoginContract.View {
    private static final String KEY_EMAIL = "Email";
    private static final String KEY_PASSWORD = "Password";

    @BindView(R.id.email) EditText mEmail;
    @BindView(R.id.password) EditText mPassword;
    @BindView(R.id.login) Button mLogin;
    @BindView(R.id.signUp) TextView mSignUp;

    @Inject SharedPreferences mSharedPreferences;
    @Inject FirebaseAuth mAuth;

    private LoginPresenter mPresenter;

    @OnClick(R.id.login)
    void onLoginClick(View view) {
        mPresenter.onLoginClicked(mEmail.getText().toString(), mPassword.getText().toString());
    }

    @OnClick(R.id.signUp)
    void onSignUpClick(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        ((App)getApplication()).getNetworkComponent().inject(this);

        mPresenter = new LoginPresenter(this);
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
        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        finish();
        startActivity(intent);
    }

    @Override
    public void onLoginFailure(String message) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("Ok", null)
                .create()
                .show();
    }

    public FirebaseAuth getAuth() {
        return mAuth;
    }
}
