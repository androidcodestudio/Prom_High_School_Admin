package com.androidcodestudio.promhsadmin.AdminClassFive;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.androidcodestudio.promhsadmin.AdminClassesAndMockTestActivity;
import com.androidcodestudio.promhsadmin.R;
import com.bumptech.glide.Glide;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;

public class AdminClassFiveAdapter extends RecyclerView.Adapter<AdminClassFiveAdapter.AdminClassFive_viewHolder>{

    private Context context;
    public static List<AdminClassFivePojo> adminClassFivePojoList;
    private DeleteListener deleteListener;

    public AdminClassFiveAdapter(Context context, List<AdminClassFivePojo> adminClassFivePojoList, DeleteListener deleteListener) {
        this.context = context;
        this.adminClassFivePojoList = adminClassFivePojoList;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public AdminClassFive_viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.admin_category_item,parent,false);
        return new AdminClassFive_viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminClassFive_viewHolder holder, int position) {
        holder.setCategory(
                adminClassFivePojoList.get(position).getName()
                ,adminClassFivePojoList.get(position).getSet()
                ,adminClassFivePojoList.get(position).getUrl()
                ,adminClassFivePojoList.get(position).getKey()
                ,position);

        String _names = adminClassFivePojoList.get(position).getName();
        int  _set  = adminClassFivePojoList.get(position).getSet();
        String _url = adminClassFivePojoList.get(position).getUrl();
        String key = adminClassFivePojoList.get(position).getKey();
        holder.setCategory(_names,_set,_url,key,position);
    }

    @Override
    public int getItemCount() {
        return adminClassFivePojoList.size();
    }

    public class AdminClassFive_viewHolder extends RecyclerView.ViewHolder {
        CircleImageView _url;
        TextView _title;
        ImageView _close;
        //ImageView _delete;
        public AdminClassFive_viewHolder(@NonNull View itemView) {
            super(itemView);

            _url = itemView.findViewById(R.id.category_icon);
            _title = itemView.findViewById(R.id.category_title);
            _close = itemView.findViewById(R.id.close);

        }
        private void setCategory(final String title,final int set,final String url,final String key,final int position){
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
                    Intent setintent = new Intent(itemView.getContext(), AdminClassesAndMockTestActivity.class);
                    setintent.putExtra("chapterName","ClassFiveChapter");
                    setintent.putExtra("className","ClassFiveCategories");
                    setintent.putExtra("conceptName","CLASS_FIVE_CONCEPT");
                    setintent.putExtra("setName","CLASS_FIVE_SETS");
                    setintent.putExtra("title",title);
                    setintent.putExtra("sets",set);
                    setintent.putExtra("key",adminClassFivePojoList.get(position).getKey());
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

            _close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });



        }
    }
    public interface DeleteListener{
        public void onDelete (String key,int position);
    }
}
