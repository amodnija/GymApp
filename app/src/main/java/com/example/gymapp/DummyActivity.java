package com.example.gymapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DummyActivity extends AppCompatActivity {
    private Button addstaff;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String UserID;


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dummy);

        addstaff = findViewById(R.id.addstaffbtn);
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        UserID = user.getUid();

        addstaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference.child(UserID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User userp = snapshot.getValue(User.class);

                        if (userp != null){
                            String email = userp.email;
                            String role = userp.role;
                            String gymid = userp.gymid;
                            Log.d("role", role);

                            if (role.equals("admin")) {
                                startActivity(new Intent(DummyActivity.this, RegisterStaffActivity.class));
                            } else {
                                Toast.makeText(DummyActivity.this, "You're not an admin!", Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(DummyActivity.this, "Couldn't connect to database!", Toast.LENGTH_LONG).show();
                    }
                });

            }
        });
    }

}