package com.example.homeworkmanager;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.SharedPreferences;
import android.content.Intent;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Objects;

public class FourthActivity extends AppCompatActivity {

    SharedPreferences prefs;
    private static final String PREF_NAME = "task_prefs";
    private static final String KEY_NAME = "key_name";
    private static final String KEY_TASKS = "key_tasks_list";

    private ArrayList<Task> taskArrayList;
    private TaskManager taskManager;
    int taskId;

    TextView titleText;
    TextView typeText;
    TextView subjectText;
    TextView priorityText;
    TextView dueDateText;
    TextView exerciseText;
    TextView statusText;
    TextView pointsText;

    Button doneBtn;
    Button deleteBtn;
    Button returnBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fourth);

        taskId = getIntent().getIntExtra("TASK_ID", -1);

        prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        taskManager = new TaskManager(this);

        titleText = findViewById(R.id.tvTitleText);
        typeText = findViewById(R.id.tvTypeText);
        subjectText = findViewById(R.id.tvSubjectText);
        priorityText = findViewById(R.id.tvPriorityText);
        dueDateText = findViewById(R.id.tvDueDateText);
        exerciseText = findViewById(R.id.tvExerciseText);
        statusText = findViewById(R.id.tvStatusText);
        pointsText = findViewById(R.id.tvPointsText);

        doneBtn = findViewById(R.id.btnDone);
        deleteBtn = findViewById(R.id.btnDelete);
        returnBtn = findViewById(R.id.btnReturn);

        taskArrayList = taskManager.loadAllTasks();
        Task currentTask = taskManager.findById(taskId);

        UpdateInfo(currentTask);

        doneBtn.setOnClickListener(v -> changeStatus(currentTask));
        deleteBtn.setOnClickListener(v -> showDeleteDialog());
        returnBtn.setOnClickListener(v -> goBack());

    }

    public void UpdateInfo(Task t){
        titleText.setText("Task Title: " + t.getTitle());
        typeText.setText("Task Type: " + t.getType());
        subjectText.setText("Subject: " + t.getSubject().getDisplayName());
        priorityText.setText("Priority: " + t.getPriority().name());
        dueDateText.setText("Due Date: " + t.getDueDate());
        if (Objects.equals(t.getType(), "Homework")){
            exerciseText.setText("Exercises: " + t.getExtra());
        } else if (Objects.equals(t.getType(), "Exam")){
            exerciseText.setText("Exam Topics: " + t.getExtra());
        }
        if (t.isDone()){
            statusText.setText("Status: Completed");
            doneBtn.setText("Mark as unfinished");
        } else {
            statusText.setText("Status: Unfinished");
            doneBtn.setText("Mark as completed");
        }

        pointsText.setText("Worth " + t.getPoints() + " points");
    }

    private void showDeleteDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Task")
                .setMessage("Are you sure?")
                .setPositiveButton("Yes", (d, w) -> {
                    Toast.makeText(this, "Task Deleted.", Toast.LENGTH_SHORT).show();
                    deleteTask();
                })
                .setNegativeButton("No", null)
                .show();
    }

    public void deleteTask(){
        taskManager.deleteById(taskId);

        Intent intent = new Intent(FourthActivity.this, SecondActivity.class);
        startActivity(intent);
    }

    public void changeStatus(Task t){
        t.setDone(!t.isDone());
        UpdateInfo(t);
        taskManager.updateTask(t);
    }

    public void goBack(){
        Intent intent = new Intent(FourthActivity.this, SecondActivity.class);
        startActivity(intent);
    }
}