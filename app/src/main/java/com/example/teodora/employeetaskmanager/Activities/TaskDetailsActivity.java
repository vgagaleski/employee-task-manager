package com.example.teodora.employeetaskmanager.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.teodora.employeetaskmanager.R;

public class TaskDetailsActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);

        // Adding Toolbar to AssignTask screen
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Assign task");
        setSupportActionBar(toolbar);
    }
}
