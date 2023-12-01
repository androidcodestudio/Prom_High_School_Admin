package com.androidcodestudio.promhsadmin.ClassFive;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.androidcodestudio.promhsadmin.AdminClassesAndMockTestActivity;
import com.androidcodestudio.promhsadmin.R;
import com.bumptech.glide.Glide;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ClassFiveAdapter extends RecyclerView.Adapter<ClassFiveAdapter.ClassFive_viewHolder>{
    private Context context;
    private List<ClassFivePojo> classFivePojoList;
    private DeleteListener deleteListener;

    public ClassFiveAdapter(Context context, List<ClassFivePojo> classFivePojoList, DeleteListener deleteListener) {
        this.context = context;
        this.classFivePojoList = classFivePojoList;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public ClassFive_viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.categories_item_view,parent,false);
        return new ClassFive_viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassFive_viewHolder holder, int position) {
        holder.setCategory(classFivePojoList.get(position).getUrl()
                ,classFivePojoList.get(position).getName()
                ,classFivePojoList.get(position).getSet()
                ,classFivePojoList.get(position).getKey()
                ,position);

        String _names = classFivePojoList.get(position).getName();
        int  _set  = classFivePojoList.get(position).getSet();
        String _url = classFivePojoList.get(position).getUrl();
        String key = classFivePojoList.get(position).getKey();
        holder.setCategory(_names,_url,_set,key,position);
    }

    @Override
    public int getItemCount() {
        return classFivePojoList.size();
    }

    public class ClassFive_viewHolder extends RecyclerView.ViewHolder {
        CircleImageView _url;
        TextView _title;
        public ClassFive_viewHolder(@NonNull View itemView) {
            super(itemView);
            _url = itemView.findViewById(R.id.category_icon);
            _title = itemView.findViewById(R.id.category_title);
        }
        private void setCategory(final String title,final String url,final int set,final String key,final int position){
            Glide.with(context).load(url).into(_url);
            _title.setText(title);

//            final Dialog loadingDialog = new Dialog(context);
//            loadingDialog.setContentView(R.layout.loading);
//            loadingDialog.getWindow().setLayout(ConstraintLayout.LayoutParams.WRAP_CONTENT,ConstraintLayout.LayoutParams.WRAP_CONTENT);
//            loadingDialog.setCancelable(false);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    loadingDialog.show();
                    //Intent setintent = new Intent(itemView.getContext(), ClassFiveSetActivity.class);
                    //Intent setintent = new Intent(itemView.getContext(), TabActivity.class);
                    Intent setintent = new Intent(itemView.getContext(), AdminClassesAndMockTestActivity.class);
                    setintent.putExtra("chapterName","ClassFiveChapter");
                    setintent.putExtra("title",title);
                    setintent.putExtra("sets",set);
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
