package com.friends.task_friends_android.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.task_friends_android.R;
import com.friends.task_friends_android.activities.CreateTaskActivity;

public class MainActivity extends AppCompatActivity {

    public final static int REQUEST_CODE_ADD_TASK = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView imageAddTaskMain = findViewById(R.id.imageAddTaskMain);
        imageAddTaskMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(
                        new Intent(getApplicationContext(), CreateTaskActivity.class),
                        REQUEST_CODE_ADD_TASK
                );
            }
        });
    }
}