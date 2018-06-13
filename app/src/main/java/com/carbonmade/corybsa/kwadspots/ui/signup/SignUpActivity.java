package com.carbonmade.corybsa.kwadspots.ui.signup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.carbonmade.corybsa.kwadspots.App;
import com.carbonmade.corybsa.kwadspots.R;
import com.carbonmade.corybsa.kwadspots.ui.main.MainActivity;
import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUpActivity extends AppCompatActivity implements SignUpContract.View {
    @BindView(R.id.email) EditText mEmail;
    @BindView(R.id.password) EditText mPassword;

    @Inject
    FirebaseAuth mAuth;

    private SignUpPresenter mPresenter;

    @OnClick(R.id.signUp)
    void onSignUpClick(View view) {
        mPresenter.onSignUpClicked(mEmail.getText().toString(), mPassword.getText().toString());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
        ((App)getApplication()).getNetworkComponent().inject(this);

        mPresenter = new SignUpPresenter(this);
    }

    @Override
    public void onAccountCreated() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onAccountFailed(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public FirebaseAuth getAuth() {
        return mAuth;
    }
}
