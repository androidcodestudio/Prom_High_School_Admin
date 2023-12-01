package com.androidcodestudio.promhsadmin.UserNotice;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.androidcodestudio.promhsadmin.AdminNotice.AdminNoticeActivity;
import com.androidcodestudio.promhsadmin.LiveClass.LiveClassPojo;
import com.androidcodestudio.promhsadmin.R;
import com.androidcodestudio.promhsadmin.ui.notice.UpdateNoticeActivity;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.AdminNoticeViewholder> {
    private ArrayList<NoticePojo> list;
    private Context context;

    public NoticeAdapter(ArrayList<NoticePojo> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public AdminNoticeViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_news_feed_item, parent, false);
        return new AdminNoticeViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminNoticeViewholder holder, int position) {
        holder.setData(list.get(position).getTitle(),list.get(position).getTime(),list.get(position).getDate()
                ,list.get(position).getImage()
                ,list.get(position).getKey()
                ,position);

        String title = list.get(position).getTitle();
        String time = list.get(position).getTime();
        String date = list.get(position).getDate();
        String image = list.get(position).getImage();
        String key = list.get(position).getKey();
        holder.setData(title,time,date,image,key,position);




    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class AdminNoticeViewholder extends RecyclerView.ViewHolder {
        TextView _noticeTitle, _date, _time;
        ImageView _noticeImage;

        Button _deleteBtn,_editBtn;

        public AdminNoticeViewholder(@NonNull View itemView) {
            super(itemView);
            _noticeTitle = itemView.findViewById(R.id.deleteNoticeTitle);
            _noticeImage = itemView.findViewById(R.id.deleteNoticeImage);
            _date = itemView.findViewById(R.id.date);
            _time = itemView.findViewById(R.id.time);
            _deleteBtn = itemView.findViewById(R.id.deleteBtn);
            _editBtn = itemView.findViewById(R.id.editBtn);

        }

        public void setData(String title, String time,String date,String image,final String key, int position) {
            _noticeTitle.setText(title);
            _time.setText(time);
            _date.setText(date);
            Glide.with(context).load(image).into(_noticeImage);

            _deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AlertDialog.Builder(itemView.getContext())
                            .setTitle("Delete Notice !")
                            .setMessage("Are You Sure, You Want To Delete This Notice ?")
                            .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("PromNotice");
                                    reference.child(key).removeValue()
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
                            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //todo
//                                    _addBtn.setVisibility(View.GONE);
//                                    progressDialog.setMessage("Updating...");
//                                    progressDialog.show();
//                                    category_dialog.show();
//                                    _updateBtn.setOnClickListener(new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View v) {
//                                            _categoryName.setText(list.get(position).getUpcomingClassName());
//                                            txtDate.setText(list.get(position).getDate());
//                                            txtTime.setText(list.get(position).getTime());
//                                            if (_categoryName.getText().toString().isEmpty()) {
//                                                _categoryName.setError("Required");
//                                                return;
//                                            }
//                                            if (txtDate.getText().toString().isEmpty()) {
//                                                txtDate.setError("Required");
//                                                return;
//                                            }
//                                            if (txtTime.getText().toString().isEmpty()) {
//                                                txtTime.setError("Required");
//                                                return;
//                                            }
//                                            for(LiveClassPojo pojo :list){
//                                                if (_categoryName.getText().toString().equals(pojo.getUpcomingClassName())){
//                                                    _categoryName.setError("Category Name Already Present!");
//                                                    return;
//                                                }
//                                            }
//                                            category_dialog.dismiss();
//                                            HashMap map = new HashMap();
//                                            map.put("UpcomingClassName",_categoryName.getText().toString());
//                                            map.put("Date",txtDate.getText().toString());
//                                            map.put("Time",txtTime.getText().toString());
//
//                                            FirebaseDatabase database = FirebaseDatabase.getInstance();
//                                            database.getReference()
//                                                    .child("UpcomingNote")
//                                                    .child(_subjectName)
//                                                    .child(list.get(position).getKey())
//                                                    .updateChildren(map)
//                                                    .addOnSuccessListener(new OnSuccessListener() {
//                                                        @SuppressLint("NotifyDataSetChanged")
//                                                        @Override
//                                                        public void onSuccess(Object o) {
//                                                            progressDialog.dismiss();
//                                                            category_dialog.dismiss();
//                                                            Toast.makeText(getContext(), "Updated Successful", Toast.LENGTH_SHORT).show();
//                                                            adapter.notifyDataSetChanged();
//                                                        }
//                                                    }).addOnFailureListener(new OnFailureListener() {
//                                                        @Override
//                                                        public void onFailure(@NonNull Exception e) {
//                                                            progressDialog.dismiss();
//                                                            Toast.makeText(getContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
//                                                        }
//                                                    });
                                        //}
                                   // });

                                }
                            })
                            .setIcon(R.drawable.ic_baseline_warning_amber_24)
                            .show();
                }
            });
            _editBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(itemView.getContext(), UpdateNoticeActivity.class);
                    intent.putExtra("image",image);
                    intent.putExtra("key",list.get(position).getKey());
                    intent.putExtra("title",_noticeTitle.getText().toString());
                    itemView.getContext().startActivity(intent);

                }
            });



        }
    }
}
