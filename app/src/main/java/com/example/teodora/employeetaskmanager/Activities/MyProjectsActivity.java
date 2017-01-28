package com.example.teodora.employeetaskmanager.Activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.teodora.employeetaskmanager.Adapters.ProjectsAdapter;
import com.example.teodora.employeetaskmanager.Adapters.TasksRecyclerViewAdapter;
import com.example.teodora.employeetaskmanager.Models.TaskModel;
import com.example.teodora.employeetaskmanager.Other.FragmentLifecycle;
import com.example.teodora.employeetaskmanager.Other.RecyclerTouchListener;
import com.example.teodora.employeetaskmanager.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyProjectsActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, FragmentLifecycle {
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    private TextView projectNameTextView;

    private DatabaseReference mDatabaseTasks;
    private FirebaseAuth mAuth;
    private boolean isFirstTime = true;

    private String currentUserNameId;
    private String currentDate;

    private List<String> projectNames = new ArrayList<String>();

    private ProjectsAdapter projectsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_projects);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Projects");
        setSupportActionBar(toolbar);


        mAuth = FirebaseAuth.getInstance();
        //Search for the exact user that is logged in
        mDatabaseTasks= FirebaseDatabase.getInstance().getReference().child("Tasks");
        currentUserNameId = mAuth.getCurrentUser().getUid();
        mDatabaseTasks.keepSynced(true);


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);


        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);
                                        fetchData();
                                    }
                                }
        );




        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

            }

            @Override
            public void onLongClick(View view, int position) {
                Toast.makeText(getApplicationContext(), "LONG CLICK is selected!", Toast.LENGTH_SHORT).show();
            }
        }));

        fetchData();

    }

    @Override
    public void onRefresh() {
        fetchData();
    }

    @Override
    public void onPauseFragment() {

    }

    @Override
    public void onResumeFragment() {
        fetchData();
    }

    private void fetchData() {
        isFirstTime = false;

//        Toast.makeText(getContext(), "vo fetch data", Toast.LENGTH_LONG).show();
        if (checkInternetConnection()){

            Query getTasksQuery = mDatabaseTasks.orderByChild("taskAssigneeId").equalTo(currentUserNameId);

            getTasksQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    projectNames.clear();
                    int childrenCount = (int) dataSnapshot.getChildrenCount();
                    Log.e("DatabaseCount " ,"" + dataSnapshot.getChildrenCount());
                    Log.e("dataSnapshot " ,"" + dataSnapshot);

                    for (DataSnapshot tasksSnapshot : dataSnapshot.getChildren()){
                        Log.e("tasksSnapshot " ,"" + tasksSnapshot);


                        TaskModel taskModel = tasksSnapshot.getValue(TaskModel.class);

                        projectNames.add(taskModel.getTaskProject());


                    }
                    projectsAdapter = new ProjectsAdapter(projectNames);
                    swipeRefreshLayout.setRefreshing(false);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    recyclerView.setAdapter(projectsAdapter);


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                    Log.e("Database Error: " ,"No databaseSnapshot caused by" + databaseError);
                    swipeRefreshLayout.setRefreshing(false);

                }
            });


        }
        else
            Toast.makeText(getApplicationContext(), "No Internet connection", Toast.LENGTH_LONG).show();
        swipeRefreshLayout.setRefreshing(false);
    }



    public boolean checkInternetConnection() {
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();
        return (activeNetworkInfo != null && activeNetworkInfo.isAvailable() && activeNetworkInfo.isConnected());
    }


}
