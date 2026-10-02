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

public class ThirdActivity extends AppCompatActivity {

    SharedPreferences prefs;
    private static final String PREF_NAME = "task_prefs";
    private static final String KEY_NAME = "key_name";
    private static final String KEY_TASKS = "key_tasks_list";

    private ArrayList<Task> taskArrayList;
    private TaskManager taskManager;

    Spinner typeDropDown;
    EditText titleEditTxt;
    Spinner subjectDropDown;
    Spinner priorityDropDown;
    EditText dateEditTxt;
    TextView exercisesTxt;
    EditText exercisesEditTxt;
    Button btnSave;
    Button btnCancel;

    private final ArrayList<String> typeList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        typeDropDown = findViewById(R.id.typeDropDown);
        titleEditTxt = findViewById(R.id.etTitle);
        subjectDropDown = findViewById(R.id.subjectDropDown);
        priorityDropDown = findViewById(R.id.priorityDropDown);
        dateEditTxt = findViewById(R.id.etDueDate);
        exercisesTxt = findViewById(R.id.tvExerciseText);
        exercisesEditTxt = findViewById(R.id.etExercises);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);

        taskManager = new TaskManager(this);
        taskArrayList = taskManager.loadAllTasks();

        typeList.add("Homework");
        typeList.add("Exam");
        setupDropDown(typeDropDown, typeList);
        setupDropDown(subjectDropDown, getSubjectList());
        setupDropDown(priorityDropDown, getPriorityList());

        btnSave.setOnClickListener(v -> addNewTask());
        btnCancel.setOnClickListener(v -> returnBack());

        typeDropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected item string
                String selectedType = parent.getItemAtPosition(position).toString();

                // Dynamically toggle fields based on selection
                if (selectedType.equalsIgnoreCase("Homework")) {
                    // e.g., hide exercises field for exams
                    exercisesTxt.setText("No. of exercises:");
                    exercisesEditTxt.setHint("ex. 8");
                } else {
                    exercisesTxt.setText("No. of topics:");
                    exercisesEditTxt.setHint("ex. 3");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Optional: Triggered when the selection disappears (rarely used)
            }
        });

    }

    public ArrayList<String> getSubjectList(){
        ArrayList<String> list = new ArrayList<>();
        for (Task.Subject subject : Task.Subject.values()) {
            list.add(subject.getDisplayName());
        }
        return list;
    }

    public ArrayList<String> getPriorityList(){
        ArrayList<String> list = new ArrayList<>();
        for (Task.Priority priority : Task.Priority.values()) {
            list.add(priority.name());
        }
        return list;
    }

    public void setupDropDown(Spinner dropDown, ArrayList<String> options){
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                options
        );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropDown.setAdapter(spinnerAdapter);
    }

    public boolean checkErrors() {
        boolean failed = false;

        if (titleEditTxt.getText().toString().trim().isEmpty()) {
            titleEditTxt.setError("Title cannot be empty!");
            failed = true;
        }

        if (dateEditTxt.getText().toString().trim().isEmpty()) {
            dateEditTxt.setError("Date cannot be empty!");
            failed = true;
        }

        String exercisesInput = exercisesEditTxt.getText().toString().trim();
        if (exercisesInput.isEmpty()) {
            exercisesEditTxt.setError("Field cannot be empty!");
            failed = true;
        } else {
            try {
                int count = Integer.parseInt(exercisesInput);
                if (count < 1) {
                    exercisesEditTxt.setError("Must be at least 1!");
                    failed = true;
                }
            } catch (NumberFormatException e) {
                exercisesEditTxt.setError("Please enter a valid number!");
                failed = true;
            }
        }
        return failed;
    }

    public void addNewTask(){
        if (checkErrors()){
            return;
        }
        else {
            String type = typeDropDown.getSelectedItem().toString();
            String title = titleEditTxt.getText().toString();
            String subject = subjectDropDown.getSelectedItem().toString();
            Task.Subject subjectEnum = Task.Subject.fromDisplayName(subject);
            String priority = priorityDropDown.getSelectedItem().toString();
            Task.Priority priorityEnum = Task.Priority.valueOf(priority);
            String dueDate = dateEditTxt.getText().toString();
            int misc = Integer.parseInt(exercisesEditTxt.getText().toString());
            int id = taskManager.nextId();

            Task newTask = null;
            if (type.equals("Homework")){
                newTask = new HomeworkTask(misc, id, title, subjectEnum, priorityEnum, dueDate);
            } else if (type.equals("Exam")){
                newTask = new ExamTask(misc, id, title, subjectEnum, priorityEnum, dueDate);
            }
            taskArrayList.add(newTask);
            taskManager.saveAll(taskArrayList);

            Intent intent = new Intent(ThirdActivity.this, SecondActivity.class);
            startActivity(intent);

        }
    }

    public void returnBack(){
        Intent intent = new Intent(ThirdActivity.this, SecondActivity.class);
        startActivity(intent);
    }

}