package com.friends.task_friends_android.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.friends.task_friends_android.R;
import com.friends.task_friends_android.database.TaskDatabase;
import com.friends.task_friends_android.db.TableTaskDB;
import com.friends.task_friends_android.entities.TableTask;
import com.friends.task_friends_android.entities.Task;

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
        imageBack.setOnClickListener(v -> onBackPressed());

        ImageView imageSave = findViewById(R.id.imageSave);
        imageSave.setOnClickListener(v -> saveTask());

        inputTaskTitle = findViewById(R.id.inputTaskTitle);
        inputTaskCategory = findViewById(R.id.inputTaskCategory);
        inputTaskDesc = findViewById(R.id.inputTaskDesc);
        textCreateDateTime = findViewById(R.id.textCreateDateTime);


        textCreateDateTime.setText(
                new SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm a", Locale.getDefault())
                        .format(new Date())
        );
    }

    private void saveTask(){
        if (inputTaskTitle.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "TITLE CANNOT BE EMPTY!! ", Toast.LENGTH_SHORT).show();
            return;
        } else if (inputTaskCategory.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "CATEGORY CANNOT BE EMPTY", Toast.LENGTH_SHORT).show();
            return;
        }

        final TableTask tableTask = new TableTask();
        tableTask.setTitle(inputTaskTitle.getText().toString());
        tableTask.setCategory(inputTaskCategory.getText().toString());
        tableTask.setTaskText(inputTaskDesc.getText().toString());
        tableTask.setCreateDateTime(textCreateDateTime.getText().toString());

//        final Task task = new Task();
//        task.setTitle(inputTaskTitle.getText().toString());
//        task.setCategory(inputTaskCategory.getText().toString());
//        task.setTaskText(inputTaskDesc.getText().toString());
//        task.setCreateDateTime(textCreateDateTime.getText().toString());


        // ROOM does not allow database operation on the main thread
        // Use of Async task to bypass it.

        @SuppressLint("StaticFieldLeak")
        class SaveTask extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... voids){
                TableTaskDB.getDatabase(getApplicationContext()).tableTaskDao().insertTableTask(tableTask);
                return null;

//                TaskDatabase.getTaskDatabase(getApplicationContext()).taskDao().insertTask(task);
//                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid){
                super.onPostExecute(aVoid);
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        }

        new SaveTask().execute();
    }
}