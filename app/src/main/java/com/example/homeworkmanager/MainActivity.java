package com.example.homeworkmanager;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.SharedPreferences;
import android.content.Intent;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    SharedPreferences prefs;
    private static final String PREF_NAME = "task_prefs";
    private static final String KEY_NAME = "key_name";

    TextView welcomeText = findViewById(R.id.tvWelcomeText);
    EditText nameEditTxt = findViewById(R.id.etStudentName);
    Button loginBtn = findViewById(R.id.btnLogIn);
    Button resetBtn = findViewById(R.id.btnReset);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        String username = loadName();
        setNewName(username);

        loginBtn.setOnClickListener(v -> LogIn());

    }

    public void LogIn(){
        //Intent intent = new Intent(MainActivity.this, SecondActivity.class);
        //startActivity(intent);
    }

    public void setNewName(String name){
        if (Objects.equals(name, "")){
            welcomeText.setText("Welcome! Enter your name:");
        }
        else{
            welcomeText.setText("Welcome back, " + name + "!");
            nameEditTxt.setText(name);
        }
    }


    public String loadName() {
        String name = prefs.getString(KEY_NAME, "");
        return name;
    }

    public void saveName(String name) {
        prefs.edit().putString(KEY_NAME, name).apply();
    }
}