package com.androidcodestudio.promhsadmin.AdminClassTen;

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
public class AdminClassTenAdapter extends RecyclerView.Adapter<AdminClassTenAdapter.AdminClassTen_viewHolder>{

    private Context context;
    public static List<AdminClassTenPojo> adminClassTenPojoList;
    private DeleteListener deleteListener;

    public AdminClassTenAdapter(Context context, List<AdminClassTenPojo> adminClassTenPojoList, DeleteListener deleteListener) {
        this.context = context;
        this.adminClassTenPojoList = adminClassTenPojoList;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public AdminClassTen_viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.admin_category_item,parent,false);
        return new AdminClassTen_viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminClassTen_viewHolder holder, int position) {
        holder.setCategory(adminClassTenPojoList.get(position).getUrl(),adminClassTenPojoList.get(position).getName(),adminClassTenPojoList.get(position).getSet(),adminClassTenPojoList.get(position).getKey(),position);
        String _names = adminClassTenPojoList.get(position).getName();
        int  _set  = adminClassTenPojoList.get(position).getSet();
        String _url = adminClassTenPojoList.get(position).getUrl();
        String key = adminClassTenPojoList.get(position).getKey();
        holder.setCategory(_names,_url,_set,key,position);
    }

    @Override
    public int getItemCount() {
        return adminClassTenPojoList.size();
    }

    public class AdminClassTen_viewHolder extends RecyclerView.ViewHolder {
        CircleImageView _url;
        TextView _title;
        //ImageView _delete;
        public AdminClassTen_viewHolder(@NonNull View itemView) {
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
                    //Intent setintent = new Intent(itemView.getContext(), AdminClassTenSetsActivity.class);
                    Intent setintent = new Intent(itemView.getContext(), AdminClassesAndMockTestActivity.class);
                    setintent.putExtra("chapterName","ClassTenChapter");
                    setintent.putExtra("className","ClassTenCategories");
                    setintent.putExtra("conceptName","CLASS_TEN_CONCEPT");
                    setintent.putExtra("setName","CLASS_TEN_SETS");
                    setintent.putExtra("title",title);
                    setintent.putExtra("sets",adminClassTenPojoList.get(position).getSet());
                    setintent.putExtra("key",adminClassTenPojoList.get(position).getKey());
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
