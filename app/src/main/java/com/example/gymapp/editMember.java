package com.example.gymapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class editMember extends AppCompatActivity {

    TextView name,dor,expiry;
    EditText phone,email;
    ImageButton delete;
    Integer id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getIntent().getExtras();
        id=1;
        if(b != null)
             id = Integer.parseInt(b.getString("id"));
        setContentView(R.layout.activity_edit_member);
        name = findViewById(R.id.emtv1);
        dor = findViewById(R.id.emtv2);
        phone = findViewById(R.id.emet1);
        email = findViewById(R.id.emet2);
        expiry = findViewById(R.id.emtv3);
        delete = findViewById(R.id.del);

        SimpleDateFormat sdf;
        sdf = new SimpleDateFormat("dd/mm/yyyy");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("members").whereEqualTo("id",id.toString()).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {


                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot d : list) {
                            name.setText(d.getString("name"));
                            dor.setText(d.getString("dor"));

                            email.setText(d.getString("email"));
                            phone.setText(d.getString("phone"));

                         /*   String dateStr = d.getString("dor");
                            Calendar cal = Calendar.getInstance();
                            String end;
                            switch (d.getString("pack"))
                            {
                                case "1 month": end = cal.MON
                            }
                            try {
                                Date endDate = sdf.parse(dateStr);
                                long diff = date.getTime() - date.getTime();
                                expiry.setText("Membership expires in: " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            expiry.setText(d.getString("dor"));

                          */



                        }
                        Toast.makeText(editMember.this, "Data fetched", Toast.LENGTH_SHORT).show();


                    }}).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(editMember.this, "Failed to get the data.", Toast.LENGTH_SHORT).show();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                               /* db.collection("members").whereEqualTo("id",id.toString())
                                        .delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {

                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                            }
                                        });*/
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(editMember.this);
                builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();

            }
        });
}}