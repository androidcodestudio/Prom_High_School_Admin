package com.androidcodestudio.promhsadmin.Admin_Category;

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

public class AdminCategoriesAdapter extends RecyclerView.Adapter<AdminCategoriesAdapter.AdminCategory_viewHolder> {
    private Context context;
    private List<AdminCategoriesPojo> adminCategoriesPojos;
    private DeleteListener deleteListener;

    public AdminCategoriesAdapter(Context context, List<AdminCategoriesPojo> adminCategoriesPojos,DeleteListener deleteListener) {
        this.context = context;
        this.adminCategoriesPojos = adminCategoriesPojos;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public AdminCategory_viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.admin_category_item,parent,false);
        return new AdminCategory_viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminCategory_viewHolder holder, int position) {
        holder.setCategory(adminCategoriesPojos.get(position).getUrl(),adminCategoriesPojos.get(position).getName(),adminCategoriesPojos.get(position).getSet(),adminCategoriesPojos.get(position).getKey(),position);
        String _names = adminCategoriesPojos.get(position).getName();
        int  _set  = adminCategoriesPojos.get(position).getSet();
        String _url = adminCategoriesPojos.get(position).getUrl();
        String key = adminCategoriesPojos.get(position).getKey();
        holder.setCategory(_names,_url,_set,key,position);
    }

    @Override
    public int getItemCount() {
        return adminCategoriesPojos.size();
    }

    public class AdminCategory_viewHolder extends RecyclerView.ViewHolder {
        CircleImageView _url;
        TextView _title;
        //ImageView _delete;
        public AdminCategory_viewHolder(@NonNull View itemView) {
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
                    //Intent setintent = new Intent(itemView.getContext(), AdminSetsActivity.class);
                    Intent setintent = new Intent(itemView.getContext(), AdminClassesAndMockTestActivity.class);
                    setintent.putExtra("chapterName","ClassTwelveChapter");
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
