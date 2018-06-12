package com.carbonmade.corybsa.kwadspots.ui.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.carbonmade.corybsa.kwadspots.App;
import com.carbonmade.corybsa.kwadspots.ui.main.MainActivity;
import com.carbonmade.corybsa.kwadspots.R;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements LoginContract.View {
    @BindView(R.id.username) EditText mUsername;
    @BindView(R.id.password) EditText mPassword;
    @BindView(R.id.login) Button mLogin;

    @Inject
    SharedPreferences mSharedPreferences;

    private LoginPresenter mPresenter;

    @OnClick(R.id.login)
    void onLoginClick(View view) {
        mPresenter.onLoginClicked(mUsername.getText().toString(), mPassword.getText().toString());
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
    public void onLoginSuccess() {
        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        finish();
        startActivity(intent);
    }

    @Override
    public void onLoginFailure(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
