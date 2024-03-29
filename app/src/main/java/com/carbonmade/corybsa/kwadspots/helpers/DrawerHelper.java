package com.carbonmade.corybsa.kwadspots.helpers;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.carbonmade.corybsa.kwadspots.R;
import com.carbonmade.corybsa.kwadspots.ui.login.LoginActivity;
import com.carbonmade.corybsa.kwadspots.ui.settings.SettingsActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

/**
 * A class to help with tedious Drawer stuff.
 */
public class DrawerHelper {
    private FirebaseAuth mAuth;
    private Activity mActivity;

    public DrawerHelper(Activity activity, FirebaseAuth auth) {
        mAuth = auth;
        mActivity = activity;
    }

    /**
     * Populates the {@code navigationView} with the header image, profile image and log out button.
     * Also sets up click handlers for the items in the {@code navigationView}.
     *
     * @param navigationView the view for the navigation
     * @param drawerLayout the layout for the drawer
     */
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
                .load(R.drawable.ic_ks_transparent)
                .resize(128, 128)
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

    /**
     * Logs out the user and clears all activities from the history stack.
     */
    private void logOutClicked() {
        mAuth.signOut();
        Intent intent = new Intent(mActivity, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        mActivity.startActivity(intent);
    }
}
