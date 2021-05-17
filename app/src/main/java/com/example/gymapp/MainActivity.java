package com.example.gymapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button login,signup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        login = findViewById(R.id.button);
        signup = findViewById(R.id.button2);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMembers();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMembers();
            }
        });

    }
    public void addMembers()
    {
        Intent intent = new Intent(this,AddMemberActivity.class);
        startActivity(intent);
    }
    public void showMembers()
    {
        Intent intent = new Intent(this,memberList.class);
        startActivity(intent);
    }
}