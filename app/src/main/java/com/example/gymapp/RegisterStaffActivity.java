package com.example.gymapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterStaffActivity extends AppCompatActivity {

    private TextView credtxt;
    private EditText emailtxt, pswdtxt;
    private Switch adswitch;
    private Button staffbtn;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register_staff);


        mAuth = FirebaseAuth.getInstance();

        credtxt = findViewById(R.id.credtxtview);
        adswitch = findViewById(R.id.adminswitch);
        staffbtn = findViewById(R.id.stafflregbtn);
        emailtxt = findViewById(R.id.emailedttxt);
        pswdtxt = findViewById(R.id.pswdedttxt);

        staffbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = emailtxt.getText().toString().trim();
                String pswd = pswdtxt.getText().toString().trim();
                String role;

                if (adswitch.isChecked()) {
                    role = "admin";
                } else {
                    role = "staff";
                }
                if (email.isEmpty()) {
                    emailtxt.setError("Email is required!");
                    emailtxt.requestFocus();
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailtxt.setError("Please enter a valid email!");
                    emailtxt.requestFocus();
                    return;
                }

                if (pswd.isEmpty()) {
                    pswdtxt.setError("Password is required!");
                    pswdtxt.requestFocus();
                    return;
                }

                if (pswd.length() < 6) {
                    pswdtxt.setError("Password should be at least 6 characters!");
                    pswdtxt.requestFocus();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, pswd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            User user = new User(email, role, "");

                            FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(RegisterStaffActivity.this, "Registration completed successfully", Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(RegisterStaffActivity.this, Dashboard.class));
                                        finish();

                                    } else {
                                        Toast.makeText(RegisterStaffActivity.this, "Registration failed", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(RegisterStaffActivity.this, "Registration failed", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }

        });
    }

}