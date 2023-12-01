package com.androidcodestudio.promhsadmin.ClassFive;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.androidcodestudio.promhsadmin.AdminClassFiveOnlineClass.AdminClassFiveOnlineClassActivity;
import com.androidcodestudio.promhsadmin.R;

import java.util.List;

public class ClassFiveClassesAdapter extends RecyclerView.Adapter<ClassFiveClassesAdapter.ClassFiveViewHolder>{
    private Context context;
    private List<ClassFiveClassesPojo> list;
    private DeleteListener deleteListener;

    public ClassFiveClassesAdapter(Context context, List<ClassFiveClassesPojo> list, DeleteListener deleteListener) {
        this.context = context;
        this.list = list;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public ClassFiveViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_chapter,parent,false);
        return new ClassFiveViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassFiveViewHolder holder, int position) {

        holder.setData(list.get(position).getName(),list.get(position).getSet(),list.get(position).getKey(),position);

        String title = list.get(position).getName();
        int set = list.get(position).getSet();
        String key = list.get(position).getKey();
        holder.setData(title,set,key,position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ClassFiveViewHolder extends RecyclerView.ViewHolder {

        private TextView ChapterTitle;

        public ClassFiveViewHolder(@NonNull View itemView) {
            super(itemView);

            ChapterTitle = itemView.findViewById(R.id.chapterTitle);
//            set = itemView.findViewById(R.id.set);

        }

        public void setData(String title, int set,final String key, int position) {
            this.ChapterTitle.setText(position+1+". "+title);
//            this.set.setText("("+set+")");

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent setintent = new Intent(itemView.getContext(), AdminClassFiveOnlineClassActivity.class);
                    setintent.putExtra("category",title);
                    setintent.putExtra("setNo",set);
                    itemView.getContext().startActivity(setintent);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    deleteListener.onDelete(key,position);
                    return false;
                }
            });
        }
    }

    public interface DeleteListener{
        public void onDelete (String key,int position);
    }
}
