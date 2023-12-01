package com.androidcodestudio.promhsadmin.AdminNotice;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.androidcodestudio.promhsadmin.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DeleteNoticeActivity extends AppCompatActivity {
    RecyclerView NoticeRecycler;
    ProgressBar progressBar;
    ArrayList<AdminNoticePojo> list;
    AdminNoticeAdapter adapter;
    FloatingActionButton fab;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_notice);

        NoticeRecycler = findViewById(R.id.deleteNoticeRecycler);
        progressBar = findViewById(R.id.progressBar);

        reference = FirebaseDatabase.getInstance().getReference().child("PromNotice");

        NoticeRecycler.setLayoutManager(new LinearLayoutManager(this));
        NoticeRecycler.setHasFixedSize(true);
        fab = findViewById(R.id.fab_notice);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DeleteNoticeActivity.this, AdminNoticeActivity.class));
            }
        });
        getNotice();

    }

    private void getNotice() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list = new ArrayList<>();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    AdminNoticePojo data = dataSnapshot.getValue(AdminNoticePojo.class);
                    list.add(data);

                }
                adapter = new AdminNoticeAdapter(list, DeleteNoticeActivity.this);
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                NoticeRecycler.setAdapter(adapter);

                }

                @Override
                public void onCancelled (@NonNull DatabaseError error){
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(DeleteNoticeActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }
    }