package com.friends.task_friends_android.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.example.task_friends_android.R;
import com.friends.task_friends_android.adapters.TableTaskAdapters;
import com.friends.task_friends_android.database.TaskDatabase;
import com.friends.task_friends_android.db.TableTaskDB;
import com.friends.task_friends_android.entities.TableTask;
import com.friends.task_friends_android.entities.Task;
import com.friends.task_friends_android.adapters.TasksAdapters;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public final static int REQUEST_CODE_ADD_TASK = 1;

    private RecyclerView tasksRecyclerView;
    private List<Task> taskList;
    private List<TableTask> tableTasksList;
    private TableTaskAdapters tableTaskAdapters;
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
        tableTasksList = new ArrayList<>();
        tableTaskAdapters = new TableTaskAdapters((tableTasksList));
        tasksRecyclerView.setAdapter(tableTaskAdapters);

        // taskList = new ArrayList<>();
        //tasksAdapter = new TasksAdapters(taskList);
        //tasksRecyclerView.setAdapter(tasksAdapter);

        getTask();
    }

    // Checking if the task list is empty , which indicates that the app just started since we have
    // Declared it as a global variable
    // But for this case we are adding all the notes from the database and notify the adapter about
    // The new loaded Dataset
    private void getTask () {

        @SuppressLint("StaticFieldLeak")
        class GetTask_HS extends AsyncTask<Void, Void, List<TableTask>>{

            @Override
            protected List<TableTask> doInBackground(Void... voids) {
                 /*return TaskDatabase
                         .getDatabase(getApplicationContext())
                         .taskDao().getAllTasks(); */

                return TableTaskDB
                        .getDatabase(getApplicationContext())
                        .tableTaskDao().getAllTableTask();
            }

            @Override
            protected void onPostExecute(List<TableTask> tableTasks){
                super.onPostExecute(tableTasks);
                 /*if (taskList.size()==0) {
                    taskList.addAll(tasks);
                    tasksAdapter.notifyDataSetChanged();
                }
                else {
                    taskList.add(0,tasks.get(0));
                    tasksAdapter.notifyItemInserted(0);
                }
                tasksRecyclerView.smoothScrollToPosition(0);
                // Scrolling the recyclerview to the top */

                if (tableTasksList.size() == 0) {
                    tableTasksList.addAll(tableTasks);
                    tableTaskAdapters.notifyDataSetChanged();
                }
                else {
                    tableTasksList.add(0, tableTasks.get(0));
                    tableTaskAdapters.notifyItemInserted(0);
                }
                tasksRecyclerView.smoothScrollToPosition(0);


                Log.d("My_TableTasks", tableTasks.toString());
            }
        }
        new GetTask_HS().execute();

    }
}