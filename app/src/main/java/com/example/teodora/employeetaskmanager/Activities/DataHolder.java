package com.example.teodora.employeetaskmanager.Activities;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.teodora.employeetaskmanager.R;
import com.github.lzyzsd.circleprogress.DonutProgress;

public class DataHolder extends RecyclerView.ViewHolder{
    private static final String TAG = DataHolder.class.getSimpleName();
    public TextView taskName, taskDueDate;
    public LinearLayout taskPriority;
    public DonutProgress taskPercentage;

    public DataHolder(View itemView) {
        super(itemView);
        taskPriority = (LinearLayout) itemView.findViewById(R.id.taskPriority);
        taskName = (TextView) itemView.findViewById(R.id.taskName);
        taskDueDate = (TextView) itemView.findViewById(R.id.taskDueDate);
        taskPercentage = (DonutProgress) itemView.findViewById(R.id.taskPercentage);
    }
}