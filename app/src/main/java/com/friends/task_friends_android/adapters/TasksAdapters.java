package com.friends.task_friends_android.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.friends.task_friends_android.R;
import com.friends.task_friends_android.entities.Task;

import java.util.List;

public class TasksAdapters extends RecyclerView.Adapter<TasksAdapters.TaskViewHolders>{

    private List<Task> tasks;

    public TasksAdapters(List<Task> tasks) {
        this.tasks = tasks;
    }

    @NonNull
    @Override
    public TaskViewHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TaskViewHolders(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_container_task,
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolders holder, int position) {
        holder.setTask(tasks.get(position));
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static class TaskViewHolders extends RecyclerView.ViewHolder {
        TextView textTaskTitle, textTaskCategory, textTaskCreateDate;
        public TaskViewHolders(@NonNull View itemView) {
            super(itemView);
            textTaskTitle = itemView.findViewById(R.id.textRVTitle);
            textTaskCategory = itemView.findViewById(R.id.textRVCategory);
            textTaskCreateDate = itemView.findViewById(R.id.textRVCreatedDateTime);
        }
        void setTask(Task task) {
            textTaskTitle.setText(task.getTitle());
            if (task.getCategory().trim().isEmpty()) {
                textTaskCategory.setVisibility(View.GONE);
            }
            else
            {
                textTaskCategory.setText(task.getCategory());
            }
            textTaskCreateDate.setText(task.getCreateDateTime());

        }
    }
}
