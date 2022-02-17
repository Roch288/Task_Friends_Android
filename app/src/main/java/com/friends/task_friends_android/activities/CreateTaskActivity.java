package com.friends.task_friends_android.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.friends.task_friends_android.Categories;
import com.friends.task_friends_android.R;
import com.friends.task_friends_android.TaskCompleted;
import com.friends.task_friends_android.adapters.CompletedSpinnerAdapter;
import com.friends.task_friends_android.adapters.SpinnerAdapter;
import com.friends.task_friends_android.db.TableTaskDB;
import com.friends.task_friends_android.entities.TableTask;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class CreateTaskActivity extends AppCompatActivity {

    private EditText inputTaskTitle, inputTaskDesc;
    private TextView textCreateDateTime;
    private View viewCategoryIndicator;
    private String selectedTaskColor;
    private ImageView imageTableTask;
    private String selectedImageBase64;
    private TableTask alreadyAvailableTableTask;
    private AlertDialog dialogDeleteTask;
    private Spinner spinner;
    private Categories selectedCategory;
    private Spinner taskSpinner;
    private TaskCompleted taskCompleted;


    private static final int REQUEST_CODE_STORAGE_PERMISSION = 1;
    private static final int REQUEST_CODE_SELECT_IMAGE = 2;
    private static final int GALLERY_REQUEST = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);

        TaskCompleted.initCompleted();
        Categories.initCategories();
        taskSpinner = (Spinner) findViewById(R.id.spinnerCompleted);
        CompletedSpinnerAdapter customCompletedAdapter = new CompletedSpinnerAdapter(this,R.layout.spinner_completed, TaskCompleted.getTaskCompletedArrayList());
        taskSpinner.setAdapter(customCompletedAdapter);
        taskSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    taskCompleted = null;
                } else {
                    taskCompleted = (TaskCompleted) parent.getSelectedItem();
                    Log.d("Progress Report", taskCompleted.toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner = (Spinner) findViewById(R.id.categorySpinner);
        SpinnerAdapter customAdapter = new SpinnerAdapter(this, R.layout.spinner_adapter, Categories.getCategoriesArrayList());
        spinner.setAdapter(customAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    selectedCategory = null;
                } else {
                    selectedCategory = (Categories) parent.getSelectedItem();
                    Log.d("selectedCategory", selectedCategory.toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ImageView imageBack = findViewById(R.id.imageBack);
        imageBack.setOnClickListener(v -> onBackPressed());

        ImageView imageSave = findViewById(R.id.imageSave);
        imageSave.setOnClickListener(v -> saveTask());

        inputTaskTitle = findViewById(R.id.inputTaskTitle);
        inputTaskDesc = findViewById(R.id.inputTaskDesc);
        textCreateDateTime = findViewById(R.id.textCreateDateTime);
        viewCategoryIndicator = findViewById(R.id.viewCategoryIndicator);
        imageTableTask = findViewById(R.id.imageTask);


        textCreateDateTime.setText(
                new SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm a", Locale.getDefault())
                        .format(new Date())
        );

        selectedTaskColor = "#333333";
        selectedImageBase64 = "";

        if (getIntent().getBooleanExtra("isViewUpdate", false)) {
            alreadyAvailableTableTask = (TableTask) getIntent().getSerializableExtra("tableTask");
            setViewOrUpdateTableTask();
        }


        initMore();
        setCategoryIndicatorColor();
    }

    private void setViewOrUpdateTableTask() {
        inputTaskTitle.setText(alreadyAvailableTableTask.getTitle());
        inputTaskDesc.setText(alreadyAvailableTableTask.getTaskText());

        ArrayList<TaskCompleted> taskCompleteds = TaskCompleted.getTaskCompletedArrayList();
        for (TaskCompleted taskCompleted : taskCompleteds) {
            if (taskCompleted.getCompleted().equals(alreadyAvailableTableTask.getCompleted())){
                this.taskCompleted = taskCompleted;
            }
        }
        if (taskCompleted != null){
            taskSpinner.setSelection(taskCompleteds.indexOf(selectedCategory));
            taskSpinner.setVisibility(View.VISIBLE);

        }else
        {
            taskSpinner.setSelection(0);
            taskSpinner.setVisibility(View.VISIBLE);
        }

        ArrayList<Categories> categories = Categories.getCategoriesArrayList();
        for (Categories category : categories) {
            if (category.getCatName().equals(alreadyAvailableTableTask.getCategory())) {
                this.selectedCategory = category;
            }
        }
        if (selectedCategory != null) {
            spinner.setSelection(categories.indexOf(selectedCategory));
        } else {
            spinner.setSelection(0);
        }

        textCreateDateTime.setText(alreadyAvailableTableTask.getCreateDateTime());
        if (alreadyAvailableTableTask.getImagePath() != null && !alreadyAvailableTableTask.getImagePath().trim().isEmpty()) {
            // decode base64 string
            byte[] bytes = Base64.decode(alreadyAvailableTableTask.getImagePath(), Base64.DEFAULT);
            // Initialize bitmap
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            // set bitmap on imageView
            imageTableTask.setImageBitmap(bitmap);
            imageTableTask.setVisibility(View.VISIBLE);
            selectedImageBase64 = alreadyAvailableTableTask.getImagePath();
        }
    }


    private void saveTask() {
        String taskTitle = inputTaskTitle.getText().toString().trim();
        if (taskTitle.isEmpty()) {
            Toast.makeText(this, "TITLE CANNOT BE EMPTY!! ", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedCategory == null) {
            Toast.makeText(this, "Please select category.", Toast.LENGTH_SHORT).show();
            return;
        }

        final TableTask tableTask = new TableTask();
        tableTask.setTitle(inputTaskTitle.getText().toString());
        tableTask.setCategory(selectedCategory.getCatName());
        tableTask.setTaskText(inputTaskDesc.getText().toString());
        tableTask.setCreateDateTime(textCreateDateTime.getText().toString());
        tableTask.setColor(selectedTaskColor);
        tableTask.setImagePath(selectedImageBase64);
        tableTask.setCompleted(taskCompleted.getCompleted());

        if (alreadyAvailableTableTask != null) {
            tableTask.setId(alreadyAvailableTableTask.getId());
        }

        // ROOM does not allow database operation on the main thread
        // Use of Async task to bypass it.

        @SuppressLint("StaticFieldLeak")
        class SaveTask extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... voids) {
                TableTaskDB.getDatabase(getApplicationContext()).tableTaskDao().insertTableTask(tableTask);
                return null;

//                TaskDatabase.getTaskDatabase(getApplicationContext()).taskDao().insertTask(task);
//                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        }

        new SaveTask().execute();
    }

    private void initMore() {
        final LinearLayout layoutMore = findViewById(R.id.layoutBottomBar);
        final BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(layoutMore);

        layoutMore.findViewById(R.id.textMore).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }

            }
        });

        final ImageView imagePriorityLow = layoutMore.findViewById(R.id.imagePriorityLow);
        final ImageView imagePriorityMedium = layoutMore.findViewById(R.id.imagePriorityMedium);
        final ImageView imagePriorityHigh = layoutMore.findViewById(R.id.imagePriorityHigh);

        layoutMore.findViewById(R.id.viewColorLow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedTaskColor = "#FF018786";
                imagePriorityLow.setImageResource(R.drawable.ic_done);
                imagePriorityMedium.setImageResource(0);
                imagePriorityHigh.setImageResource(0);
                setCategoryIndicatorColor();
            }
        });
        layoutMore.findViewById(R.id.viewColorMedium).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedTaskColor = "#FDBE3B";
                imagePriorityLow.setImageResource(0);
                imagePriorityMedium.setImageResource(R.drawable.ic_done);
                imagePriorityHigh.setImageResource(0);
                setCategoryIndicatorColor();
            }
        });
        layoutMore.findViewById(R.id.viewColorHigh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedTaskColor = "#FF4842";
                imagePriorityLow.setImageResource(0);
                imagePriorityMedium.setImageResource(0);
                imagePriorityHigh.setImageResource(R.drawable.ic_done);
                setCategoryIndicatorColor();
            }
        });

        if (alreadyAvailableTableTask != null && alreadyAvailableTableTask.getColor() != null && !alreadyAvailableTableTask.getColor().trim().isEmpty()) {
            switch (alreadyAvailableTableTask.getColor()) {
                case "#FF018786":
                    layoutMore.findViewById(R.id.viewColorLow).performClick();
                    break;
                case "#FDBE3B":
                    layoutMore.findViewById(R.id.viewColorMedium).performClick();
                    break;
                case "#FF4842":
                    layoutMore.findViewById(R.id.viewColorHigh).performClick();
                    break;
            }
        }

        layoutMore.findViewById(R.id.layoutAddImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                if (ContextCompat.checkSelfPermission(
                        getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                            CreateTaskActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            REQUEST_CODE_STORAGE_PERMISSION
                    );
                } else {
                    selectImage();
                }
            }
        });

        if (alreadyAvailableTableTask != null) {
            layoutMore.findViewById(R.id.layoutDeleteTask).setVisibility(View.VISIBLE);
            layoutMore.findViewById(R.id.layoutDeleteTask).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    showDeleteDialog();
                }
            });
        }

    }

    private void showDeleteDialog() {
        if (dialogDeleteTask == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(CreateTaskActivity.this);
            View view = LayoutInflater.from(this).inflate(
                    R.layout.layout_delete_dialoug,
                    (ViewGroup) findViewById(R.id.layoutDeleteTaskContainer)
            );
            builder.setView(view);
            dialogDeleteTask = builder.create();
            if (dialogDeleteTask.getWindow() != null) {
                dialogDeleteTask.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }
            view.findViewById(R.id.textDeleteTask).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    @SuppressLint("StaticFieldLeak")
                    class DeleteTaskFunc extends AsyncTask<Void, Void, Void> {

                        @Override
                        protected Void doInBackground(Void... voids) {
                            TableTaskDB.getDatabase(getApplicationContext()).tableTaskDao()
                                    .deleteTask(alreadyAvailableTableTask);
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void unused) {
                            super.onPostExecute(unused);
                            Intent intent = new Intent();
                            intent.putExtra("isTaskDeleted", true);
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    }
                    new DeleteTaskFunc().execute();
                }
            });

            view.findViewById(R.id.textCancelDelete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogDeleteTask.dismiss();
                }
            });
        }
        dialogDeleteTask.show();
    }

    private void setCategoryIndicatorColor() {
        GradientDrawable gradientDrawable = (GradientDrawable) viewCategoryIndicator.getBackground();
        gradientDrawable.setColor(Color.parseColor(selectedTaskColor));
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SELECT_IMAGE && resultCode == RESULT_OK) {
            if (data != null) {
                Uri selectedImageUri = data.getData();
                if (selectedImageUri != null) {
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        imageTableTask.setImageBitmap(bitmap);
                        imageTableTask.setVisibility(View.VISIBLE);
                        selectedImageBase64 = encode(selectedImageUri);
//                        selectedImagePath = getPathFromUri(selectedImageUri);
                    } catch (Exception exception) {
                        Toast.makeText(this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                    }
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

    private String getPathFromUri(Uri contentUri) {
        String filePath;
        Cursor cursor = getContentResolver()
                .query(contentUri, null, null, null, null);
        if (cursor == null) {
            filePath = contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex("_data");
            filePath = cursor.getString(index);
            cursor.close();
        }
        return filePath;
    }
}