package com.friends.task_friends_android.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.friends.task_friends_android.R;
import com.friends.task_friends_android.entities.TableTask;
import com.friends.task_friends_android.listeners.TableTaskListeners;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;


public class TableTaskAdapters extends RecyclerView.Adapter<TableTaskAdapters.TableTaskViewHolder> {

    private List<TableTask> tablesTasks;
    private TableTaskListeners tableTaskListeners;

    public TableTaskAdapters(List<TableTask> tablesTasks, TableTaskListeners tableTaskListeners) {
        this.tablesTasks = tablesTasks;
        this.tableTaskListeners = tableTaskListeners;
    }

    @NonNull
    @Override
    public TableTaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TableTaskViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_container_task,
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull TableTaskViewHolder holder, int position) {
        holder.setTableTask(tablesTasks.get(position));
        holder.layoutTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tableTaskListeners.onTableTaskClicked(tablesTasks.get(position), position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tablesTasks.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static class TableTaskViewHolder extends RecyclerView.ViewHolder{

        TextView textTableTitle, textTableCategory, textTableDate;
        LinearLayout layoutTask;
        RoundedImageView imageTableTask;

        public TableTaskViewHolder(@NonNull View itemView) {
            super(itemView);
            textTableTitle = itemView.findViewById(R.id.textRVTitle);
            textTableCategory = itemView.findViewById(R.id.textRVCategory);
            textTableDate = itemView.findViewById(R.id.textRVCreatedDateTime);
            layoutTask = itemView.findViewById(R.id.layoutTask);
            imageTableTask = itemView.findViewById(R.id.imageRVTask);
        }

        void setTableTask(TableTask tableTask) {
            textTableTitle.setText(tableTask.getTitle());
            if (tableTask.getCategory().trim().isEmpty()){
                textTableCategory.setVisibility(View.GONE);
            }
            else
            {
                textTableCategory.setText(tableTask.getCategory());
            }

            textTableDate.setText(tableTask.getCreateDateTime());

            GradientDrawable gradientDrawable = (GradientDrawable) layoutTask.getBackground();
            if (tableTask.getColor() != null){
                gradientDrawable.setColor(Color.parseColor(tableTask.getColor()));
            }
            else {
                gradientDrawable.setColor(Color.parseColor("#333333"));
            }

            if (tableTask.getImagePath() == null) {
                imageTableTask.setImageBitmap(BitmapFactory.decodeFile(tableTask.getImagePath()));
                imageTableTask.setVisibility(View.VISIBLE);
            }
            else {
                imageTableTask.setVisibility(View.GONE);
            }
        }
    }
}