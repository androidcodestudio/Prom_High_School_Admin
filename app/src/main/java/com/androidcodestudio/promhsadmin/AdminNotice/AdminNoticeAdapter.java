package com.androidcodestudio.promhsadmin.AdminNotice;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.androidcodestudio.promhsadmin.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AdminNoticeAdapter extends RecyclerView.Adapter<AdminNoticeAdapter.AdminNoticeViewholder>{
    private ArrayList<AdminNoticePojo> list;
    private Context context;

    public AdminNoticeAdapter(ArrayList<AdminNoticePojo> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public AdminNoticeViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.newsfeed_item,parent,false);
        return new AdminNoticeViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminNoticeViewholder holder, @SuppressLint("RecyclerView") int position) {
        AdminNoticePojo currentItem = list.get(position);
        holder.deleteNoticeTitle.setText(currentItem.getTitle());
        Glide.with(context).load(currentItem.getImage()).into(holder.deleteNoticeImage);
        holder.deleteNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("PromNotice");
                reference.child(currentItem.getKey()).removeValue()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(context, "Removed Success", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                            }
                        });
                notifyItemRemoved(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class AdminNoticeViewholder extends RecyclerView.ViewHolder {
        Button deleteNotice;
        TextView deleteNoticeTitle;
        ImageView deleteNoticeImage;
        public AdminNoticeViewholder(@NonNull View itemView) {
            super(itemView);
            deleteNotice = itemView.findViewById(R.id.deleteNotice);
            deleteNoticeTitle = itemView.findViewById(R.id.deleteNoticeTitle);
            deleteNoticeImage = itemView.findViewById(R.id.deleteNoticeImage);
        }
    }
}
