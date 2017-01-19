package com.example.teodora.employeetaskmanager.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.teodora.employeetaskmanager.Activities.AssignTaskActivity;
import com.example.teodora.employeetaskmanager.R;

public class AssignedTasksFragment extends Fragment {

    private FloatingActionButton fab;

    public AssignedTasksFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_assigned_tasks, container, false);



        // Floating Action Button Click Listener
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getContext(), AssignTaskActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

}