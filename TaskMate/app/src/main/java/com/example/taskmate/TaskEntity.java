package com.example.taskmate;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tasks")
public class TaskEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String title;
    public String description;

    // Constructor
    public TaskEntity(String title, String description) {
        this.title = title;
        this.description = description;
    }
}
