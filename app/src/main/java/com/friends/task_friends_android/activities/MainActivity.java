package com.friends.task_friends_android.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.example.task_friends_android.R;
import com.friends.task_friends_android.database.TaskDatabase;
import com.friends.task_friends_android.entities.Task;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public final static int REQUEST_CODE_ADD_TASK = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView imageAddTaskMain = findViewById(R.id.imageAddTaskMain);
        imageAddTaskMain.setOnClickListener(v -> startActivityForResult(
                new Intent(getApplicationContext(), CreateTaskActivity.class),
                REQUEST_CODE_ADD_TASK
        ));
        getTask();
    }

    private void getTask () {

        class GetTask_HS extends AsyncTask<Void, Void, List<Task>>{

            @Override
            protected List<Task> doInBackground(Void... voids) {
                return TaskDatabase
                        .getTaskDatabase(getApplicationContext())
                        .taskDao().getAllTasks();
            }

            @Override
            protected void onPostExecute(List<Task> tasks){
                super.onPostExecute(tasks);
                Log.d("My_Tasks", tasks.toString());
            }
        }
        new GetTask_HS().execute();

    }
}