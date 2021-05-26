package com.example.gymapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private TextView AppName, txtforpas;
    private Button loginbtn,contact;
    private ProgressBar progbar;
    private EditText email, password;


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();




        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            startActivity(new Intent(this, Dashboard.class));
            finish();
        }

        loginbtn = findViewById(R.id.loginbtn);
        //AppName = findViewById(R.id.AppName);
        txtforpas = findViewById(R.id.txtforpas);
        contact = findViewById(R.id.txtsgnup);
        email = findViewById(R.id.edttxtun);
        password = findViewById(R.id.edttxtpswd);
        loginbtn.setOnClickListener(this);
        contact.setOnClickListener(this);
        txtforpas.setOnClickListener(this);


        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginbtn:
                userLogin();
                break;
            case R.id.txtforpas:
                startActivity(new Intent(this, ForgotPassword.class));
                break;
            case R.id.txtsgnup:
                startActivity(new Intent(this,ContactUs.class));
        }

    }

    private void userLogin() {
        String emailstr = email.getText().toString().trim();
        String pswd = password.getText().toString().trim();

        if(emailstr.isEmpty()) {
            email.setError("Email is required!");
            email.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(emailstr).matches()){
            email.setError("Please enter a valid email!");
            email.requestFocus();
            return;
        }

        if(pswd.isEmpty()) {
            password.setError("Password is required!");
            password.requestFocus();
            return;
        }

        if(pswd.length() < 6) {
            password.setError("Password should be at least 6 characters!");
            password.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(emailstr, pswd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    startActivity(new Intent(LoginActivity.this, Dashboard.class));
                    finish();

                }else{
                    Toast.makeText(LoginActivity.this, "Failed to login! Please check your credentials", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    //QAvD_BwE
}