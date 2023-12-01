package com.androidcodestudio.promhsadmin.AdminClassEleven;

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

public class AdminClassElevenAdapter extends RecyclerView.Adapter<AdminClassElevenAdapter.AdminClassEleven_viewHolder>{
    private Context context;
    public static List<AdminClassElevenPojo> adminClassElevenPojoList;
    private DeleteListener deleteListener;

    public AdminClassElevenAdapter(Context context, List<AdminClassElevenPojo> adminClassElevenPojoList, DeleteListener deleteListener) {
        this.context = context;
        this.adminClassElevenPojoList = adminClassElevenPojoList;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public AdminClassEleven_viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.admin_category_item,parent,false);
        return new AdminClassEleven_viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminClassEleven_viewHolder holder, int position) {
        holder.setCategory(adminClassElevenPojoList.get(position).getUrl(),adminClassElevenPojoList.get(position).getName(),adminClassElevenPojoList.get(position).getSet(),adminClassElevenPojoList.get(position).getKey(),position);
        String _names = adminClassElevenPojoList.get(position).getName();
        int  _set  = adminClassElevenPojoList.get(position).getSet();
        String _url = adminClassElevenPojoList.get(position).getUrl();
        String key = adminClassElevenPojoList.get(position).getKey();
        holder.setCategory(_names,_url,_set,key,position);

    }

    @Override
    public int getItemCount() {
        return adminClassElevenPojoList.size();
    }

    public class AdminClassEleven_viewHolder extends RecyclerView.ViewHolder {
        CircleImageView _url;
        TextView _title;
        //ImageView _delete;
        public AdminClassEleven_viewHolder(@NonNull View itemView) {
            super(itemView);
            _url = itemView.findViewById(R.id.category_icon);
            _title = itemView.findViewById(R.id.category_title);
            //_delete = itemView.findViewById(R.id.close);
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
                    //Intent setintent = new Intent(itemView.getContext(), AdminClassElevenSetsActivity.class);
                    Intent setintent = new Intent(itemView.getContext(), AdminClassesAndMockTestActivity.class);
                    setintent.putExtra("chapterName","ClassElevenChapter");
                    setintent.putExtra("className","ClassElevenCategories");
                    setintent.putExtra("conceptName","CLASS_ELEVEN_CONCEPT");
                    setintent.putExtra("setName","CLASS_ELEVEN_SETS");
                    setintent.putExtra("title",title);
                    setintent.putExtra("sets",set);
                    setintent.putExtra("key",key);
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
