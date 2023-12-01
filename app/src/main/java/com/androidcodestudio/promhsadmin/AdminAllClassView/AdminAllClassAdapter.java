package com.androidcodestudio.promhsadmin.AdminAllClassView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.androidcodestudio.promhsadmin.AdminClassEight.AdminClassEightActivity;
import com.androidcodestudio.promhsadmin.AdminClassEleven.AdminClassElevenActivity;
import com.androidcodestudio.promhsadmin.AdminClassFive.AdminClassFiveActivity;
import com.androidcodestudio.promhsadmin.AdminClassNine.AdminClassNineActivity;
import com.androidcodestudio.promhsadmin.AdminClassSeven.AdminClassSevenActivity;
import com.androidcodestudio.promhsadmin.AdminClassSix.AdminClassSixActivity;
import com.androidcodestudio.promhsadmin.AdminClassTen.AdminClassTenActivity;
import com.androidcodestudio.promhsadmin.AdminClassTwelve.AdminClassTwelveActivity;
import com.androidcodestudio.promhsadmin.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import java.util.ArrayList;


public class AdminAllClassAdapter extends RecyclerView.Adapter<AdminAllClassAdapter.ViewHolder> {
    private ArrayList<AdminAllClassPojo> _list;
    private int lastposition =-1;
    private DeleteListener deleteListener;

    private Dialog _loadingDialog;

    public AdminAllClassAdapter() {
    }

    public AdminAllClassAdapter(ArrayList<AdminAllClassPojo> _list, DeleteListener deleteListener) {
        this._list = _list;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.categari_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.setData(_list.get(position).getCategoryName(),_list.get(position).getCategoryIconLink()
                ,_list.get(position).getBackgroundColor()
                ,_list.get(position).getIndex()
                ,_list.get(position).getKey()
                ,position);

        String name = _list.get(position).getCategoryName();
        String icon = _list.get(position).getCategoryIconLink();
        String background_color = _list.get(position).getBackgroundColor();
        int index = _list.get(position).getIndex();
        String key = _list.get(position).getKey();
        holder.setData(name,icon,background_color,index,key,position);

        if (lastposition<position) {
            Animation animation = new AnimationUtils().loadAnimation(holder.itemView.getContext(), R.anim.fade_in);
            holder.itemView.setAnimation(animation);
            lastposition=position;

        }

    }

    @Override
    public int getItemCount() {
        return _list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView _categoryIcon;
        private TextView _categoryName,_index;
        private LinearLayout _backgroundColor;



        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            _categoryIcon=(ImageView)itemView.findViewById(R.id.category_icon);
            _categoryName=(TextView)itemView.findViewById(R.id.category_name);
            _backgroundColor =(LinearLayout)itemView.findViewById(R.id.card_color);
            _index =(TextView)itemView.findViewById(R.id.index);

        }

        private void setData(final String name,String iconUrl,String backgroundColor,int index,final String key,final int position){
            _categoryName.setText("Class ("+name+")");
            _index.setText("index ("+index+")");

            //loading dialog
            _loadingDialog = new Dialog(itemView.getContext());
            _loadingDialog.setContentView(R.layout.loading);
            _loadingDialog.setCancelable(false);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                _loadingDialog.getWindow().setBackgroundDrawable(itemView.getContext().getDrawable(R.drawable.slider_background));
            }
            _loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            //loading dialog

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (position == 0) {

                        _loadingDialog.show();
                        Intent intent = new Intent(itemView.getContext(), AdminClassFiveActivity.class);
                        intent.putExtra("CategoryName", name);
                        itemView.getContext().startActivity(intent);
                        _loadingDialog.dismiss();


                    }else if (position == 1){
                        _loadingDialog.show();
                        Intent intent = new Intent(itemView.getContext(), AdminClassSixActivity.class);
                        intent.putExtra("CategoryName", name);
                        itemView.getContext().startActivity(intent);
                        _loadingDialog.dismiss();


                    }else if (position == 2){
                        _loadingDialog.show();
                        Intent intent = new Intent(itemView.getContext(), AdminClassSevenActivity.class);
                        intent.putExtra("CategoryName", name);
                        itemView.getContext().startActivity(intent);
                        _loadingDialog.dismiss();


                    }else if (position == 3){
                        _loadingDialog.show();
                        Intent intent = new Intent(itemView.getContext(), AdminClassEightActivity.class);
                        intent.putExtra("CategoryName", name);
                        itemView.getContext().startActivity(intent);
                        _loadingDialog.dismiss();

                    }else if (position == 4){
                        _loadingDialog.show();
                        Intent intent = new Intent(itemView.getContext(), AdminClassNineActivity.class);
                        intent.putExtra("CategoryName", name);
                        itemView.getContext().startActivity(intent);
                        _loadingDialog.dismiss();


                    }else if (position == 5){
                        _loadingDialog.show();
                        Intent intent = new Intent(itemView.getContext(), AdminClassTenActivity.class);
                        intent.putExtra("CategoryName", name);
                        itemView.getContext().startActivity(intent);
                        _loadingDialog.dismiss();


                    }else if (position == 6){
                        _loadingDialog.show();
                        Intent intent = new Intent(itemView.getContext(), AdminClassElevenActivity.class);
                        intent.putExtra("CategoryName", name);
                        itemView.getContext().startActivity(intent);
                        _loadingDialog.dismiss();


                    }else if (position == 7){
                        _loadingDialog.show();
                        Intent intent = new Intent(itemView.getContext(), AdminClassTwelveActivity.class);
                        intent.putExtra("CategoryName", name);
                        itemView.getContext().startActivity(intent);
                        _loadingDialog.dismiss();


                    }


                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    deleteListener.onDelete(key,position);
                    return false;
                }
            });

            if (!iconUrl.equals("null")) {
                Glide.with(itemView.getContext()).load(iconUrl).apply(new RequestOptions().placeholder(R.color.aluminum)).into(_categoryIcon);
            }else{
                _categoryIcon.setImageResource(R.color.aluminum);
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                _backgroundColor.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(backgroundColor)));
            }
        }



    }

    public interface DeleteListener{
        public void onDelete (String key,int position);
    }

}
