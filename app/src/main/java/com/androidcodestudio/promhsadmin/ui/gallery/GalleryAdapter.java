package com.androidcodestudio.promhsadmin.ui.gallery;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.androidcodestudio.promhsadmin.R;
import com.bumptech.glide.Glide;

import java.util.List;


public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.GalleryViewholder>{

    private Context context;
    private List<String> images;

    public GalleryAdapter(Context context, List<String> images) {
        this.context = context;
        this.images = images;
    }

    @NonNull
    @Override
    public GalleryViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_image, parent, false);
        return new GalleryViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryViewholder holder, int position) {
        Glide.with(context).load(images.get(position)).into(holder.imageView);


    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class GalleryViewholder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public GalleryViewholder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
        }
    }
}
