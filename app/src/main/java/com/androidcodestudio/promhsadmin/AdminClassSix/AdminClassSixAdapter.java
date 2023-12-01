package com.androidcodestudio.promhsadmin.AdminClassSix;

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

public class AdminClassSixAdapter extends RecyclerView.Adapter<AdminClassSixAdapter.AdminClassSix_viewHolder>{

    private Context context;
    public static List<AdminClassSixPojo> adminClassSixPojoList;
    private DeleteListener deleteListener;

    public AdminClassSixAdapter(Context context, List<AdminClassSixPojo> adminClassSixPojoList, DeleteListener deleteListener) {
        this.context = context;
        this.adminClassSixPojoList = adminClassSixPojoList;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public AdminClassSix_viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.admin_category_item,parent,false);
        return new AdminClassSix_viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminClassSix_viewHolder holder, int position) {
        holder.setCategory(adminClassSixPojoList.get(position).getUrl()
                ,adminClassSixPojoList.get(position).getName()
                ,adminClassSixPojoList.get(position).getSet()
                ,adminClassSixPojoList.get(position).getKey()
                ,position);
        String _names = adminClassSixPojoList.get(position).getName();
        int  _set  = adminClassSixPojoList.get(position).getSet();
        String _url = adminClassSixPojoList.get(position).getUrl();
        String key = adminClassSixPojoList.get(position).getKey();
        holder.setCategory(_names,_url,_set,key,position);
    }

    @Override
    public int getItemCount() {
        return adminClassSixPojoList.size();
    }

    public class AdminClassSix_viewHolder extends RecyclerView.ViewHolder {
        CircleImageView _url;
        TextView _title;
        //ImageView _delete;
        public AdminClassSix_viewHolder(@NonNull View itemView) {
            super(itemView);
            _url = itemView.findViewById(R.id.category_icon);
            _title = itemView.findViewById(R.id.category_title);
            //_delete = itemView.findViewById(R.id.close);
        }
        private void setCategory(final String title,final String url,final int set,final String key,final int position){
            Glide.with(context).load(url).into(_url);
            _title.setText(title);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    loadingDialog.show();
                    Intent setintent = new Intent(itemView.getContext(), AdminClassesAndMockTestActivity.class);
                    setintent.putExtra("chapterName","ClassSixChapter");
                    setintent.putExtra("conceptName","CLASS_SIX_CONCEPT");
                    setintent.putExtra("setName","CLASS_SIX_SETS");
                    setintent.putExtra("className","ClassSixCategories");
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
