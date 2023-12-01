package com.androidcodestudio.promhsadmin.ui.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.androidcodestudio.promhsadmin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.smarteist.autoimageslider.DefaultSliderView;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderLayout;


public class HomeFragment extends Fragment {

    SliderLayout sliderLayout;
    ImageView map;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_home, container, false);

        map = view.findViewById(R.id.map);
        sliderLayout = view.findViewById(R.id.slider);
        sliderLayout.setIndicatorAnimation(IndicatorAnimations.FILL);

        sliderLayout.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderLayout.setScrollTimeInSec(4);

        setSliderViews();

        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://goo.gl/maps/yKXhvp86R6Z77d4E9");
                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                intent.setPackage("com.google.android.apps.maps");
                startActivity(intent);
            }
        });

       return view;
    }

    private void setSliderViews() {


        try {
            FirebaseFirestore.getInstance().collection("SLIDER_IMAGE")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {

                                    String url_one = document.get("IMAGE_URL_1").toString();
                                    String url_two = document.get("IMAGE_URL_2").toString();
                                    String url_three = document.get("IMAGE_URL_3").toString();
                                    String url_four = document.get("IMAGE_URL_4").toString();
                                    String url_five = document.get("IMAGE_URL_5").toString();

                                    String url_dec_one = document.get("IMAGE_DESCRIPTION_1").toString();
                                    String url_dec_two = document.get("IMAGE_DESCRIPTION_2").toString();
                                    String url_dec_three = document.get("IMAGE_DESCRIPTION_3").toString();
                                    String url_dec_four = document.get("IMAGE_DESCRIPTION_4").toString();
                                    String url_dec_five = document.get("IMAGE_DESCRIPTION_5").toString();

                                    for (int i = 0; i < 5; i++) {
                                        DefaultSliderView sliderView = new DefaultSliderView(getContext());

                                        switch (i) {
                                            case 0:
                                                sliderView.setDescription(url_dec_one);
                                                sliderView.setImageUrl(url_one);
                                                break;
                                            case 1:
                                                sliderView.setDescription(url_dec_two);
                                                sliderView.setImageUrl(url_two);
                                                break;
                                            case 2:
                                                sliderView.setDescription(url_dec_three);
                                                sliderView.setImageUrl(url_three);
                                                break;
                                            case 3:
                                                sliderView.setDescription(url_dec_four);
                                                sliderView.setImageUrl(url_four);
                                                break;
                                            case 4:
                                                sliderView.setDescription(url_dec_five);
                                                sliderView.setImageUrl(url_five);
                                                break;
                                        }
                                        sliderView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
                                        sliderLayout.addSliderView(sliderView);
                                    }
                                }
                            } else {
                                String error = task.getException().getMessage();
                                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}