package com.friends.task_friends_android.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.friends.task_friends_android.dao.TaskDao;
import com.friends.task_friends_android.entities.Task;

@Database(entities = Task.class, version = 1, exportSchema = false)
public abstract class TaskDatabase extends RoomDatabase {
    private static TaskDatabase taskDatabase;

    public synchronized TaskDatabase getTaskDatabase(Context context){
        if (taskDatabase == null){
            taskDatabase = Room.databaseBuilder(
                    context,
                    TaskDatabase.class,
                    "tasks_db"
            ).build();
        }
        return taskDatabase;
    }

    public abstract TaskDao taskDao();

}
