package com.example.taskmate;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Switch;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<TaskEntity> taskList;
    ArrayAdapter<String> adapter;
    ArrayList<String> taskTitles;
    ListView listView;
    FloatingActionButton fabAdd;
    TaskDatabase taskDatabase;
    Switch switchDarkMode; // ✅ Switch for Dark Mode

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        fabAdd = findViewById(R.id.fabAdd);
        switchDarkMode = findViewById(R.id.switchDarkMode); // ✅ Find the Dark Mode switch

        taskDatabase = TaskDatabase.getInstance(this);

        taskTitles = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, taskTitles);
        listView.setAdapter(adapter);

        // ✅ Request Notification Permission for Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
            }
        }

        // ✅ Dark Mode Toggle Logic
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            switchDarkMode.setChecked(true);
        }

        switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });

        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddTaskActivity.class);
            startActivity(intent);
        });

        listView.setOnItemClickListener((adapterView, view, position, id) -> {
            Intent intent = new Intent(MainActivity.this, EditTaskActivity.class);
            intent.putExtra("task_id", taskList.get(position).id); // pass id
            startActivity(intent);
        });

        listView.setOnItemLongClickListener((adapterView, view, position, id) -> {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Delete Task")
                    .setMessage("Are you sure you want to delete this task?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        taskDatabase.taskDao().delete(taskList.get(position));
                        refreshList();
                    })
                    .setNegativeButton("No", null)
                    .show();
            return true;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshList();
    }

    private void refreshList() {
        taskList = taskDatabase.taskDao().getAllTasks();
        taskTitles.clear();
        for (TaskEntity task : taskList) {
            taskTitles.add(task.title);
        }
        adapter.notifyDataSetChanged();
    }
}
