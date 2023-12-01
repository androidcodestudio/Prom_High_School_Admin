package com.androidcodestudio.promhsadmin.AdminClassFiveOnlineClass;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.androidcodestudio.promhsadmin.R;
import com.androidcodestudio.promhsadmin.VideoActivity;

import java.util.List;

public class ClassFiveOnlineClassAdapter extends RecyclerView.Adapter<ClassFiveOnlineClassAdapter.ClassFiveOnlineClassViewholder>{
    private List<ClassFiveOnlineClassePojo> list;
    private String category;
    private DeleteListener deleteListener;

    public ClassFiveOnlineClassAdapter(List<ClassFiveOnlineClassePojo> list, String category,  DeleteListener deleteListener) {
        this.list = list;
        this.category = category;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public ClassFiveOnlineClassViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chapter,parent,false);
        return new ClassFiveOnlineClassViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassFiveOnlineClassViewholder holder, int position) {
        String title = list.get(position).getConcept();
        String url = list.get(position).getUrl();
        int set = list.get(position).getSet();
        holder.setData(title,url,set,position);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ClassFiveOnlineClassViewholder extends RecyclerView.ViewHolder {

        private TextView ChapterTitle,set;

        public ClassFiveOnlineClassViewholder(@NonNull View itemView) {
            super(itemView);

            ChapterTitle = itemView.findViewById(R.id.chapterTitle);
            //set = itemView.findViewById(R.id.set);
        }

        public void setData(String title,String url, int set, int position) {
            this.ChapterTitle.setText(position+1+". "+title);
            //this.set.setText("("+set+")");

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent editintent = new Intent(itemView.getContext(), VideoActivity.class);
                    editintent.putExtra("url",url);
                    itemView.getContext().startActivity(editintent);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    deleteListener.onLongClick(position,list.get(position).getId());
                    return false;
                }
            });
        }
    }

    public interface DeleteListener{
        void onLongClick(int position,String id );
    }
}
