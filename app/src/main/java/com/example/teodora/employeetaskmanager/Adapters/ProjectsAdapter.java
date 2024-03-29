package com.example.teodora.employeetaskmanager.Adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.teodora.employeetaskmanager.R;
import java.util.List;

public class ProjectsAdapter extends RecyclerView.Adapter<ProjectsAdapter.MyViewHolder> {

    private List<String> taskModelList;
    private static final String TAG = ProjectsAdapter.class.getSimpleName();
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView projectNameTextView;
        public MyViewHolder(View view) {
            super(view);
            projectNameTextView = (TextView) view.findViewById(R.id.projectName);
        }
    }

    public ProjectsAdapter(List taskModelList) {
        this.taskModelList = taskModelList;
    }

    @Override
    public ProjectsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.project_row_layot, parent, false);

        return new ProjectsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ProjectsAdapter.MyViewHolder holder, int position) {
        String projectName = taskModelList.get(position);
        holder.projectNameTextView.setText(projectName);
    }

    @Override
    public int getItemCount() {
        return taskModelList.size();
    }
}
