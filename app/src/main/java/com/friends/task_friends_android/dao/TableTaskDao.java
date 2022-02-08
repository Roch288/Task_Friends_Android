package com.friends.task_friends_android.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.friends.task_friends_android.entities.TableTask;

import java.util.List;

@Dao
public interface TableTaskDao {

    @Query("SELECT * FROM tableTask ORDER BY id DESC")
    List<TableTask> getAllTableTask();

    @Insert
    void insertTableTask (TableTask tableTask);
}