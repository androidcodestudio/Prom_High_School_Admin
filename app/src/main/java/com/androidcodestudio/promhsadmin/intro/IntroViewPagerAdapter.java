package com.androidcodestudio.promhsadmin.intro;

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

public class IntroViewPagerAdapter extends PagerAdapter {

    Context _context;
    List<IntroPojo> _introPojoList;

    public IntroViewPagerAdapter(Context _context, List<IntroPojo> _introPojoList) {
        this._context = _context;
        this._introPojoList = _introPojoList;
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        LayoutInflater layoutInflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layoutScreen = layoutInflater.inflate(R.layout.intro_screen,null);


        ImageView imageSlide = layoutScreen.findViewById(R.id.slideImage);
        TextView title = layoutScreen.findViewById(R.id.intro_title);
        TextView descraitpion = layoutScreen.findViewById(R.id.intro_decr);

        imageSlide.setImageResource(_introPojoList.get(position).getScreenImages());
        title.setText(_introPojoList.get(position).getTitle());
        descraitpion.setText(_introPojoList.get(position).getDescription());

        container.addView(layoutScreen);

        return layoutScreen;


    }

    @Override
    public int getCount() {
        return _introPojoList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}
