package com.androidcodestudio.promhsadmin.category;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.androidcodestudio.promhsadmin.R;
import com.androidcodestudio.promhsadmin.set.SetsActivity;
import com.bumptech.glide.Glide;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;

public class Categories_Adapter extends RecyclerView.Adapter<Categories_Adapter.Category_viewHolder> {

    private Context context;
    private List<Categories_pojo> categories_pojos;

    public Categories_Adapter(Context context, List<Categories_pojo> categories_pojos) {
        this.context = context;
        this.categories_pojos = categories_pojos;
    }

    @NonNull
    @Override
    public Category_viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.categories_item_view,parent,false);
        return new Category_viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Category_viewHolder holder, int position) {
        holder.setCategory(categories_pojos.get(position).getUrl(),categories_pojos.get(position).getName(),categories_pojos.get(position).getSet());
        String _names = categories_pojos.get(position).getName();
        int  _set  = categories_pojos.get(position).getSet();
        String _url = categories_pojos.get(position).getUrl();
        holder.setCategory(_names,_url,_set);
    }

    @Override
    public int getItemCount() {
        return categories_pojos.size();
    }

    public class Category_viewHolder extends RecyclerView.ViewHolder {

        CircleImageView _url;
        TextView _title;

        public Category_viewHolder(@NonNull View itemView) {
            super(itemView);
            _url = itemView.findViewById(R.id.category_icon);
            _title = itemView.findViewById(R.id.category_title);
        }
        private void setCategory(final String title,final String url,final int set){
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
                    Intent setintent = new Intent(itemView.getContext(), SetsActivity.class);
                    setintent.putExtra("title",title);
                    setintent.putExtra("sets",set);
                    itemView.getContext().startActivity(setintent);
                }
            });

        }
    }
}
