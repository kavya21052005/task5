package com.example.taskmate;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class EditTaskActivity extends AppCompatActivity {

    EditText editTitle, editDescription;
    Button buttonUpdate;
    TaskDatabase taskDatabase;
    TaskEntity task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        editTitle = findViewById(R.id.editTitle);
        editDescription = findViewById(R.id.editDescription);
        buttonUpdate = findViewById(R.id.buttonUpdate);

        taskDatabase = TaskDatabase.getInstance(this);

        int taskId = getIntent().getIntExtra("task_id", -1);
        if (taskId != -1) {
            for (TaskEntity t : taskDatabase.taskDao().getAllTasks()) {
                if (t.id == taskId) {
                    task = t;
                    break;
                }
            }
            if (task != null) {
                editTitle.setText(task.title);
                editDescription.setText(task.description);
            }
        }

        buttonUpdate.setOnClickListener(v -> {
            String updatedTitle = editTitle.getText().toString();
            String updatedDescription = editDescription.getText().toString();

            if (!updatedTitle.isEmpty() && task != null) {
                task.title = updatedTitle;
                task.description = updatedDescription;
                taskDatabase.taskDao().update(task);

                NotificationHelper.showNotification(this, "Task Updated", updatedTitle); // ✅ NEW LINE added

                finish();
            }
        });

    }
}
