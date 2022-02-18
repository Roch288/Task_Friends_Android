package com.friends.task_friends_android.activities;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.friends.task_friends_android.R;
import com.friends.task_friends_android.adapters.TableTaskAdapters;
import com.friends.task_friends_android.database.TaskDatabase;
import com.friends.task_friends_android.db.TableTaskDB;
import com.friends.task_friends_android.entities.TableTask;
import com.friends.task_friends_android.entities.Task;
import com.friends.task_friends_android.listeners.TableTaskListeners;
import com.friends.task_friends_android.adapters.TasksAdapters;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TableTaskListeners {

    public final static int REQUEST_CODE_ADD_TASK = 1;
    public final static int REQUEST_CODE_UPDATE_TASK = 2;
    public final static int REQUEST_CODE_SHOW_TASKS = 3;
    public final static int REQUEST_CODE_SELECT_IMAGE = 4;
    public final static int REQUEST_CODE_STORAGE_PERMISSION = 5;
    public final static int REQUEST_AUDIO_PERMISSION = 6;


    private RecyclerView tasksRecyclerView;
    private List<Task> taskList;
    private List<TableTask> tableTasksList;
    private TableTaskAdapters tableTaskAdapters;
    private TasksAdapters tasksAdapter;

    private int taskClickedPosition = -1;


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
        tableTaskAdapters = new TableTaskAdapters(tableTasksList, this);
        tasksRecyclerView.setAdapter(tableTaskAdapters);

        // taskList = new ArrayList<>();
        //tasksAdapter = new TasksAdapters(taskList);
        //tasksRecyclerView.setAdapter(tasksAdapter);

        getTask(REQUEST_CODE_SHOW_TASKS, false);

        EditText inputSearch = findViewById(R.id.inputSearch);
        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tableTaskAdapters.cancelTimer();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(tableTasksList.size() != 0){
                    tableTaskAdapters.searchTasks(s.toString());
                }
            }
        });


        findViewById(R.id.imageAddTask).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(
                        new Intent(getApplicationContext(), CreateTaskActivity.class),
                        REQUEST_CODE_ADD_TASK
                );
            }
        });

        findViewById(R.id.imageAddImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(
                        getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                            MainActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            REQUEST_CODE_STORAGE_PERMISSION
                    );
                } else {
                    selectImage();
                }
            }
        });

    }

    public void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_CODE_SELECT_IMAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_STORAGE_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                selectImage();
            } else {
                Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == REQUEST_AUDIO_PERMISSION){
            if (grantResults.length > 0){
                boolean permissionToRecord=grantResults[0]==PackageManager.PERMISSION_GRANTED;
                if (permissionToRecord){

                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                }
                else {

                    Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private String encode(Uri imageUri) throws FileNotFoundException {
        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
        String encodedImage = encodeImage(selectedImage);
        return encodedImage;
    }

    private String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encImage;
    }

    @Override
    public void onTableTaskClicked(TableTask tableTask, int position) {
        taskClickedPosition = position;
        Intent intent = new Intent(getApplicationContext(), CreateTaskActivity.class);
        intent.putExtra("isViewUpdate", true);
        intent.putExtra("tableTask", tableTask);
        startActivityForResult(intent, REQUEST_CODE_UPDATE_TASK);
    }

    // Checking if the task list is empty , which indicates that the app just started since we have
    // Declared it as a global variable
    // But for this case we are adding all the notes from the database and notify the adapter about
    // The new loaded Dataset
    private void getTask (final int requestCode, final boolean isTaskDeleted) {

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

            @SuppressLint("NotifyDataSetChanged")
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

                if (requestCode == REQUEST_CODE_SHOW_TASKS){
                    tableTasksList.clear();
                    tableTasksList.addAll(tableTasks);
                    tableTaskAdapters.notifyDataSetChanged();
                } else if (requestCode == REQUEST_CODE_ADD_TASK) {
                    tableTasksList.add(0, tableTasks.get(0));
                    tableTaskAdapters.notifyItemInserted(0);
                    tasksRecyclerView.smoothScrollToPosition(0);
                } else if (requestCode == REQUEST_CODE_UPDATE_TASK){
                    tableTasksList.remove(taskClickedPosition);

                        if (isTaskDeleted){
                            tableTaskAdapters.notifyItemRemoved(taskClickedPosition);
                        } else{
                            tableTasksList.add(taskClickedPosition, tableTasks.get(taskClickedPosition));
                            tableTaskAdapters.notifyItemChanged(taskClickedPosition);

                        }
                }
                Log.d("My_TableTasks", tableTasksList.toString());
            }
        }
        new GetTask_HS().execute();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD_TASK && resultCode == RESULT_OK) {
            getTask(REQUEST_CODE_SHOW_TASKS, false);
        } else if (requestCode == REQUEST_CODE_UPDATE_TASK && resultCode == RESULT_OK){
            if (data != null){
                getTask(REQUEST_CODE_UPDATE_TASK, data.getBooleanExtra("isTaskDeleted", false));
            }
        }
          else  if (requestCode == REQUEST_CODE_SELECT_IMAGE && resultCode == RESULT_OK) {
                if (data != null) {
                    Uri selectedImageUri = data.getData();
                    if (selectedImageUri != null) {
                        try {
                            InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            Intent intent = new Intent(getApplicationContext(), CreateTaskActivity.class);
                            intent.putExtra("isFromQuickAction", true);
                            intent.putExtra("quickActionType", "image");
                            intent.putExtra("imagePath", encode(selectedImageUri));
                            startActivityForResult(intent, REQUEST_CODE_ADD_TASK);
//                        selectedImagePath = getPathFromUri(selectedImageUri);
                        } catch (Exception exception) {
                            Toast.makeText(this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

        }
    }
}