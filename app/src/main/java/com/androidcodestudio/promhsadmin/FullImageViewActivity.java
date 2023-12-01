package com.androidcodestudio.promhsadmin;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;

public class FullImageViewActivity extends AppCompatActivity {
    PhotoView fullImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image_view);

        fullImageView = findViewById(R.id.fullImageView);

        String image = getIntent().getStringExtra("image");
        Glide.with(this).load(image).into(fullImageView);
    }
}