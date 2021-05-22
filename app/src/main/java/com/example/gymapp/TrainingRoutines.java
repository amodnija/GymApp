package com.example.gymapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

public class TrainingRoutines extends AppCompatActivity {

    CardView cv1,cv2,cv3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_routines);
        cv1 = findViewById(R.id.cardView1);
        cv2 = findViewById(R.id.cardView2);
        cv3 = findViewById(R.id.cardView3);


        cv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showinfo("TR 1");
            }
        });
        cv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showinfo("TR 2");
            }
        });
        cv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showinfo("TR 3");
            }
        });
    }
    public void showinfo(String s)
    {
        Intent i = new Intent(this,routineInfo.class);
        Bundle b = new Bundle();
        b.putString("trno",s); //tr number
        i.putExtras(b);
        startActivity(i);
    }

}