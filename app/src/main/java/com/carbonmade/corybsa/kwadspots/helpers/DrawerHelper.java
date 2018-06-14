package com.carbonmade.corybsa.kwadspots.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.carbonmade.corybsa.kwadspots.R;
import com.carbonmade.corybsa.kwadspots.ui.login.LoginActivity;
import com.carbonmade.corybsa.kwadspots.ui.settings.SettingsActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class DrawerHelper {
    private FirebaseAuth mAuth;
    private Activity mActivity;

    public DrawerHelper(Activity activity, FirebaseAuth auth) {
        mAuth = auth;
        mActivity = activity;
    }

    public void setupDrawerContent(NavigationView navigationView, final DrawerLayout drawerLayout) {
        FirebaseUser user = mAuth.getCurrentUser();
        String headerText = user.getDisplayName();

        if(headerText != null) {
            headerText = user.getEmail();
        }

        View headerView = navigationView.getHeaderView(0);
        ImageView drawerHeaderProfilePic = headerView.findViewById(R.id.drawer_header_profile_pic);
        TextView drawerHeaderText = headerView.findViewById(R.id.drawer_header_text);
        Button logOutButton = navigationView.findViewById(R.id.log_out);

        drawerHeaderText.setText(headerText);

        Picasso.with(mActivity)
                .load(R.drawable.ks_orange)
                .transform(new CropCircleTransformation())
                .into(drawerHeaderProfilePic);

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        String activityName = mActivity.getClass().getSimpleName();
                        switch(item.getItemId()) {
                            case R.id.drawer_item_settings:
                                if(!activityName.equals(SettingsActivity.class.getSimpleName())) {
                                    Intent intent = new Intent(mActivity, SettingsActivity.class);
                                    mActivity.startActivity(intent);
                                }
                                break;
                            default:
                                break;
                        }

                        drawerLayout.closeDrawers();
                        return true;
                    }
                }
        );

        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOutClicked();;
            }
        });
    }

    private void logOutClicked() {
        mAuth.signOut();
        Intent intent = new Intent(mActivity, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        mActivity.startActivity(intent);
    }
}
