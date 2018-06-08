package com.carbonmade.corybsa.kwadspots;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.navigation) BottomNavigationView mNavigationView;
    @BindView(R.id.main_content) LinearLayout mMainContentLayout;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent intent;

            switch(item.getItemId()) {
                case R.id.navigation_home:
                    loadLayout(R.layout.activity_main_home);
                    return true;
                case R.id.navigation_spots:
                    loadLayout(R.layout.activity_main_spots);
                    return true;
                case R.id.navigation_search:
                    loadLayout(R.layout.activity_main_search);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        loadLayout(R.layout.activity_main_home);
        mNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private void loadLayout(int layout) {
        mMainContentLayout.removeAllViews();
        View view = getLayoutInflater().inflate(layout, null);
        mMainContentLayout.addView(view);
    }
}
