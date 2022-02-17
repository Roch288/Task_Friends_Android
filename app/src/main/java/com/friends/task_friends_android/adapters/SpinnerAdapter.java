package com.friends.task_friends_android.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.friends.task_friends_android.Categories;
import com.friends.task_friends_android.R;

import java.util.List;

public class SpinnerAdapter extends ArrayAdapter<Categories> {


    LayoutInflater layoutInflater;

    public SpinnerAdapter(@NonNull Context context, int resource, @NonNull List<Categories> categories) {
        super(context, resource, categories);
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View rowView = layoutInflater.inflate(R.layout.item_selected_view, null, true);
        Categories categories = getItem(position);
        TextView categoryText = (TextView) rowView.findViewById(R.id.categoryText);
        ImageView categoryImageIcon = (ImageView) rowView.findViewById(R.id.categoryImageIcon);
        categoryText.setText(categories.getCatName());
        categoryImageIcon.setImageResource(categories.getImage());
        return rowView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null)
            convertView = layoutInflater.inflate(R.layout.spinner_adapter, parent, false);

        Categories categories = getItem(position);
        TextView categoryText = (TextView) convertView.findViewById(R.id.categoryText);
        ImageView categoryImageIcon = (ImageView) convertView.findViewById(R.id.categoryImageIcon);
        categoryText.setText(categories.getCatName());
        categoryImageIcon.setImageResource(categories.getImage());
        return convertView;
    }
}
