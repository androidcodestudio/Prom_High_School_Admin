package com.androidcodestudio.promhsadmin;

import android.app.Dialog;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidcodestudio.promhsadmin.category.Categories_Adapter;
import com.androidcodestudio.promhsadmin.category.Categories_pojo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;


public class ClasssSixActivity extends AppCompatActivity {

    private RecyclerView category_item_recycler_view;
    private List<Categories_pojo> categories_pojos;
    private Dialog _loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classs_six);

        //loading dialog
        _loadingDialog = new Dialog(this);
        _loadingDialog.setContentView(R.layout.loading);
        _loadingDialog.setCancelable(false);
        _loadingDialog.getWindow().setBackgroundDrawable(this.getDrawable(R.drawable.slider_background));
        _loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        _loadingDialog.show();
        //loading dialog

        //this code allays mobile screen light on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //this code allays mobile screen light on

        Toolbar toolbar = findViewById(R.id.six_toolbar);
        category_item_recycler_view = findViewById(R.id.six_category_item_recycler_view);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.category);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        categories_pojos = new ArrayList<>();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3, RecyclerView.VERTICAL, false);
        category_item_recycler_view.setLayoutManager(gridLayoutManager);

        final Categories_Adapter categories_adapter = new Categories_Adapter(this, categories_pojos);
        category_item_recycler_view.setAdapter(categories_adapter);

        myRef.child("Categories").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot Snapshot : snapshot.getChildren()){
                    categories_pojos.add(Snapshot.getValue(Categories_pojo.class));
                    _loadingDialog.dismiss();
                }
                categories_adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ClasssSixActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}