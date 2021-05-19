package com.example.gymapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.security.spec.ECField;
import java.util.ArrayList;
import java.util.List;

public class memberList extends AppCompatActivity {

    RecyclerView rv;
    FloatingActionButton fb;
    ImageButton edit;
    ArrayList<Integer> ids = new ArrayList<>();

    ArrayList<String> names = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_list);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        rv = findViewById(R.id.rv);
        fb = findViewById(R.id.fab);
        edit = findViewById(R.id.editbutton);

    try {
        db.collection("members").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {

                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot d : list) {

                                names.add(d.getString("name"));
                                ids.add(Integer.parseInt(d.getString("id")));


                            }
                            Toast.makeText(memberList.this, "Data fetched", Toast.LENGTH_SHORT).show();


                            String[] fin = names.toArray(new String[names.size()]);


                            rv.setLayoutManager(new LinearLayoutManager(memberList.this));
                            rv.setAdapter(new MyAdapter(fin, (ids.toArray(new Integer[ids.size()]))));

                        }
                        else{


                            Toast.makeText(memberList.this, "No member found. Please Add a member first", Toast.LENGTH_LONG).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(memberList.this, "Failed to get the data.", Toast.LENGTH_SHORT).show();
            }
        });
    }catch(@Nullable Exception e)
    {
        Toast.makeText(memberList.this, "Database connection failed", Toast.LENGTH_SHORT).show();

    }
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMembers();
            }
        });

       /* edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MemberEdit();
            }
        });

        */



    }
    public void MemberEdit(String s)
    {
        Intent intent = new Intent(memberList.this, editMember.class);
        Bundle b = new Bundle();
        b.putString("id",s); //member id
        intent.putExtras(b);
        startActivity(intent);
        finish();
    }
    public void addMembers()
    {
        Intent intent = new Intent(this,AddMemberActivity.class);
        startActivity(intent);
    }
}