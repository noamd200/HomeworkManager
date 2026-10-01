package com.example.homeworkmanager;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class TaskManager {

    private static final String PREF_NAME = "task_prefs";
    private static final String KEY_TASKS = "key_tasks_list";

    private final SharedPreferences prefs;
    private final Gson gson;

    public TaskManager(Context context) {
        this.prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        this.gson = new Gson();
    }


    //Loads all tasks saved in SharedPreferences.
    public ArrayList<Task> loadAll() {
        String json = prefs.getString(KEY_TASKS, null);
        if (json == null) {
            return new ArrayList<>();
        }
        Type type = new TypeToken<ArrayList<Task>>() {}.getType();
        ArrayList<Task> tasks = gson.fromJson(json, type);
        if (tasks == null){
            return new ArrayList<>();
        }
        return tasks;
    }


    //Overwrites the current task list in SharedPreferences.
    public void saveAll(ArrayList<Task> tasks) {
        String json = gson.toJson(tasks);
        prefs.edit().putString(KEY_TASKS, json).apply();
    }


    //Adds a single task and saves the updated list.
    public void addTask(Task task) {
        ArrayList<Task> tasks = loadAll();
        tasks.add(task);
        saveAll(tasks);
    }


    //Finds a task by its ID. Returns null if not found.
    public Task findById(int id) {
        ArrayList<Task> tasks = loadAll();
        for (Task task : tasks) {
            if (task.getId() == id) {
                return task;
            }
        }
        return null;
    }


    //Updates an existing task with matching ID.
    public void updateTask(Task newTask) {
        ArrayList<Task> tasks = loadAll();
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getId() == newTask.getId()) {
                tasks.set(i, newTask);
                saveAll(tasks);
                return;
            }
        }
    }


    //Deletes a task by ID.
    public void deleteById(int id) {
        ArrayList<Task> tasks = loadAll();
        tasks.removeIf(task -> task.getId() == id);
        saveAll(tasks);
    }


    //Generates the next available task ID (highest current ID + 1).
    public int nextId() {
        ArrayList<Task> tasks = loadAll();
        if (tasks == null){ return 0; }
        int maxId = 0;
        for (Task task : tasks) {
            if (task.getId() > maxId) {
                maxId = task.getId();
            }
        }
        return maxId + 1;
    }
}
