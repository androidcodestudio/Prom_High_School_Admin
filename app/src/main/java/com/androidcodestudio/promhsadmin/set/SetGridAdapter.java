package com.androidcodestudio.promhsadmin.set;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.androidcodestudio.promhsadmin.R;
import com.androidcodestudio.promhsadmin.quiestion.QuestionActivity;


public class SetGridAdapter extends BaseAdapter {

    private int sets = 0;
    private String category;

    public SetGridAdapter(int sets, String category) {
        this.sets = sets;
        this.category = category;
    }

    @Override
    public int getCount() {
        return sets;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

//        final Dialog loadingDialog = new Dialog(parent.getContext());
//        loadingDialog.setContentView(R.layout.loading);
//        loadingDialog.getWindow().setLayout(ConstraintLayout.LayoutParams.WRAP_CONTENT,ConstraintLayout.LayoutParams.WRAP_CONTENT);
//        loadingDialog.setCancelable(false);

        View view;
        if(convertView==null){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sets_item_grid_view,parent,false);
        }else
        {
            view = convertView;
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // loadingDialog.show();
                Intent questionintent = new Intent(parent.getContext(), QuestionActivity.class);
                questionintent.putExtra("category",category);
                questionintent.putExtra("setNo",position+1);
                parent.getContext().startActivity(questionintent);
            }
        });

        ((TextView)view.findViewById(R.id.set_number)).setText(String.valueOf(position+1));
        return view;
    }
}
