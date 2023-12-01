package com.androidcodestudio.promhsadmin.ClassFive;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidcodestudio.promhsadmin.AddConceptActivity;
import com.androidcodestudio.promhsadmin.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ClassFiveConceptActivity extends AppCompatActivity {

    private RecyclerView ConceptRecyclerView;
    private DatabaseReference reference;
    private List<ClassFiveConceptPojo> list;
    private ClassFiveConceptAdapter adapter;
    private AdView mAdView;
    private ImageView _back_buttonConcept;
    private Window _window;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_five_concept);

        _window=this.getWindow();
        _window.setStatusBarColor(this.getResources().getColor(R.color.backface));
        _window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        //this code allays mobile screen light on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //this code allays mobile screen light on

        ConceptRecyclerView = findViewById(R.id.concept_item_recycler_view);
        _back_buttonConcept = findViewById(R.id.back_buttonConcept);

        _back_buttonConcept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        fab = findViewById(R.id.fab_concept);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ClassFiveConceptActivity.this, AddConceptActivity.class));
            }
        });


        //banner ad
        mAdView = findViewById(R.id.adViewConcept);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdClicked() {
                super.onAdClicked();
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdClosed() {


                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                super.onAdFailedToLoad(adError);
                mAdView.loadAd(adRequest);
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdImpression() {
                // Code to be executed when an impression is recorded
                // for an ad.
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                Toast.makeText(ClassFiveConceptActivity.this, "BANNER AD LOADED", Toast.LENGTH_SHORT).show();
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }
        });
        reference = FirebaseDatabase.getInstance().getReference().child("pdf");
        getData();
    }

    private void getData() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list = new ArrayList<>();
                for (DataSnapshot snapshot1 : snapshot.getChildren()){
                    ClassFiveConceptPojo pojo = snapshot1.getValue(ClassFiveConceptPojo.class);
                    list.add(pojo);
                }
                adapter = new ClassFiveConceptAdapter(ClassFiveConceptActivity.this,list);
                ConceptRecyclerView.setLayoutManager(new LinearLayoutManager(ClassFiveConceptActivity.this));
                ConceptRecyclerView.setAdapter(adapter);
//                container.stopShimmer();
//                shimmer_layout.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ClassFiveConceptActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}