package com.example.teodora.employeetaskmanager.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.teodora.employeetaskmanager.R;

public class PrivacyPolicyActivity extends AppCompatActivity {
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        // Adding Toolbar to AssignTask screen
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbar.setTitle("Privacy Policy");
//        setSupportActionBar(toolbar);
    }
}
