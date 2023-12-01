package com.androidcodestudio.promhsadmin.Admin_Question;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.androidcodestudio.promhsadmin.AddQuestionActivity;
import com.androidcodestudio.promhsadmin.R;

import java.util.List;

public class AdminQuestionsAdepter extends RecyclerView.Adapter<AdminQuestionsAdepter.QViewholder> {
    private List<AdminQuestionsPojo> list;
    private String category;
    private DeleteListener deleteListener;


    public AdminQuestionsAdepter(List<AdminQuestionsPojo> list,String category,DeleteListener deleteListener) {
        this.list = list;
        this.category = category;
        this.deleteListener=deleteListener;
    }

    @NonNull
    @Override
    public QViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_item,parent,false);
        return new QViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QViewholder holder, int position) {
        String question = list.get(position).getQuestion();
        String answer = list.get(position).getCorrect_ans();
        holder.setData(question,answer,position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class QViewholder extends RecyclerView.ViewHolder{

        private TextView question,answer;

        public QViewholder(@NonNull View itemView) {
            super(itemView);
            question = itemView.findViewById(R.id.questiona);
            answer = itemView.findViewById(R.id.answerb);
        }

        private void setData(String question,String answer,int position){

            this.question.setText(position+1+". "+question);
            this.answer.setText("Ans. "+answer);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent editintent = new Intent(itemView.getContext(), AddQuestionActivity.class);
                    editintent.putExtra("categoryName",category);
                    editintent.putExtra("setNo",list.get(position).getSet());
                    editintent.putExtra("position",position);
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
