package com.example.teodora.employeetaskmanager.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.teodora.employeetaskmanager.Models.TaskModel;
import com.example.teodora.employeetaskmanager.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

public class TaskDetailsActivity2 extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView textView1, textView2, textView3, textView4, textView5, textView6, textView7, textView8, lblProgress;
    private String priority;
    private LinearLayout statusLayout;
    private DiscreteSeekBar discreteSeekBar;

    private int status;
    DatabaseReference databaseTasks;
    private String taskID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);

        // Adding Toolbar to AssignTask screen
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Task details");
        setSupportActionBar(toolbar);

        TaskModel taskModel = (TaskModel)getIntent().getSerializableExtra("taskDetails");
//        taskID = (String) getIntent().getStringExtra("taskID");

        databaseTasks = FirebaseDatabase.getInstance().getReference("Tasks");

        String color = taskModel.getTaskPriority();
        if (color.equals("#e91e63"))priority="High";
        else if (color.equals("#66bb6a")) priority="Medium";
        else if (color.equals("#e2e619")) priority="Low";

        textView1 = (TextView) findViewById(R.id.txt_assignee);
        textView2 = (TextView) findViewById(R.id.txt_assignedBy);
        textView3 = (TextView) findViewById(R.id.txt_time);
        textView4 = (TextView) findViewById(R.id.txt_project);
        textView5 = (TextView) findViewById(R.id.txt_priority);
        textView6 = (TextView) findViewById(R.id.txt_description);
        textView7 = (TextView) findViewById(R.id.txt_location);
        textView8 = (TextView) findViewById(R.id.txt_status);

        textView1.setText(" " + taskModel.getTaskAssignee());
        textView2.setText(" " + taskModel.getTaskAssignedBy());
        textView3.setText(" " + taskModel.getTaskDueDate());
        textView4.setText(" " + taskModel.getTaskProject());
        textView5.setText(priority);
        textView6.setText(taskModel.getTaskDescription());
        textView7.setText(taskModel.getTaskLocation());
        textView8.setText(taskModel.getTaskPercentage());
    }
}

