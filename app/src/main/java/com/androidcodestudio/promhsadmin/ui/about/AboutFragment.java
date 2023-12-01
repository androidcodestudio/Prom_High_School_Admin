package com.androidcodestudio.promhsadmin.ui.about;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;


import com.androidcodestudio.promhsadmin.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class AboutFragment extends Fragment {

    private ViewPager viewPager;
    private BranchAdapter adapter;
    private List<BranchModel> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        list = new ArrayList<>();
        list.add(new BranchModel(R.drawable.ic_computer,"Computer Science","Our school was established on 1972. Its became Junior High School on 1982 and Secondary in 2005. This school was upgraded to Higher Secondary school in 2016"));
        list.add(new BranchModel(R.drawable.ic_civil,"Civil Engineering","Our school was established on 1972. Its became Junior High School on 1982 and Secondary in 2005. This school was upgraded to Higher Secondary school in 2016"));
        list.add(new BranchModel(R.drawable.ic_mobile,"Mobile Repairing","Our school was established on 1972. Its became Junior High School on 1982 and Secondary in 2005. This school was upgraded to Higher Secondary school in 2016"));

        adapter = new BranchAdapter(getContext(),list);
        viewPager = view.findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);

        ImageView imageView = view.findViewById(R.id.collage_image);
        Glide.with(getContext()).load("https://firebasestorage.googleapis.com/v0/b/comexam-e9453.appspot.com/o/images.jpg?alt=media&token=241b2605-6278-4ff3-8c95-c1505c12fa5b").into(imageView);

        return view;
    }
}