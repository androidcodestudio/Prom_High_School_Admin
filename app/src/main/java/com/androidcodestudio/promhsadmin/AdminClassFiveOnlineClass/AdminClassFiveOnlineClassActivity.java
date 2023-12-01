package com.androidcodestudio.promhsadmin.AdminClassFiveOnlineClass;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidcodestudio.promhsadmin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class AdminClassFiveOnlineClassActivity extends AppCompatActivity {

    private Button _add_btn;
    private RecyclerView _recyclerView;

    private ClassFiveOnlineClassAdapter _adepter;
    public static List<ClassFiveOnlineClassePojo> list;
    private Dialog loadingDialog;
    private DatabaseReference myRef;

    private int set;
    private String categoryName;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_class_five_online_class);

        Toolbar toolbar = findViewById(R.id.toolbara);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add Online Class");
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        myRef = FirebaseDatabase.getInstance().getReference();
        categoryName = getIntent().getStringExtra("category");
        set = getIntent().getIntExtra("setNo", 1);

        getSupportActionBar().setTitle(categoryName + "/set" + set);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        loadingDialog = new Dialog(AdminClassFiveOnlineClassActivity.this);
        loadingDialog.setContentView(R.layout.loading);
        loadingDialog.getWindow().setLayout(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        loadingDialog.setCancelable(false);


        _add_btn = findViewById(R.id.add_btn);
        _recyclerView = findViewById(R.id.recycler_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);

        _recyclerView.setLayoutManager(linearLayoutManager);
        list = new ArrayList<>();

        _adepter = new ClassFiveOnlineClassAdapter(list, categoryName,new ClassFiveOnlineClassAdapter.DeleteListener() {
            @Override
            public void onLongClick(int position, String id) {
                new AlertDialog.Builder(AdminClassFiveOnlineClassActivity.this)
                        .setTitle("Delete Online Class Room")
                        .setMessage("Are you sure,you want to Delete This Online Class Room")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                myRef.child("CLASS_FIVE_CONCEPT")
                                        .child(categoryName)
                                        .child("concept")
                                        .child(id)
                                        .removeValue()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    list.remove(position);
                                                    _adepter.notifyDataSetChanged();
                                                } else {
                                                    Toast.makeText(AdminClassFiveOnlineClassActivity.this, "fail to remove", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
                        }).setNegativeButton("Cancel", null)
                        .setIcon(R.drawable.ic_baseline_warning_amber_24)
                        .show();
            }
        });
        _recyclerView.setAdapter(_adepter);

        getData(categoryName, set);

        _add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Add Online Class Activity
                Intent addOnlineClass = new Intent(AdminClassFiveOnlineClassActivity.this, AdminClassFiveAddOnlineClassActivity.class);
                addOnlineClass.putExtra("categoryName", categoryName);
                addOnlineClass.putExtra("setNo", set);
                startActivity(addOnlineClass);
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

    private void getData(String categoryName, int set) {
        loadingDialog.show();
        myRef.child("CLASS_FIVE_CONCEPT")
                .child(categoryName)
                .child("concept")
                .orderByChild("setNo")
                .equalTo(set).addListenerForSingleValueEvent(new ValueEventListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            String id = dataSnapshot.getKey();
                            String concept = dataSnapshot.child("concept").getValue().toString();
                            String url = dataSnapshot.child("url").getValue().toString();
                            int set = Integer.parseInt(dataSnapshot.child("setNo").getValue().toString());
                            list.add(new ClassFiveOnlineClassePojo(id,concept,url,set));
                        }
                        loadingDialog.dismiss();
                        _adepter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(AdminClassFiveOnlineClassActivity.this, "error", Toast.LENGTH_SHORT).show();
                        loadingDialog.dismiss();
                        finish();
                    }
                });

    }



    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onStart() {
        super.onStart();
        _adepter.notifyDataSetChanged();
    }


}