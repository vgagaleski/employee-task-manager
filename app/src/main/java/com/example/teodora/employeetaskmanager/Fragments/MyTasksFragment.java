package com.example.teodora.employeetaskmanager.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.teodora.employeetaskmanager.Activities.TaskDetailsActivity;
import com.example.teodora.employeetaskmanager.Adapters.TasksRecyclerViewAdapter;
import com.example.teodora.employeetaskmanager.Models.TaskModel;
import com.example.teodora.employeetaskmanager.Other.RecyclerTouchListener;
import com.example.teodora.employeetaskmanager.R;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyTasksFragment extends Fragment  { //implements SwipeRefreshLayout.OnRefreshListener


    private RecyclerView recyclerView;
//    private SwipeRefreshLayout swipeRefreshLayout;
    private ArrayList<TaskModel> tasksList = new ArrayList<>();


    private DatabaseReference mDatabaseTasks;
    private FirebaseAuth mAuth;

    private String currentUserNameId;

    public MyTasksFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        //Search for the exact user that is logged in
        mDatabaseTasks= FirebaseDatabase.getInstance().getReference().child("Tasks");
        currentUserNameId = mAuth.getCurrentUser().getUid();
        mDatabaseTasks.keepSynced(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_tasks, container, false);

        if (view.getVisibility() == View.VISIBLE) {
            Toast.makeText(getContext(),"Helllooooo", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getContext(),"Goodbye", Toast.LENGTH_LONG).show();
        }

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(view.getContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

//        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
//        swipeRefreshLayout.setOnRefreshListener(this);

        fetchData();

//        swipeRefreshLayout.post(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        swipeRefreshLayout.setRefreshing(true);
//                                        fetchData();
//                                    }
//                                }
//        );



        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {


                Intent taskDetailsIntent = new Intent(getContext(),TaskDetailsActivity.class);
                taskDetailsIntent.putExtra("taskDetails",tasksList.get(position));
                startActivity(taskDetailsIntent);
                Toast.makeText(getContext(), tasksList.get(position).getTaskAssigneeId() + " is selected!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {
                Toast.makeText(getContext(), "LONG CLICK is selected!", Toast.LENGTH_SHORT).show();
            }
        }));


        return view;
    }

//    @Override
//    public void onRefresh() {
//        fetchData();
//    }

    private void fetchData() {

//        Toast.makeText(getContext(), "vo fetch data", Toast.LENGTH_LONG).show();
        if (checkInternetConnection()){

            Query getTasksQuery = mDatabaseTasks.orderByChild("taskAssigneeId").equalTo(currentUserNameId);

            getTasksQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    tasksList.clear();
                    int childrenCount = (int) dataSnapshot.getChildrenCount();
                    Log.e("DatabaseCount " ,"" + dataSnapshot.getChildrenCount());

                    for (DataSnapshot tasksSnapshot : dataSnapshot.getChildren()){

                        TaskModel taskModel = tasksSnapshot.getValue(TaskModel.class);
                        tasksList.add(taskModel);
                        Log.v("Added to tasksList: ", "tasksAssignee " + taskModel.getTaskAssignee());

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                    Log.e("Database Error: " ,"No databaseSnapshot caused by" + databaseError);
//                    swipeRefreshLayout.setRefreshing(false);

                }
            });

            TasksRecyclerViewAdapter tasksRecyclerViewAdapter = new TasksRecyclerViewAdapter(tasksList);
//            swipeRefreshLayout.setRefreshing(false);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(tasksRecyclerViewAdapter);

        }
        else
            Toast.makeText(getContext(), "No Internet connection", Toast.LENGTH_LONG).show();
//        swipeRefreshLayout.setRefreshing(false);
    }



    public boolean checkInternetConnection() {
        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();
        return (activeNetworkInfo != null && activeNetworkInfo.isAvailable() && activeNetworkInfo.isConnected());
    }

}
