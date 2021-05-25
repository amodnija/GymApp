package com.example.gymapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

import static androidx.core.content.ContextCompat.startActivity;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.viewholder> {
    @NonNull
    private String[] data;
    private Context context;

    private Integer[] data1;
    public MyAdapter(String[] data,Integer[] data1)
    {
        this.data = data;
        this.data1 = data1;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.rv_layout, parent, false);

        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        String title = data[position];
        Integer id = data1[position];
        holder.tv.setText(title);
        holder.idtv.setText(id.toString());


        StorageReference ref = FirebaseStorage.getInstance().getReference().child("images/"+id.toString()+".jpg");

        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                if(uri!=null) {
                    // Got the download URL for 'users/me/profile.png'
                    RequestOptions requestOptions = new RequestOptions();
                    requestOptions.placeholder(R.drawable.person_icon);
                    requestOptions.error(R.drawable.person_icon);
                    requestOptions.diskCacheStrategy(DiskCacheStrategy.DATA);
                    GlideApp.with(holder.itemView)
                            .load(uri.toString())
                            .diskCacheStrategy(DiskCacheStrategy.DATA)
                            .dontAnimate()
                            .into(holder.img);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });


    }

    @Override
    public int getItemCount()
    {
        return data.length;
    }


    public class viewholder extends RecyclerView.ViewHolder{
        CardView cv;
        ImageView img;
        TextView tv,idtv;
        ImageButton ib;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.cv);
            idtv = itemView.findViewById(R.id.rvid);
            ib = itemView.findViewById(R.id.editbutton);
            img = itemView.findViewById(R.id.iv);
            tv = itemView.findViewById(R.id.name);
            cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    memberEdit(idtv.getText().toString());

                }
            });
            ib.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    memberEdit(idtv.getText().toString());
                }
            });


        }
        public void memberEdit(String s)
        {
            Intent intent = new Intent(itemView.getContext(), editMember.class);
            Bundle b = new Bundle();
            b.putString("id",s); //member id
            intent.putExtras(b);
            itemView.getContext().startActivity(intent);
            ((Activity)itemView.getContext()).finish();

        }


    }

}
