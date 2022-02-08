package com.friends.task_friends_android.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.task_friends_android.R;
import com.friends.task_friends_android.database.TaskDatabase;
import com.friends.task_friends_android.entities.Task;
import com.friends.task_friends_android.adapters.TasksAdapters;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public final static int REQUEST_CODE_ADD_TASK = 1;

    private RecyclerView tasksRecyclerView;
    private List<Task> taskList;
    private TasksAdapters tasksAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView imageAddTaskMain = findViewById(R.id.imageAddTaskMain);
        imageAddTaskMain.setOnClickListener(v -> startActivityForResult(
                new Intent(getApplicationContext(), CreateTaskActivity.class),
                REQUEST_CODE_ADD_TASK
        ));

        tasksRecyclerView = findViewById(R.id.tasksRecycleView);
        tasksRecyclerView.setLayoutManager(
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        );
        taskList = new ArrayList<>();
        tasksAdapter = new TasksAdapters(taskList);
        tasksRecyclerView.setAdapter(tasksAdapter);

        getTask();
    }

    // Checking if the task list is empty , which indicates that the app just started since we have
    // Declared it as a global variable
    // But for this case we are adding all the notes from the database and notify the adapter about
    // The new loaded Dataset
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

                if (taskList.size()==0) {
                    taskList.addAll(tasks);
                    tasksAdapter.notifyDataSetChanged();
                }
                else {
                    taskList.add(0,tasks.get(0));
                    tasksAdapter.notifyItemInserted(0);
                }
                tasksRecyclerView.smoothScrollToPosition(0);
            }
        }
        new GetTask_HS().execute();

    }
}