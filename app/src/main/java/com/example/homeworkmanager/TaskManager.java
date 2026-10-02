package com.example.homeworkmanager;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.Objects;

public class TaskManager {

    private static final String PREF_NAME = "task_prefs";
    private static final String KEY_TASKS = "key_tasks_list";

    private final SharedPreferences prefs;
    private final Gson gson;

    public TaskManager(Context context) {
        this.prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        this.gson = new Gson();
    }

    // Loads all tasks saved in SharedPreferences with polymorphic deserialization support.
    public ArrayList<Task> loadAllTasks() {
        String json = prefs.getString(KEY_TASKS, null);
        if (json == null || json.trim().isEmpty()) {
            return new ArrayList<>();
        }

        ArrayList<Task> tasks = new ArrayList<>();

        JsonArray jsonArray = JsonParser.parseString(json).getAsJsonArray();

        for (JsonElement element : jsonArray) {
            if (element == null || element.isJsonNull()) continue;

            JsonObject jsonObject = element.getAsJsonObject();

            // Inspect type tag or fallback to concrete subclasses
            String type = jsonObject.has("type") ? jsonObject.get("type").getAsString() : "";

            Task task = null;
            if ("HomeworkTask".equalsIgnoreCase(type)) {
                task = gson.fromJson(jsonObject, HomeworkTask.class);
            } else if ("ExamTask".equalsIgnoreCase(type)) {
                task = gson.fromJson(jsonObject, ExamTask.class);
            } else {
                // Default fallback: attempts parsing to standard concrete class if type is unspecified
                task = gson.fromJson(jsonObject, HomeworkTask.class);
            }

            if (task != null) {
                tasks.add(task);
            }
        }

        tasks.removeIf(Objects::isNull);
        return tasks;
    }

    // Overwrites the current task list in SharedPreferences.
    public void saveAll(ArrayList<Task> tasks) {
        if (tasks == null) {
            tasks = new ArrayList<>();
        }
        tasks.removeIf(Objects::isNull);
        String json = gson.toJson(tasks);
        prefs.edit().putString(KEY_TASKS, json).apply();
    }

    // Adds a single task and saves the updated list.
    public void addTask(Task task) {
        if (task == null) return;
        ArrayList<Task> tasks = loadAllTasks();
        tasks.add(task);
        saveAll(tasks);
    }

    // Finds a task by its ID. Returns null if not found.
    public Task findById(int id) {
        ArrayList<Task> tasks = loadAllTasks();
        for (Task task : tasks) {
            if (task != null && task.getId() == id) {
                return task;
            }
        }
        return null;
    }

    // Updates an existing task with matching ID.
    public void updateTask(Task newTask) {
        if (newTask == null) return;
        ArrayList<Task> tasks = loadAllTasks();
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            if (task != null && task.getId() == newTask.getId()) {
                tasks.set(i, newTask);
                saveAll(tasks);
                return;
            }
        }
    }

    // Deletes a task by ID.
    public void deleteById(int id) {
        ArrayList<Task> tasks = loadAllTasks();
        tasks.removeIf(task -> task == null || task.getId() == id);
        saveAll(tasks);
    }

    // Generates the next available task ID (highest current ID + 1).
    public int nextId() {
        ArrayList<Task> tasks = loadAllTasks();
        if (tasks.isEmpty()){
            return 0;
        }
        int maxId = 0;
        for (Task task : tasks) {
            if (task != null && task.getId() > maxId) {
                maxId = task.getId();
            }
        }
        return maxId + 1;
    }
}