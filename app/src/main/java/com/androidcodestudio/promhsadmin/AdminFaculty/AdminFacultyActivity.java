package com.androidcodestudio.promhsadmin.AdminFaculty;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
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
import java.util.List;

public class AdminFacultyActivity extends AppCompatActivity {

    FloatingActionButton fab;
    private RecyclerView csDepartment,mechanicalDepartment,physicsDepartment,ChemistryDepartment;
    private LinearLayout csNoData,mcNoData,physicsNoData,ChemistryNoData;
    private List<TeacherPojo> list1,list2,list3,list4;
    private DatabaseReference reference,dbRef;
    private TeacherAdapter teacherAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_faculty);

        csNoData = findViewById(R.id.csNoData);
        mcNoData = findViewById(R.id.mcNoData);
        physicsNoData = findViewById(R.id.physicsNoData);
        ChemistryNoData = findViewById(R.id.ChemistryNoData);

        csDepartment = findViewById(R.id.csDepartment);
        mechanicalDepartment = findViewById(R.id.mechanicalDepartment);
        physicsDepartment = findViewById(R.id.physicsDepartment);
        ChemistryDepartment = findViewById(R.id.ChemistryDepartment);

        reference = FirebaseDatabase.getInstance().getReference().child("PromHighSchoolTeachers");
        csDepartment();
        mechanicalDepartment();
        physicsDepartment();


        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminFacultyActivity.this,AdminAddTeacherActivity.class));
            }
        });
    }

    private void csDepartment(){
        dbRef = reference.child("Science Department");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                list1 = new ArrayList<>();
                if (!snapshot.exists()){
                    csNoData.setVisibility(View.VISIBLE);
                    csDepartment.setVisibility(View.GONE);
                }else{
                    csNoData.setVisibility(View.GONE);
                    csDepartment.setVisibility(View.VISIBLE);
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        TeacherPojo data = dataSnapshot.getValue(TeacherPojo.class);
                        list1.add(data);
                    }
                    csDepartment.setHasFixedSize(true);
                    csDepartment.setLayoutManager(new LinearLayoutManager(AdminFacultyActivity.this));
                    teacherAdapter = new TeacherAdapter(list1,AdminFacultyActivity.this,"Science Department");
                    csDepartment.setAdapter(teacherAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(AdminFacultyActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void mechanicalDepartment(){
        dbRef = reference.child("Commerce Department");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                list2 = new ArrayList<>();
                if (!snapshot.exists()){
                    mcNoData.setVisibility(View.VISIBLE);
                    mechanicalDepartment.setVisibility(View.GONE);
                }else{
                    mcNoData.setVisibility(View.GONE);
                    mechanicalDepartment.setVisibility(View.VISIBLE);
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        TeacherPojo data = dataSnapshot.getValue(TeacherPojo.class);
                        list2.add(data);
                    }
                    mechanicalDepartment.setHasFixedSize(true);
                    mechanicalDepartment.setLayoutManager(new LinearLayoutManager(AdminFacultyActivity.this));
                    teacherAdapter = new TeacherAdapter(list2,AdminFacultyActivity.this,"Commerce Department");
                    mechanicalDepartment.setAdapter(teacherAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(AdminFacultyActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void physicsDepartment(){
        dbRef = reference.child("Arts Department");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                list3 = new ArrayList<>();
                if (!snapshot.exists()){
                    physicsNoData.setVisibility(View.VISIBLE);
                    physicsDepartment.setVisibility(View.GONE);
                }else{
                    physicsNoData.setVisibility(View.GONE);
                    physicsDepartment.setVisibility(View.VISIBLE);
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        TeacherPojo data = dataSnapshot.getValue(TeacherPojo.class);
                        list3.add(data);
                    }
                    physicsDepartment.setHasFixedSize(true);
                    physicsDepartment.setLayoutManager(new LinearLayoutManager(AdminFacultyActivity.this));
                    teacherAdapter = new TeacherAdapter(list3,AdminFacultyActivity.this,"Arts Department");
                    physicsDepartment.setAdapter(teacherAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(AdminFacultyActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
