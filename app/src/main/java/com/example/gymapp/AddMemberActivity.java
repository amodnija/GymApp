package com.example.gymapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class AddMemberActivity extends AppCompatActivity {

    TextView tv,tv1,tv2,tv3,tv4,tv5;
    int id,count=0;
    EditText et1,et2,et3,date,et5;
    Button b;
    ImageButton icon;
    Boolean check;
    Spinner sp;
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
        et1 = findViewById(R.id.amet1);
        et2 = findViewById(R.id.amet2);
        et3 = findViewById(R.id.amet3);
        date = findViewById(R.id.amet4);
        icon = findViewById(R.id.IconCal);

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

                if(et1.getText().toString()=="" || et2.getText().toString().length()==0 || et3.getText().toString().length()==0 || TextUtils.isEmpty(date.getText().toString()))
                {
                    Toast.makeText(AddMemberActivity.this, "One or more required fields is empty!", Toast.LENGTH_LONG).show();
                    check = false;
                }
                if(et2.getText().toString().length()!=10)
                {
                    et2.setError("Mobile number is invalid");
                    check = false;
                }
                if(!(isValidEmail(et3.getText().toString())))
                {
                    et3.setError("Email ID is invalid");
                    check = false;
                }

                if(check==true)
                {

                }




            }
        });

        datepicklistener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                date.setText(dayOfMonth +"-"+month+"-"+year);
            }
        };


    }
    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}