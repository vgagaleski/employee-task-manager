package com.example.teodora.employeetaskmanager.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.teodora.employeetaskmanager.Activities.AssignTaskActivity;
import com.example.teodora.employeetaskmanager.R;
import com.github.lzyzsd.circleprogress.CircleProgress;
import com.github.lzyzsd.circleprogress.DonutProgress;

public class MyTasksFragment extends Fragment {




    public MyTasksFragment() {
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
        View view = inflater.inflate(R.layout.fragment_my_tasks, container, false);

//        DonutProgress circleProgress = (DonutProgress) view.findViewById(R.id.donut_progress);
//        circleProgress.setPivotX(55);
//        circleProgress.setPivotY(55);




        return view;
    }

}
