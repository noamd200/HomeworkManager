package com.example.homeworkmanager;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.AdapterView;

import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.content.Intent;
import android.widget.Toast;

import java.util.Objects;


public class MainActivity extends AppCompatActivity {

    SharedPreferences prefs;
    private static final String PREF_NAME = "task_prefs";
    private static final String KEY_NAME = "key_name";

    TextView welcomeText;
    EditText nameEditTxt;
    Button loginBtn;
    Button resetBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        welcomeText = findViewById(R.id.tvWelcomeText);
        nameEditTxt = findViewById(R.id.etStudentName);
        loginBtn = findViewById(R.id.btnLogIn);
        resetBtn = findViewById(R.id.btnReset);

        String username = loadName();
        setNewName(username);

        loginBtn.setOnClickListener(v -> LogIn());
        resetBtn.setOnClickListener(v -> showResetDialog());

    }

    public void LogIn(){
        String username = String.valueOf(nameEditTxt.getText());
        saveName(username);

        Intent intent = new Intent(MainActivity.this, SecondActivity.class);
        startActivity(intent);
    }

    public void ResetData(){
        prefs.edit().clear().apply();
        setNewName("");
    }

    private void showResetDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Reset Data")
                .setMessage("All your tasks and information will be deleted. Continue?")
                .setPositiveButton("Yes", (d, w) -> {
                    Toast.makeText(this, "Data Reset.", Toast.LENGTH_SHORT).show();
                    ResetData();
                })
                .setNegativeButton("No", (d, w) ->
                        Toast.makeText(this, "Cancelled.", Toast.LENGTH_SHORT).show())
                .show();
    }


    public void setNewName(String name){
        if (Objects.equals(name, "")){
            welcomeText.setText("Welcome! Enter your name:");
            nameEditTxt.setText("");
        }
        else{
            welcomeText.setText("Welcome back, " + name + "!");
            nameEditTxt.setText(name);
        }
    }


    public String loadName() {
        return prefs.getString(KEY_NAME, "");
    }

    public void saveName(String name) {
        prefs.edit().putString(KEY_NAME, name).apply();
    }
}