package com.friends.task_friends_android.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.io.Serializable;
import java.util.ArrayList;


@Entity (tableName = "tasks")

public class Task implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name= "title")
    private String title;

    @ColumnInfo(name = "create_date_time")
    private String createDateTime;

    @ColumnInfo(name = "set_date_time")
    private String setDateTime;

    @ColumnInfo(name = "category")
    private String category;

    @ColumnInfo(name = "task_text")
    private String taskText;

    @ColumnInfo(name = "image_path")
    private String imagePath;

    @ColumnInfo(name = "image_list")
    private ArrayList<String> image;

    @ColumnInfo(name = "color")
    private String color;

    @ColumnInfo(name = "audio_path")
    private String audioPath;

    @ColumnInfo(name = "web_link")
    private String webLink;

    @ColumnInfo(name = "completed")
    private String completed;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(String createDateTime) {
        this.createDateTime = createDateTime;
    }

    public String getSetDateTime() {
        return setDateTime;
    }

    public void setSetDateTime(String setDateTime) {
        this.setDateTime = setDateTime;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTaskText() {
        return taskText;
    }

    public void setTaskText(String taskText) {
        this.taskText = taskText;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public ArrayList<String> getImage() {
        return image;
    }

    public void setImage(ArrayList<String> image) {
        this.image = image;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getAudioPath() {
        return audioPath;
    }

    public void setAudioPath(String audioPath) {
        this.audioPath = audioPath;
    }

    public String getWebLink() {
        return webLink;
    }

    public void setWebLink(String webLink) {
        this.webLink = webLink;
    }

    public String getCompleted() {
        return completed;
    }

    public void setCompleted(String completed) {
        this.completed = completed;
    }

    @NonNull
    @Override
    public String toString() {
        return title + " : " + createDateTime;
    }
}
