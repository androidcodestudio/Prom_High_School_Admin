package com.androidcodestudio.promhsadmin.AdminSliderImage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.androidcodestudio.promhsadmin.R;
import com.bumptech.glide.Glide;

import java.util.List;


public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.SliderAdapter_viewHolder>{
    private Context context;
    public static List<SliderPojo> sliderPojoList;
    private DeleteListener deleteListener;

    public SliderAdapter(Context context, List<SliderPojo> sliderPojoList, DeleteListener deleteListener) {
        this.context = context;
        this.sliderPojoList = sliderPojoList;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public SliderAdapter_viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.slider_item_view,parent,false);
        return new SliderAdapter_viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SliderAdapter_viewHolder holder, int position) {
        holder.setCategory(
                sliderPojoList.get(position).getTitle()
                ,sliderPojoList.get(position).getUrl()
                ,sliderPojoList.get(position).getKey()
                ,position);


        String  _title  = sliderPojoList.get(position).getTitle();
        String _url = sliderPojoList.get(position).getUrl();
        String key = sliderPojoList.get(position).getKey();
        holder.setCategory(_title,_url,key,position);
    }

    @Override
    public int getItemCount() {
        return sliderPojoList.size();
    }

    public class SliderAdapter_viewHolder extends RecyclerView.ViewHolder {
        ImageView _url;
        TextView _title;
        ImageView _delete;
        public SliderAdapter_viewHolder(@NonNull View itemView) {
            super(itemView);
            _url = itemView.findViewById(R.id.category_icon);
            _title = itemView.findViewById(R.id.category_title);
            _delete = itemView.findViewById(R.id.close);
        }
        private void setCategory(final String title,final String url,final String key,final int position){
            _title.setText("Youtube Embed Url "+title);
            Glide.with(context).load(url).into(_url);


//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
////                    loadingDialog.show();
//                    Intent setintent = new Intent(itemView.getContext(),TabActivity.class);
//                    setintent.putExtra("chapterName","Chapter");
//                    setintent.putExtra("title",title);
//                    setintent.putExtra("sets",set);
//                    setintent.putExtra("key",key);
//                    itemView.getContext().startActivity(setintent);
//                }
//            });

            _delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteListener.onDelete(key,position);
                }
            });

        }
    }
    public interface DeleteListener{
        public void onDelete (String key,int position);
    }
}
