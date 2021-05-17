package com.example.gymapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.core.OrderBy;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddMemberActivity extends AppCompatActivity {

    TextView tv,tv1,tv2,tv3,tv4,tv5;
    Integer id=1,count=0;
    EditText name,phone,email,date,et5;
    Button b;
    ImageButton icon;
    Boolean check;
    RadioGroup rg;
    Spinner sp;
    RadioButton r;
    String Name,Email,Phone;

    DatePickerDialog.OnDateSetListener datepicklistener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_member);

        check = true;
        tv = findViewById(R.id.amtv);
        tv1 = findViewById(R.id.amtv1);
        tv2 = findViewById(R.id.amtv2);
        tv3 = findViewById(R.id.amtv3);
        tv4 = findViewById(R.id.amtv4);
        tv5 = findViewById(R.id.amtv5);
        b = findViewById(R.id.button);
        sp = findViewById(R.id.spinner1);
        name = findViewById(R.id.amet1);
        phone = findViewById(R.id.amet2);
        email = findViewById(R.id.amet3);
        date = findViewById(R.id.amet4);
        icon = findViewById(R.id.IconCal);
        rg = findViewById(R.id.radioGroup);

        date.setEnabled(false);

        String[] packs = {"1 month","3 months","6 months","1 year"};

        ArrayAdapter ad = new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item, packs);
        sp.setAdapter(ad);

        icon.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(AddMemberActivity.this, datepicklistener, year, month, day);

                dialog.show();
            }
        });





        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check = true;
                r = findViewById(rg.getCheckedRadioButtonId());

                if(name.getText().toString()=="" || phone.getText().toString().length()==0 || email.getText().toString().length()==0 || TextUtils.isEmpty(date.getText().toString()))
                {
                    Toast.makeText(AddMemberActivity.this, "One or more required fields is empty!", Toast.LENGTH_LONG).show();
                    check = false;
                }
                if(phone.getText().toString().length()!=10)
                {
                    phone.setError("Mobile number is invalid");
                    check = false;
                }
                if(!(isValidEmail(email.getText().toString())))
                {
                    email.setError("Email ID is invalid");
                    check = false;
                }
                 Name = name.getText().toString();
                 Phone = phone.getText().toString();
                 Email = email.getText().toString();

                if(check==true)
                {

                    Map<String, Object> member = new HashMap<>();
                    member.put("name",Name);
                    member.put("phone",Phone);
                    member.put("email",Email);
                    member.put("gender",r.getText().toString());
                    member.put("dor",date.getText().toString());
                    member.put("pack",sp.getSelectedItem().toString());
                    member.put("time", FieldValue.serverTimestamp());


                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                    db.collection("members").orderBy("time", Query.Direction.DESCENDING).limit(1).get()
                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                                    if (!queryDocumentSnapshots.isEmpty()) {

                                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                                    for (DocumentSnapshot d : list) {

                                        if(d.getString("id")==null || d.getString("id")=="")
                                            id = 1;
                                        else
                                            id = Integer.parseInt(d.getString("id"));
                                        member.put("id",id.toString());


                                    }}

                                    else{
                                        id++;
                                        member.put("id",id.toString());

                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            id=2;
                            member.put("id",id.toString());
                        }
                    });

                   // id = 1;
                    member.put("id",id.toString());
                    db.collection("members")
                            .add(member)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {

                                   // Toast.makeText(AddMemberActivity.this, "New Member has Been registered", Toast.LENGTH_SHORT).show();
                                    }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(AddMemberActivity.this, "Error registering member", Toast.LENGTH_SHORT).show();
                                }
                            });
                    showMembers();



                }




            }
        });

        datepicklistener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                date.setText(dayOfMonth +"/"+month+"/"+year);
            }
        };


    }
    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public void showMembers()
    {
        Intent intent = new Intent(this,memberList.class);
        startActivity(intent);
    }
}