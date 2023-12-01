package com.androidcodestudio.promhsadmin.AdminClassFiveSet;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidcodestudio.promhsadmin.AdminAddChapterActivity;
import com.androidcodestudio.promhsadmin.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;


public class AdminClassFiveSetFragment extends Fragment {

    private RecyclerView set_item_RecyclerView;
    private Dialog loadingDialog;
    private AdminClassFiveSetAdapter adapter;
    private List<AdminClassFiveSetPojo> list;
    private DatabaseReference reference;
    private FloatingActionButton fab;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_class_five_set, container, false);
        set_item_RecyclerView = view.findViewById(R.id.set_item_recycler_view);


        loadingDialog = new Dialog(getContext());
        loadingDialog.setContentView(R.layout.loading);
        loadingDialog.getWindow().setLayout(ConstraintLayout.LayoutParams.WRAP_CONTENT,ConstraintLayout.LayoutParams.WRAP_CONTENT);
        loadingDialog.setCancelable(false);

        fab = view.findViewById(R.id.fab_mockTest);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AdminAddChapterActivity.class));
            }
        });


        //reference = FirebaseDatabase.getInstance().getReference().child("ClassFiveChapter");


        reference = FirebaseDatabase.getInstance().getReference().child("ClassFiveCategories")
                .child(getActivity().getIntent().getStringExtra("key")).child("set");
        getData();

        return view;
    }

    private void getData() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list = new ArrayList<>();
                for (DataSnapshot snapshot1 : snapshot.getChildren()){
                    AdminClassFiveSetPojo pojo = snapshot1.getValue(AdminClassFiveSetPojo.class);
                    list.add(0,pojo);
                }
                adapter = new AdminClassFiveSetAdapter(getContext(),list);
                set_item_RecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                set_item_RecyclerView.setAdapter(adapter);
//                container.stopShimmer();
//                shimmer_layout.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}