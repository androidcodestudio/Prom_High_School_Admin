package com.androidcodestudio.promhsadmin.ui.about;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.androidcodestudio.promhsadmin.R;

import java.util.List;

public class BranchAdapter extends PagerAdapter {

    private Context context;
    private List<BranchModel> list;

    public BranchAdapter(Context context, List<BranchModel> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
       View view = LayoutInflater.from(context).inflate(R.layout.branch_item_layout,container,false);

        ImageView br_icon;
        TextView brTitle,brDesc;

        br_icon = view.findViewById(R.id.bar_icon);
        brTitle = view.findViewById(R.id.bar_title);
        brDesc = view.findViewById(R.id.bar_desc);

        br_icon.setImageResource(list.get(position).getImg());
        brTitle.setText(list.get(position).getTitle());
        brDesc.setText(list.get(position).getDescription());

        container.addView(view,0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}
