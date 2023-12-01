package com.androidcodestudio.promhsadmin.category;

import android.app.Dialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidcodestudio.promhsadmin.FirebaseDatabase.DataBaseQuerys;
import com.androidcodestudio.promhsadmin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class Categories_Activity extends AppCompatActivity {

    private RecyclerView category_item_recycler_view;
    private List<Categories_pojo> categories_pojos;
    private Dialog _loadingDialog;
    private Window _window;
    private Toolbar toolbar;

    TextView name_tv,roll_tv,section_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories_);

        _window=this.getWindow();
        _window.setStatusBarColor(this.getResources().getColor(R.color.backface));
        _window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        //this code allays mobile screen light on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //this code allays mobile screen light on

        Toolbar toolbar = findViewById(R.id.toolbar);
        category_item_recycler_view = findViewById(R.id.category_item_recycler_view);

        name_tv = findViewById(R.id.name);
        roll_tv = findViewById(R.id.roll);
        section_tv = findViewById(R.id.section);


        //loading dialog
        _loadingDialog = new Dialog(this);
        _loadingDialog.setContentView(R.layout.loading);
        _loadingDialog.setCancelable(false);
        _loadingDialog.getWindow().setBackgroundDrawable(this.getDrawable(R.drawable.slider_background));
        _loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        _loadingDialog.show();
        //loading dialog

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();


        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.category);
        getSupportActionBar().getThemedContext();

        toolbar.setTitleTextColor(getResources().getColor(R.color.black));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        categories_pojos = new ArrayList<>();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, RecyclerView.VERTICAL, false);
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
                Toast.makeText(Categories_Activity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getUid())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DataBaseQuerys.phone = task.getResult().getString("phone");
                            DataBaseQuerys.Name = task.getResult().getString("Name");
                            DataBaseQuerys.roll = task.getResult().getString("Roll");
                            DataBaseQuerys.section = task.getResult().getString("Section");

                            //_user_phone.setText(DataBaseQuerys.phone);
                            name_tv.setText(String.format(DataBaseQuerys.Name));
                            roll_tv.setText(String.format(DataBaseQuerys.roll));
                            section_tv.setText(String.format(DataBaseQuerys.section));

                        } else {
                            String error = task.getException().getMessage();
                            Toast.makeText(Categories_Activity.this, error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}