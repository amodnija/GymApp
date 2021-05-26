package com.example.gymapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.example.gymapp.AddMemberActivity.isValidEmail;



public class editMember extends AppCompatActivity {

    TextView name,dor,idno;
    EditText phone,email;
    ImageButton delete;
    Button save;
    ImageView photo;
    Integer id;
    Boolean check;
    Spinner sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_member);

        Bundle b = getIntent().getExtras();
        id=1;
        if(b != null)
             id = Integer.parseInt(b.getString("id"));
        name = findViewById(R.id.emtv1);
        photo = findViewById(R.id.imageView);
        dor = findViewById(R.id.emtv2);
        phone = findViewById(R.id.emet1);
        email = findViewById(R.id.emet2);
        idno = findViewById(R.id.emtv3);
        delete = findViewById(R.id.del);
        save = findViewById(R.id.savebutton);
        sp = findViewById(R.id.spinner);
         check = true;
        String[] TR = {"None", "TR 1", "TR 2", "TR 3"};

        ArrayAdapter ad = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, TR);
        sp.setAdapter(ad);

        StorageReference ref = FirebaseStorage.getInstance().getReference().child("images/"+id.toString()+".jpg");


        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                RequestOptions requestOptions = new RequestOptions();
                requestOptions.placeholder(R.drawable.person_icon);
                requestOptions.error(R.drawable.person_icon);
                requestOptions.diskCacheStrategy(DiskCacheStrategy.DATA);
                GlideApp.with(getApplicationContext())
                        .load(uri.toString())
                        .diskCacheStrategy(DiskCacheStrategy.DATA)
                        .dontAnimate()
                        .into(photo);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });



        SimpleDateFormat sdf;

        sdf = new SimpleDateFormat("dd/mm/yyyy");
        try {
            Date d = sdf.parse(dor.getText().toString());

        } catch (ParseException e) {
            e.printStackTrace();
        }
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("members").whereEqualTo("id",id.toString()).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {


                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot d : list) {
                            name.setText(d.getString("name")+", "+d.getString("gender"));
                            dor.setText("Registered On: "+d.getString("dor")+" for "+d.getString("pack"));

                            email.setText(d.getString("email"));
                            phone.setText(d.getString("phone"));
                            idno.setText("Membership ID: "+d.getString("id"));
                            if (d.getString("routine") != null) {
                                int spinnerPosition = ad.getPosition(d.getString("routine"));
                                sp.setSelection(spinnerPosition);
                            }
                            else
                                sp.setSelection(0);



                        }
                       // Toast.makeText(editMember.this, "Member Details fetched", Toast.LENGTH_SHORT).show();


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
                                db.collection("members").document(id.toString())
                                        .delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                FirebaseStorage storage = FirebaseStorage.getInstance();
                                                StorageReference ref = storage.getReference();
                                                ref.child("images/"+id.toString()+".jpg").delete();
                                                Toast.makeText(editMember.this, "Member successfully deleted", Toast.LENGTH_SHORT).show();

                                                showMembers();


                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                               }
                                        });

                                break;

                            case DialogInterface.BUTTON_NEGATIVE:

                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(editMember.this);
                builder.setMessage("Permanently Delete Member?").setPositiveButton("Delete", dialogClickListener)
                        .setNegativeButton("Cancel", dialogClickListener).show();

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check = true;
                if(phone.getText().toString().length()!=10)
                {
                    check = false;
                    phone.setError("Enter valid mobile number");
                }
                if(!isValidEmail(email.getText().toString()))
                {
                    check = false;
                    email.setError("Enter valid Email ID");
                }
                if(check)
                {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                String tr = sp.getSelectedItem().toString();

                                db.collection("members").document(id.toString())
                                        .update("phone",phone.getText().toString(),"email",email.getText().toString(),"routine",tr)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(editMember.this, "Member details were updated", Toast.LENGTH_SHORT).show();

                                                showMembers();


                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(editMember.this, "Failed to update details", Toast.LENGTH_SHORT).show();


                                            }
                                        });
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:

                                break;
                        }
                    }
                };
                    AlertDialog.Builder builder = new AlertDialog.Builder(editMember.this);
                    builder.setMessage("Save your changes?").setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();
            }

            }
        });
    }
    public void showMembers()
    {
        Intent i = new Intent(this,memberList.class);
        startActivity(i);
        this.finish();
    }
    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public void onBackPressed()
    {

        this.startActivity(new Intent(editMember.this,memberList.class));
        finish();
        return;
    }
}