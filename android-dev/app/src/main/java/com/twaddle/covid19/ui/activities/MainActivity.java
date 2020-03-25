package com.twaddle.covid19.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.twaddle.covid19.R;
import com.twaddle.covid19.ui.fragments.Tab1;
import com.twaddle.covid19.ui.fragments.Tab2;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener( navigationItemSelectedListener );
        openFragment( new Tab1() );

        changeStatusBarColor();
    }
    private void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.navigation_tab1:
                            openFragment(new Tab1());
                            return true;
                        case R.id.navigation_tab2:
                            openFragment(new Tab2());
                            return true;
                    }
                    return false;
                }
            };

    private void changeStatusBarColor() {
        Window window = getWindow();
        window.addFlags( WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor( getColor( R.color.colorAccent ));
    }
}
