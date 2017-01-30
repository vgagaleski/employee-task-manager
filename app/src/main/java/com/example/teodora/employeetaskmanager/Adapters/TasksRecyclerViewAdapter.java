package com.example.teodora.employeetaskmanager.Adapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.teodora.employeetaskmanager.Models.TaskModel;
import com.example.teodora.employeetaskmanager.R;
import com.github.lzyzsd.circleprogress.DonutProgress;
import java.util.List;

public class TasksRecyclerViewAdapter extends RecyclerView.Adapter<TasksRecyclerViewAdapter.MyViewHolder> {

    private List<TaskModel> taskModelList;
    private static final String TAG = TasksRecyclerViewAdapter.class.getSimpleName();

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView taskName, taskDueDate;
        public LinearLayout taskPriority;
        public DonutProgress taskPercentage;

        public MyViewHolder(View view) {
            super(view);
            taskPriority = (LinearLayout) view.findViewById(R.id.taskPriority);
            taskName = (TextView) view.findViewById(R.id.taskName);
            taskDueDate = (TextView) view.findViewById(R.id.taskDueDate);
            taskPercentage = (DonutProgress) view.findViewById(R.id.taskPercentage);
        }
    }

    public TasksRecyclerViewAdapter(List<TaskModel> taskModelList) {
        this.taskModelList = taskModelList;
    }

    @Override
    public TasksRecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_row_layout, parent, false);

        return new TasksRecyclerViewAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        TaskModel taskModel = taskModelList.get(position);
        holder.taskPriority.setBackgroundColor(Color.parseColor(taskModel.getTaskPriority()));
        holder.taskName.setText(taskModel.getTaskName());
        holder.taskDueDate.setText(taskModel.getTaskDueDate());
        holder.taskPercentage.setProgress(Integer.parseInt(taskModel.getTaskPercentage()));
        holder.taskPercentage.setFinishedStrokeColor(Color.parseColor("#00BCD4"));
        holder.taskPercentage.setUnfinishedStrokeColor(Color.parseColor("#CCCCCC"));
        holder.taskPercentage.setFinishedStrokeWidth((float) 20.0);
        holder.taskPercentage.setTextColor(Color.parseColor("#00BCD4"));
        holder.taskPercentage.setUnfinishedStrokeWidth(20);
        holder.taskPercentage.setTextSize((float)30.0);
    }

    @Override
    public int getItemCount() {
        return taskModelList.size();
    }
}
