package com.example.teodora.employeetaskmanager.Other;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.teodora.employeetaskmanager.Models.TaskModel;
import com.example.teodora.employeetaskmanager.R;
import com.github.lzyzsd.circleprogress.DonutProgress;
import java.util.List;

public class RecyclerViewHolders extends RecyclerView.ViewHolder{
    private static final String TAG = RecyclerViewHolders.class.getSimpleName();
    public TextView taskName, taskDueDate;
    public LinearLayout taskPriority;
    public DonutProgress taskPercentage;
    private List<TaskModel> taskObject;

    public RecyclerViewHolders(final View itemView, final List<TaskModel> taskObject) {
        super(itemView);
        this.taskObject = taskObject;
        taskPriority = (LinearLayout) itemView.findViewById(R.id.taskPriority);
        taskName = (TextView) itemView.findViewById(R.id.taskName);
        taskDueDate = (TextView) itemView.findViewById(R.id.taskDueDate);
        taskPercentage = (DonutProgress) itemView.findViewById(R.id.taskPercentage);

    }
}