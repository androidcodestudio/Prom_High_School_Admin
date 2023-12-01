package com.androidcodestudio.promhsadmin.intro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.androidcodestudio.promhsadmin.MainActivity;
import com.androidcodestudio.promhsadmin.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class IntroActivity extends AppCompatActivity {

    private ViewPager screenPager;
    private IntroViewPagerAdapter introViewPagerAdapter;
    private TabLayout tab_indicator;
    private Button _next_btn;
    private Button getStarted;
    private int position = 0;
    private Animation btn_animation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_intro);

        btn_animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.button_animation);

        final List<IntroPojo> introPojoList = new ArrayList<>();
        introPojoList.add(new IntroPojo("How To Prepare For Govt Jobs?"," Create The Master Plan To Crack Govt Job Exam, Segregate the topics based on your strengths and weaknesses, Allocate fixed time for each section as you need to clear the sectional cut off along with overall cut off", R.drawable.study_fast));
        introPojoList.add(new IntroPojo("What Is the Next Move?","Understand the concepts – clear the theory behind them.,Stay Updated,Use Tricks for Memorization,Prepare for Aptitude and Reasoning,Solve Previous Year Question PapersMake Proper NotesTake Care of Your Health",R.drawable.study_three));
        introPojoList.add(new IntroPojo("Now Manage Your Relationship With Studying","“It does not matter how slowly you go as long as you do not stop.” “Never give up, for that is just the place and time that the tide will turn.” “You just can't beat the person who won't give up.” “There is no failure except in no longer trying.”",R.drawable.study_two));

        screenPager = findViewById(R.id.viewPager);
        introViewPagerAdapter = new IntroViewPagerAdapter(IntroActivity.this,introPojoList);
        screenPager.setAdapter(introViewPagerAdapter);

        tab_indicator = findViewById(R.id.tabLayout);
        tab_indicator.setupWithViewPager(screenPager);

        _next_btn = findViewById(R.id.next_btn);
        _next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position = screenPager.getCurrentItem();
                if (position < introPojoList.size()){

                    position++;
                    screenPager.setCurrentItem(position);
                }

                if (position == introPojoList.size()-1){
                    loadLastSceen();
                }
            }
        });

        tab_indicator.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == introPojoList.size()-1){
                    loadLastSceen();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        getStarted = findViewById(R.id.btn_getStarted);
        getStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent main_intent = new Intent(IntroActivity.this, MainActivity.class);
                startActivity(main_intent);
                finish();
            }
        });
    }

    private void loadLastSceen(){

        getStarted.setVisibility(View.VISIBLE);
        _next_btn.setVisibility(View.INVISIBLE);
        tab_indicator.setVisibility(View.INVISIBLE);

        getStarted.setAnimation(btn_animation);
    }
}
