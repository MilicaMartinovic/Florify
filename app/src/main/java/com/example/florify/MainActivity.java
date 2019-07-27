package com.example.florify;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private Session session;
    private ImageButton magnifier;
    private TextView appName;
    private AutoCompleteTextView autoCompleteTextView;
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
        //-------------------------------------


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
//
//                    search_mode = false;
                    autoCompleteTextView.clearFocus();
                    autoCompleteTextView.setText("");
                }
            }
        });

    }
}
