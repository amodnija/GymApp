package com.example.gymapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class routineInfo extends AppCompatActivity {

    TextView tv,TV;
    Button b;
    EditText et;
    RecyclerView rv;
    ArrayList<Integer> ids = new ArrayList<>();
    ArrayList<Integer> fetchedIDs = new ArrayList<>();

    String trno;

    ArrayList<String> names = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_routine_info);
        Bundle bun = getIntent().getExtras();
        trno="None";
        if(bun != null)
            trno = bun.getString("trno");
        TV = findViewById(R.id.textView7);
        tv = findViewById(R.id.textView8);
        rv = findViewById(R.id.rv1);
        b = findViewById(R.id.buttonAdd);
        et = findViewById(R.id.addID);

        b.setEnabled(false);
        switch(trno)
        {
            case "TR 1":
                TV.setText("Training Routine 1");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    tv.setText(Html.fromHtml("<pre><b>Monday Thursday : </b> Upper Body <br><br><b>Tuesday    Friday : </b> Cardio   <br><br><b> Wednesday    Saturday : </b> Legs <br><br></pre>", Html.FROM_HTML_MODE_COMPACT));
                } else {
                    tv.setText(Html.fromHtml("<h2>Title</h2><br><p>Description here</p>"));
                }
                break;

            case "TR 2":
                TV.setText("Training Routine 2");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    tv.setText(Html.fromHtml("<pre><b>Monday Thursday : </b> Legs <br><br><b>Tuesday    Friday : </b> Upper Body   <br><br><b> Wednesday    Saturday : </b> Cardio<br><br></pre>", Html.FROM_HTML_MODE_COMPACT));
                } else {
                    tv.setText(Html.fromHtml("<h2>Title</h2><br><p>Description here</p>"));
                }
                break;

            case "TR 3":
                TV.setText("Training Routine 3");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    tv.setText(Html.fromHtml("<pre><b>Monday Thursday : </b> Cardio <br><br><b>Tuesday    Friday : </b> Legs   <br><br><b> Wednesday    Saturday : </b> Upper Body<br><br></pre>", Html.FROM_HTML_MODE_COMPACT));
                } else {
                    tv.setText(Html.fromHtml("<h2>Title</h2><br><p>Description here</p>"));
                }
                break;


        }




        et.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.toString().trim().length()==0){
                    b.setEnabled(false);
                } else {
                    b.setEnabled(true);
                }
            }



            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }


        });

        try {
            db.collection("members").whereEqualTo("routine", trno).get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if (!queryDocumentSnapshots.isEmpty()) {

                                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                                for (DocumentSnapshot d : list) {


                                    names.add(d.getString("name"));
                                    fetchedIDs.add(Integer.parseInt(d.getString("id")));


                                }


                                String[] fin = names.toArray(new String[names.size()]);


                                rv.setLayoutManager(new LinearLayoutManager(routineInfo.this));
                                rv.setAdapter(new Adapter1(names,fetchedIDs));

                            } else {


                                Toast.makeText(routineInfo.this, "No member is currently registered to this Routine", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("Firestore Exception", "get failed with ", e);
                    Toast.makeText(routineInfo.this, "Failed to get the data. Check Internet connection", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (@Nullable Exception e) {
            Toast.makeText(routineInfo.this, "Database connection failed", Toast.LENGTH_SHORT).show();

        }

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean check = true;
                if(et.getText().toString()=="")
                {
                    check = false;
                    et.setError("Invalid member ID");
                }
                Boolean bool = fetchedIDs.contains(Integer.parseInt(et.getText().toString().trim()));

                if(bool==true)
                {
                    check = false;
                    Toast.makeText(routineInfo.this, "This member has already been added!", Toast.LENGTH_SHORT).show();

                }

                if (check==true) {
                    String id = et.getText().toString();
                    Integer i = Integer.parseInt(id);
                    db.collection("members").document(id).update("routine", trno)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    et.setText("");
                                    Toast.makeText(routineInfo.this, "Member Added", Toast.LENGTH_SHORT).show();
                                    addToList();
                                }
                            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(routineInfo.this, "This member ID does not exist", Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }
        });
    }

    public void addToList()
    {
        try {
            ids.clear();
            names.clear();
            db.collection("members").whereEqualTo("routine", trno).get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if (!queryDocumentSnapshots.isEmpty()) {

                                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                                for (DocumentSnapshot d : list) {


                                    names.add(d.getString("name"));
                                    ids.add(Integer.parseInt(d.getString("id")));


                                }



                                rv.setLayoutManager(new LinearLayoutManager(routineInfo.this));
                                rv.setAdapter(new Adapter1(names,ids));

                            } else {


                                Toast.makeText(routineInfo.this, "No member is currently registered to this Routine", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("Firestore Exception", "get failed with ", e);
                    Toast.makeText(routineInfo.this, "Failed to get the data. Check Internet connection", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (@Nullable Exception e) {
            Toast.makeText(routineInfo.this, "Database connection failed", Toast.LENGTH_SHORT).show();

        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

}