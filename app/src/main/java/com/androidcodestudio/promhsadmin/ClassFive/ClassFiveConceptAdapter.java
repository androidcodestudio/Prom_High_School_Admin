package com.androidcodestudio.promhsadmin.ClassFive;

import android.content.Context;
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

public class ClassFiveConceptAdapter extends RecyclerView.Adapter<ClassFiveConceptAdapter.ClassFiveConceptViewHolder>{
    private Context context;
    private List<ClassFiveConceptPojo> list;

    public ClassFiveConceptAdapter(Context context, List<ClassFiveConceptPojo> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ClassFiveConceptViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_concept,parent,false);
        return new ClassFiveConceptViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassFiveConceptViewHolder holder, int position) {
        holder.ConceptTitle.setText(list.get(position).getConceptTitle());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ClassFiveConceptViewHolder extends RecyclerView.ViewHolder {
        private TextView ConceptTitle;
        public ClassFiveConceptViewHolder(@NonNull View itemView) {
            super(itemView);
            ConceptTitle = itemView.findViewById(R.id.conceptTitle);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent setintent = new Intent(itemView.getContext(), VideoActivity.class);
                    //setintent.putExtra("title",ChapterTitle.getText().toString());
                    //setintent.putExtra("sets",set);
                    itemView.getContext().startActivity(setintent);
                }
            });
        }
    }
}
