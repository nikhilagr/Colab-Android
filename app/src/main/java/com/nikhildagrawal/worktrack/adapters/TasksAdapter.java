package com.nikhildagrawal.worktrack.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nikhildagrawal.worktrack.R;
import com.nikhildagrawal.worktrack.models.Task;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.TasksViewHolder> {


    private Context mContext;
    private List<Task> taskList;


    public TasksAdapter(Context context){
        mContext = context;
    }


    @NonNull
    @Override
    public TasksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_task_list_item,parent,false);

        TasksViewHolder holder = new TasksViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull TasksViewHolder holder, int position) {

        holder.mTitle.setText(taskList.get(position).getName());
    }

    @Override
    public int getItemCount() {

        if(taskList.isEmpty()){
            return 0;
        }
        return taskList.size();
    }

    public class TasksViewHolder extends RecyclerView.ViewHolder {

        TextView mTitle;
        public TasksViewHolder(@NonNull View itemView) {
            super(itemView);

            mTitle = itemView.findViewById(R.id.task_title);

        }
    }


}
