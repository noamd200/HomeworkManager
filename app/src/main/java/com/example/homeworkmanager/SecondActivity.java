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

public class SecondActivity extends AppCompatActivity {

    SharedPreferences prefs;
    private static final String PREF_NAME = "task_prefs";
    private static final String KEY_NAME = "key_name";
    private static final String KEY_TASKS = "key_tasks_list";

    private ArrayList<String> displayList;
    private ArrayAdapter<String> adapter;
    private ArrayList<Task> taskArrayList;

    private TaskManager taskManager;

    TextView welcomeText;
    TextView statsText;
    Spinner dropDown;
    TextView emptyListText;
    ListView taskList;

    Button logOutBtn;
    Button addNewBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        welcomeText = findViewById(R.id.tvWelcomeText);
        statsText = findViewById(R.id.tvStatsTitle);
        dropDown = findViewById(R.id.subjectDropDown);
        emptyListText = findViewById(R.id.tvNoTasksTitle);
        taskList = findViewById(R.id.listViewTasks);
        logOutBtn = findViewById(R.id.btnLogOut);
        addNewBtn = findViewById(R.id.btnNewTask);

        taskManager = new TaskManager(this);


        taskArrayList = loadTasks();
        displayList = new ArrayList<>();

        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                displayList
        );
        taskList.setAdapter(adapter);


        ArrayList<String> subjectOptions = getSubjectSpinnerList();
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                subjectOptions
        );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropDown.setAdapter(spinnerAdapter);

        dropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedSubject = subjectOptions.get(position);
                filterTasksBySubject(selectedSubject);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        taskList.setOnItemClickListener((parent, view, position, id) -> {
            String selectedString = displayList.get(position);
            Task clickedTask = null;

            // Find the task in taskArrayList that matches the clicked string
            for (Task task : taskArrayList) {
                if (task != null && task.toString().equals(selectedString)) {
                    clickedTask = task;
                    break;
                }
            }

            if (clickedTask != null) {
                Intent intent = new Intent(SecondActivity.this, FourthActivity.class);
                intent.putExtra("TASK_ID", clickedTask.getId());
                startActivity(intent);
            }
        });

        UpdateInfo();

        logOutBtn.setOnClickListener(v -> showLogoutDialog());
        addNewBtn.setOnClickListener(v -> addNewTask());

    }

    public String loadName() {
        return prefs.getString(KEY_NAME, "");
    }

    public ArrayList<Task> loadTasks(){
        return taskManager.loadAllTasks();
    }


    public void UpdateInfo(){
        welcomeText.setText("Welcome, " + loadName() + "!");

        if (taskArrayList.isEmpty()){
            statsText.setText("Tasks: 0  | Completed: 0  | Points: 0");
            emptyListText.setVisibility(View.VISIBLE);
            rebuildDisplayList();
        }
        else {

            int count = taskArrayList.size();
            int completed = 0;
            int points = 0;

            for (int i = 0; i < taskArrayList.size(); i++) {
                if (taskArrayList.get(i).isDone()) {
                    completed++;
                    points += taskArrayList.get(i).getPoints();
                }
            }
            statsText.setText("Tasks: " + count + "  | Completed: " + completed + "  | Points: " + points);
            emptyListText.setVisibility(View.GONE);
            rebuildDisplayList();
        }

        taskManager.saveAll(taskArrayList);
    }

    private void rebuildDisplayList() {
        displayList.clear();
        for (int i = 0; i < taskArrayList.size(); i++) {
            String line = taskArrayList.get(i).toString();
            displayList.add(line);
        }

        adapter.notifyDataSetChanged();
    }

    private ArrayList<String> getSubjectSpinnerList() {
        ArrayList<String> list = new ArrayList<>();
        list.add("All"); // Default option to show everything

        for (Task.Subject subject : Task.Subject.values()) {
            list.add(subject.getDisplayName());
        }
        return list;
    }

    private void filterTasksBySubject(String selectedSubject) {
        displayList.clear();

        for (Task task : taskArrayList) {
            if (task == null) continue;

            if (selectedSubject.equalsIgnoreCase("All") ||
                    task.getSubject().getDisplayName().equalsIgnoreCase(selectedSubject)) {
                displayList.add(task.toString());
            }
        }

        emptyListText.setVisibility(displayList.isEmpty() ? View.VISIBLE : View.GONE);
        adapter.notifyDataSetChanged();
    }


    private void showLogoutDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Log Out")
                .setMessage("Are you sure?")
                .setPositiveButton("Yes", (d, w) -> {
                    Toast.makeText(this, "Logged out.", Toast.LENGTH_SHORT).show();
                    logOut();
                })
                .setNegativeButton("No", null)
                .show();
    }

    public void logOut(){
        taskManager.saveAll(taskArrayList);

        Intent intent = new Intent(SecondActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void addNewTask(){
        taskManager.saveAll(taskArrayList);

        Intent intent = new Intent(SecondActivity.this, ThirdActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh task list from SharedPreferences on screen focus
        taskArrayList = loadTasks();
        UpdateInfo();
    }
}