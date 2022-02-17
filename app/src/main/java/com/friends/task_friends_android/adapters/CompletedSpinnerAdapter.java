package com.friends.task_friends_android.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.friends.task_friends_android.Categories;
import com.friends.task_friends_android.R;
import com.friends.task_friends_android.TaskCompleted;

import java.util.List;

public class CompletedSpinnerAdapter extends ArrayAdapter<TaskCompleted> {

    LayoutInflater layoutInflater;

    public CompletedSpinnerAdapter(@NonNull Context context, int resource, @NonNull List<TaskCompleted> taskCompleteds) {
        super(context, resource, taskCompleteds);
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        View rowView = layoutInflater.inflate(R.layout.spinner_completed_itemview, null, true);
        TaskCompleted texttaskCompleted = getItem(position);
        TextView progressText = (TextView) rowView.findViewById(R.id.itemviewtext);
        progressText.setText(texttaskCompleted.getCompleted());
        return rowView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        if (convertView == null)
            convertView = layoutInflater.inflate(R.layout.spinner_completed, parent, false);

        TaskCompleted taskCompleted = getItem(position);
        TextView texttaskCompleted = (TextView) convertView.findViewById(R.id.spinnerCompletedText);
        texttaskCompleted.setText(taskCompleted.getCompleted());
        return convertView;
    }
}
