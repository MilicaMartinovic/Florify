package com.example.florify;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.example.florify.adapters.SectionPageAdapter;
import com.example.florify.dialogs.FiltersDialog;
import com.example.florify.dialogs.OnFiltersSubmitted;
import com.example.florify.fragments.FeedFragment;
import com.example.florify.fragments.MapFragment;
import com.example.florify.fragments.ProfileFragment;
import com.example.florify.fragments.SettingsFragment;
import com.example.florify.helpers.FileHelper;
import com.example.florify.helpers.MapResolver;
import com.example.florify.models.PostFilters;
import com.example.florify.services.BackgroundService;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements OnFiltersSubmitted {
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
    private FloatingActionButton fab;
    private Button btnMenuConnections, btnMenuConnectionRequests,
            btnMenuPendingConnections, btnMenuScoreboard, btnMenuLogout;
    private FileHelper fileHelper;

    private FiltersDialog filtersDialog;
    private ActionBar actionBar;

    public int LOCATION_REQUEST_CODE = 15;


    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                1,
                0
        );

        session = new Session(this);
        fileHelper = new FileHelper();

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);

        LayoutInflater inflator = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.custom_app_bar, null);

        actionBar.setCustomView(v);

        magnifier = findViewById(R.id.btnSearchBar);
        appName = findViewById(R.id.app_name);
        autoCompleteTextView = findViewById(R.id.search_auto_complete);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        fab = findViewById(R.id.fab);

        autoCompleteTextView.setVisibility(View.INVISIBLE);
        appName.setVisibility(View.VISIBLE);
        magnifier.setImageResource(R.drawable.ic_search);

        autoCompleteTextView.clearFocus();
        autoCompleteTextView.setText("");

        btnMenuConnections = findViewById(R.id.btnMenuConnections);
        btnMenuConnectionRequests = findViewById(R.id.btnMenuConnectionRequests);
        btnMenuPendingConnections = findViewById(R.id.btnMenuPendingRequests);
        btnMenuScoreboard = findViewById(R.id.btnMemiScoreoard);
        btnMenuLogout = findViewById(R.id.btnMenuLogout);

        setMenuClickListeners();
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

            //    fabFilter.setVisibility(View.INVISIBLE);
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
                        actionBar.hide();
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
                String title = menuItem.getTitle().toString();
                switch (title) {
                    case "home" : {
                        viewPager.setCurrentItem(0);
                        actionBar.show();
                        return true;
                    }
                    case "map" : {
                        actionBar.hide();
                        viewPager.setCurrentItem(1);
                        return true;
                    }
                    case "profile" : {
                        actionBar.hide();
                       viewPager.setCurrentItem(2);
                        return true;
                    }
                    case "settings" : {
                        viewPager.setCurrentItem(3);
                        actionBar.hide();
                        return true;

                    }
                }
                return false;
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermissions(new String[]{Manifest.permission.CAMERA},1);
            }
        });


        if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}
                        , LOCATION_REQUEST_CODE);
            }
            return;
        }

        startService(new Intent(this, BackgroundService.class));
    }

    private void setMenuClickListeners() {
        btnMenuConnections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btnMenuPendingConnections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btnMenuConnectionRequests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btnMenuScoreboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btnMenuLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = fileHelper.createImageFile(this);
                    } catch (IOException ex) {
                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        Uri photoURI = FileProvider.getUriForFile(this,
                                "com.example.florify",
                                photoFile);
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(cameraIntent, 0);
                    }

                } else {
                    Toast.makeText(
                            this,
                            "camera permission denied",
                            Toast.LENGTH_LONG
                    ).show();
                }

            }
        }
        else if(requestCode == LOCATION_REQUEST_CODE)
        {
            MapResolver mapResolver = new MapResolver(this);
            mapResolver.initListener();
            mapResolver.sendLocationRequest();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==0 && resultCode==RESULT_OK) {
            Bitmap bitmap;
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();

            File f = new File(fileHelper.getmCurrentPhotoPath());
            bmOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
            try {
                bitmap = BitmapFactory.decodeStream(new FileInputStream(f), null, bmOptions);
                fileHelper.setBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            };
            new Encode_image().execute();
        }
    }

    @Override
    public void OnFiltersSubmitCompleted(boolean plantNameEnabled,
                                         boolean radiusEnabled,
                                         boolean dateTimeRangeEnabled,
                                         PostFilters postFilters) {

    }

    public class Encode_image extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {

            Intent intent = new Intent(getApplicationContext(), PlantUploadActivity.class);
            intent.putExtra("path", fileHelper.getmCurrentPhotoPath());
            startActivity(intent);
            return null;
        }
    }
}
