package com.androidcodestudio.promhsadmin.bookmark;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.androidcodestudio.promhsadmin.R;
import com.androidcodestudio.promhsadmin.quiestion.Question_Pojo;

import java.util.List;

public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.BookmarkViewHolder> {
    private List<Question_Pojo> questionPojoList;

//    public BookmarkAdapter() {
//        //for real time database
//    }

    public BookmarkAdapter(List<Question_Pojo> questionPojoList) {
        this.questionPojoList = questionPojoList;
    }

    @NonNull
    @Override
    public BookmarkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bookmark_item,parent,false);
        return new BookmarkViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookmarkViewHolder holder, int position) {
        holder.setData(questionPojoList.get(position).getQuestion(),questionPojoList.get(position).getCorrect_ans(),position);
    }

    @Override
    public int getItemCount() {
        return questionPojoList.size();
    }

    public class BookmarkViewHolder extends RecyclerView.ViewHolder {

       private TextView quastion,answer;
       private ImageButton delete;
        public BookmarkViewHolder(@NonNull View itemView) {
            super(itemView);
            quastion = itemView.findViewById(R.id.question);
            answer = itemView.findViewById(R.id.answar);
            delete = itemView.findViewById(R.id.delete);
        }

        private void setData(String quatin, String answar, final int position){
            this.quastion.setText(quatin);
            this.answer.setText(answar);
            this.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    questionPojoList.remove(position);
                    notifyDataSetChanged();
                }
            });


        }
    }
}
