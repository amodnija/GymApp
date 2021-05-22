 package com.example.gymapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
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

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.core.OrderBy;
import com.google.firebase.installations.Utils;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddMemberActivity extends AppCompatActivity {

    TextView tv, tv1, tv2, tv3, tv4, tv5, showID;
    Integer id = 0, count = 0;
    EditText name, phone, email, date, et5;
    Button generateID, b, uploadPic;
    ImageButton icon;
    Boolean check;
    RadioGroup rg;
    Spinner sp;
    RadioButton r;
    String Name, Email, Phone;

    DatePickerDialog.OnDateSetListener datepicklistener;

    ImageButton addpic;

    // Uri indicates, where the image will be picked from
    Uri filePath;

    private final int PICK_IMAGE_REQUEST = 22;

    FirebaseStorage storage;
    StorageReference storageReference;

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
        showID = findViewById(R.id.idshow);
        generateID = findViewById(R.id.generate);
        addpic = findViewById(R.id.imageButton);


        date.setEnabled(false);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        FirebaseFirestore db = FirebaseFirestore.getInstance();


        String[] packs = {"1 month", "3 months", "6 months", "1 year"};

        ArrayAdapter ad = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, packs);
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


        addpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage(AddMemberActivity.this);
            }
        });


        generateID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                check = true;
                Map<String, Object> member = new HashMap<>();

                r = findViewById(rg.getCheckedRadioButtonId());

                if (name.getText().toString().length() == 0 || phone.getText().toString().length() == 0 || email.getText().toString().length() == 0 || TextUtils.isEmpty(date.getText().toString())) {
                    Toast.makeText(AddMemberActivity.this, "One or more required fields is empty!", Toast.LENGTH_LONG).show();
                    check = false;
                }
                if (phone.getText().toString().length() != 10) {
                    phone.setError("Mobile number is invalid");
                    check = false;
                }
                if (!(isValidEmail(email.getText().toString()))) {
                    email.setError("Email ID is invalid");
                    check = false;
                }
                Name = name.getText().toString();
                Phone = phone.getText().toString();
                Email = email.getText().toString();



                    db.collection("members").whereEqualTo("phone", Phone)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        //  check = false;
                                        //  Toast.makeText(AddMemberActivity.this, "This Mobile number is already registered", Toast.LENGTH_SHORT).show();

                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            if (document.getString("phone") == Phone) {
                                                Toast.makeText(AddMemberActivity.this, "This Mobile number is already registered", Toast.LENGTH_SHORT).show();
                                                check = false;
                                                b.setEnabled(false);

                                            }
                                        }


                                        if (check == true) {


                                            member.put("name", Name);
                                            member.put("phone", Phone);
                                            member.put("email", Email);
                                            member.put("gender", r.getText().toString());
                                            member.put("dor", date.getText().toString());
                                            member.put("pack", sp.getSelectedItem().toString());
                                            member.put("time", FieldValue.serverTimestamp());


                                            db.collection("members").orderBy("time", Query.Direction.DESCENDING).limit(1).get()
                                                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                                                            if (!queryDocumentSnapshots.isEmpty()) {

                                                                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                                                                for (DocumentSnapshot d : list) {

                                                                    if (d.getString("id") == null || d.getString("id") == "")
                                                                        id = 0;
                                                                    else
                                                                        id = Integer.parseInt(d.getString("id"));
                                                                    id++;
                                                                    member.put("id", id.toString());
                                                                    showID.setText(id.toString());
                                                                    b.setEnabled(true);


                                                                }
                                                            } else {
                                                                id = 1;
                                                                member.put("id", id.toString());
                                                                showID.setText(id.toString());
                                                                b.setEnabled(true);

                                                            }
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                            /*id = 1;
                            member.put("id", id.toString());
                            showID.setText(id.toString());
                            b.setEnabled(true);*/
                                                }
                                            });

                                        }

                                    }
                                }});

                    b.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            db.collection("members").document(showID.getText().toString()).set(member)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(AddMemberActivity.this, "Member added successfully", Toast.LENGTH_SHORT).show();
                                            uploadImage();

                                            home();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(AddMemberActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();

                                }
                            });


                        }
                    });
                    // id = 0 ?;

                }




        });





        datepicklistener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                date.setText(dayOfMonth + "/" + month + "/" + year);
            }
        };


    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public void home() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }

    public void showMembers() {
        Intent intent = new Intent(this, memberList.class);
        startActivity(intent);
    }

    private void selectImage(Context context) {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose profile picture for Member");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {
                    Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);

                } else if (options[item].equals("Choose from Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, 1);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    Bitmap selectedImage;
    // Override onActivityResult method
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                       selectedImage = (Bitmap) data.getExtras().get("data");
                        addpic.setImageBitmap(selectedImage);

                        filePath = data.getData();
                    }

                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                         filePath = data.getData();
                       /* String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (filePath != null) {
                            Cursor cursor = getContentResolver().query(filePath,
                                    filePathColumn, null, null, null);
                            if (cursor != null) {
                                ((Cursor) cursor).moveToFirst();

                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);*/
                                try {

                                    // Setting image on image view using Bitmap
                                    Bitmap bitmap = MediaStore
                                            .Images
                                            .Media
                                            .getBitmap(
                                                    getContentResolver(),
                                                    filePath);
                                    //Setting image using Glide
                                    Glide.with(this).load(filePath.toString()).into(addpic);
                                    //addpic.setImageBitmap(bitmap);
                                }

                                catch (IOException e) {
                                    e.printStackTrace();
                                }
                              /*  addpic.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                cursor.close();
                            }
                        }*/

                    }
                    break;
            }
        }
    }
    private void SelectImage()
    {

        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST);
    }
   /* @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data)
    {

        super.onActivityResult(requestCode,
                resultCode,
                data);

        // checking request code and result code
        // if request code is PICK_IMAGE_REQUEST and
        // resultCode is RESULT_OK
        // then set image in the image view
        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            // Get the Uri of data
            filePath = data.getData();
            try {

                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                getContentResolver(),
                                filePath);
                addpic.setImageBitmap(bitmap);
            }

            catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }*/

    private void uploadImage()
    {
        if (filePath != null) {

            ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref
                    = storageReference
                    .child("images/"+ id.toString()+".jpg");

            // adding listeners on upload
            // or failure of image
            ref.putFile(filePath)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {

                                    // Image uploaded successfully
                                    // Dismiss dialog
                                    progressDialog.dismiss();
                                    Toast
                                            .makeText(AddMemberActivity.this,
                                                    "Image Uploaded!!",
                                                    Toast.LENGTH_SHORT)
                                            .show();
                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {

                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast
                                    .makeText(AddMemberActivity.this,
                                            "Failed " + e.getMessage(),
                                            Toast.LENGTH_SHORT)
                                    .show();
                        }
                    });
                    }
        else
        {
            Bitmap bitmap = addpic.getDrawingCache();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);

            byte[] data = baos.toByteArray();
            StorageReference ref
                    = storageReference
                    .child("images/"+ id.toString()+".jpg");
            ref.putBytes(data);
            }catch(Exception e)
            {

            }

        }
    }


}
