package com.example.gymapp;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.menu.MenuView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static androidx.core.content.ContextCompat.startActivity;

public class Adapter1 extends RecyclerView.Adapter<Adapter1.viewholder> {
    @NonNull
    private ArrayList<String> data = new ArrayList<>();
    private ArrayList<Integer> data1 = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    private Context context;

    public Adapter1(ArrayList<String> data,ArrayList<Integer> data1)
    {
        this.data = data;
        this.data1 = data1;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.rv_layout_1, parent, false);

        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        String title = data.get(position);
        Integer id = data1.get(position);
        holder.tv.setText(title);
        holder.idtv.setText(id.toString());

        StorageReference ref = FirebaseStorage.getInstance().getReference().child("images/" + id.toString() + ".jpg");

        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
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
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

        holder.ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    db.collection("members").document(id.toString()).update("routine", "None");
                    data.remove(position);
                    data1.remove(position);
                    notifyItemRemoved(holder.getAdapterPosition());
                    notifyItemRangeChanged(holder.getAdapterPosition(), data.size());
                } catch (Exception e) {
                }
            }
        });
    }



    @Override
    public int getItemCount()
    {
        return data.size();
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




        }
        public void memberEdit(String s)
        {
            Intent intent = new Intent(itemView.getContext(), editMember.class);
            Bundle b = new Bundle();
            b.putString("id",s); //member id
            intent.putExtras(b);
            itemView.getContext().startActivity(intent);
        }


    }


}
