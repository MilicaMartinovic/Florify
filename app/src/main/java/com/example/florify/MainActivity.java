package com.example.florify;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.example.florify.adapters.SectionPageAdapter;
import com.example.florify.fragments.FeedFragment;
import com.example.florify.fragments.MapFragment;
import com.example.florify.fragments.ProfileFragment;
import com.example.florify.fragments.SettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private Session session;
    private ImageButton magnifier;
    private TextView appName;
    private AutoCompleteTextView autoCompleteTextView;
    private BottomNavigationView bottomNavigationView;
    private FeedFragment feedFragment;
    private MapFragment mapFragment;
    private ProfileFragment profileFragment;
    private SettingsFragment settingsFragment;
    private ViewPager viewPager;
    private SectionPageAdapter mSectionPageAdapter;
    private MenuItem prevMenuItem;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, 1, 0);
        session = new Session(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);

        LayoutInflater inflator = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.custom_app_bar, null);

        actionBar.setCustomView(v);

        magnifier = findViewById(R.id.btnSearchBar);
        appName = findViewById(R.id.app_name);
        autoCompleteTextView = findViewById(R.id.search_auto_complete);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        autoCompleteTextView.setVisibility(View.INVISIBLE);
        appName.setVisibility(View.VISIBLE);
        magnifier.setImageResource(R.drawable.ic_search);

        autoCompleteTextView.clearFocus();
        autoCompleteTextView.setText("");

        magnifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(appName.getVisibility() != View.INVISIBLE) {

                    appName.setVisibility(View.INVISIBLE);
                    autoCompleteTextView.setVisibility(View.VISIBLE);
                    magnifier.setImageResource(R.drawable.ic_close);
                }
                else {

                    autoCompleteTextView.setVisibility(View.INVISIBLE);
                    appName.setVisibility(View.VISIBLE);
                    magnifier.setImageResource(R.drawable.ic_search);

                    autoCompleteTextView.clearFocus();
                    autoCompleteTextView.setText("");
                }
            }
        });

        mSectionPageAdapter = new SectionPageAdapter(getSupportFragmentManager());
        viewPager = findViewById(R.id.viewPager); //Init Viewpager
        setupViewPager(viewPager); //Setup Fragment
        viewPager.setCurrentItem(0); //Set Currrent Item When Activity Start
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                }
                else
                {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                switch (position) {
                    case 0 : {
                        bottomNavigationView.getMenu().getItem(0).setChecked(true);
                        prevMenuItem = bottomNavigationView.getMenu().getItem(0);
                        break;
                    }
                    case 1 : {
                        bottomNavigationView
                                .getMenu()
                                .getItem(1)
                                .setChecked(true);
                        prevMenuItem = bottomNavigationView.getMenu().getItem(1);
                        break;
                    }
                    case 2 : {
                        bottomNavigationView.getMenu().getItem(3).setChecked(true);
                        prevMenuItem = bottomNavigationView.getMenu().getItem(3);
                        break;
                    }
                    case 3 : {
                        bottomNavigationView.getMenu().getItem(4).setChecked(true);
                        prevMenuItem = bottomNavigationView.getMenu().getItem(4);
                        break;
                    }
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_home : {
                        viewPager.setCurrentItem(0);
                        return true;
                    }
                    case R.id.nav_map : {
                        viewPager.setCurrentItem(1);
                        return true;
                    }
                    case R.id.nav_profile : {
                       viewPager.setCurrentItem(2);
                        return true;
                    }
                    case R.id.nav_settings : {
                        viewPager.setCurrentItem(3);
                        return true;
                    }
                }
                return false;
            }
        });

    }


    private void setupViewPager(ViewPager viewPager)
    {
        mSectionPageAdapter = new SectionPageAdapter(getSupportFragmentManager());
        feedFragment=new FeedFragment();
        mapFragment=new MapFragment();
        profileFragment = new ProfileFragment();
        settingsFragment = new SettingsFragment();
        mSectionPageAdapter.addFragment(feedFragment, "FEED");
        mSectionPageAdapter.addFragment(mapFragment, "MAP");
        mSectionPageAdapter.addFragment(profileFragment, "PROFILE");
        mSectionPageAdapter.addFragment(settingsFragment, "SETTINGS");
        viewPager.setAdapter(mSectionPageAdapter);
    }
}
