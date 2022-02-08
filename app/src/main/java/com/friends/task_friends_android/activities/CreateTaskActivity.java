package com.friends.task_friends_android.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.task_friends_android.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CreateTaskActivity extends AppCompatActivity {

    private EditText inputTaskTitle, inputTaskCategory, inputTaskDesc;
    private TextView textCreateDateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);

        ImageView imageBack = findViewById(R.id.imageBack);
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        inputTaskTitle = findViewById(R.id.inputTaskTitle);
        inputTaskCategory = findViewById(R.id.inputTaskCategory);
        inputTaskDesc = findViewById(R.id.inputTaskDesc);
        textCreateDateTime = findViewById(R.id.textCreateDateTime);


        textCreateDateTime.setText(
                new SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm a", Locale.getDefault())
                        .format(new Date())
        );
    }
}