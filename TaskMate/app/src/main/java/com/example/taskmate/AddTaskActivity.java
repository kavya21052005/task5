package com.example.taskmate;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class AddTaskActivity extends AppCompatActivity {

    EditText editTitle, editDescription;
    Button buttonSave;
    TaskDatabase taskDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        editTitle = findViewById(R.id.editTitle);
        editDescription = findViewById(R.id.editDescription);
        buttonSave = findViewById(R.id.buttonSave);

        taskDatabase = TaskDatabase.getInstance(this);

        buttonSave.setOnClickListener(v -> {
            String title = editTitle.getText().toString();
            String description = editDescription.getText().toString();
            if (!title.isEmpty()) {
                taskDatabase.taskDao().insert(new TaskEntity(title, description));
                NotificationHelper.showNotification(this, "New Task Added", title);

                finish();
            }
        });
    }
}
