package com.example.teodora.employeetaskmanager.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.example.teodora.employeetaskmanager.Models.TaskModel;
import com.example.teodora.employeetaskmanager.R;

public class TaskDetailsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView textView1, textView2, textView3, textView4, textView5, textView6, textView7;
    private String priority;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);

        // Adding Toolbar to AssignTask screen
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Assign task");
        setSupportActionBar(toolbar);

        TaskModel taskModel = (TaskModel)getIntent().getSerializableExtra("taskDetails");

        String color = taskModel.getTaskPriority();
        if (color.equals(R.color.highPriorityTask)) priority="High";
        else if (color.equals(R.color.mediumPriorityTask)) priority="Medium";
        else if (color.equals(R.color.lowPriorityTask)) priority="Low";


        textView1 = (TextView) findViewById(R.id.txt_assignee);
        textView2 = (TextView) findViewById(R.id.txt_assignedBy);
        textView3 = (TextView) findViewById(R.id.txt_time);
        textView4 = (TextView) findViewById(R.id.txt_project);
        textView5 = (TextView) findViewById(R.id.txt_priority);
        textView6 = (TextView) findViewById(R.id.txt_description);
        textView7 = (TextView) findViewById(R.id.txt_location);

        textView1.setText(" " + taskModel.getTaskAssignee() + "blablerogjoaeijro iejroaiegj eorjg ");
        textView2.setText(" " + taskModel.getTaskAssignedBy() + "blablerogjoaeijro iejroaiegj eorjg ");
        textView3.setText(" " + taskModel.getTaskDueDate());
        textView4.setText(" " + taskModel.getTaskProject()+ "blablerogjoaeijro");
        textView5.setText(priority);
        textView6.setText(taskModel.getTaskDescription());
        textView7.setText(taskModel.getTaskLocation());


    }
}
