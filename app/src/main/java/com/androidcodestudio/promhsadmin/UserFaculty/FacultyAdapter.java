package com.androidcodestudio.promhsadmin.UserFaculty;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.androidcodestudio.promhsadmin.FullImageViewActivity;
import com.androidcodestudio.promhsadmin.R;
import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FacultyAdapter extends RecyclerView.Adapter<FacultyAdapter.TeacherViewholder>{
    private List<FacultyPojo> list;
    private Context context;
    private String category;

    public FacultyAdapter(List<FacultyPojo> list, Context context, String category) {
        this.list = list;
        this.context = context;
        this.category = category;
    }

    @NonNull
    @Override
    public TeacherViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_faculty_item,parent,false);
        return new TeacherViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeacherViewholder holder, int position) {
        String name = list.get(position).getName();
        String email = list.get(position).getEmail();
        String post = list.get(position).getPost();
        String image = list.get(position).getImage();
        String key = list.get(position).getKey();
        holder.setData(name,email,post,image,key,position);


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class TeacherViewholder extends RecyclerView.ViewHolder {

        private TextView name,email,post;
        private CircleImageView _image;


        public TeacherViewholder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.teacherName);
            email = itemView.findViewById(R.id.teacherEmail);
            post = itemView.findViewById(R.id.teacherPost);
            _image = itemView.findViewById(R.id.teacherImage);

        }
        private void setData(String name,String email,String post,String image,String key,int position){

            this.name.setText(name);
            this.email.setText(email);
            this.post.setText(post);
            Glide.with(context).load(image).into(_image);

            _image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, FullImageViewActivity.class);
                    intent.putExtra("image",image);
                    context.startActivity(intent);
                }
            });

        }
    }

}
