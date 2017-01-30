package com.example.teodora.employeetaskmanager.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.teodora.employeetaskmanager.R;

public class AboutUsActivity extends AppCompatActivity {
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        // Adding Toolbar to About Us screen
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("About us");
        setSupportActionBar(toolbar);
    }
}
