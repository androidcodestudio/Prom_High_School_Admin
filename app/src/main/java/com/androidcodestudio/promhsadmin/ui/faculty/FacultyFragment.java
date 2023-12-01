package com.androidcodestudio.promhsadmin.ui.faculty;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidcodestudio.promhsadmin.AdminFaculty.AdminFacultyActivity;
import com.androidcodestudio.promhsadmin.R;
import com.androidcodestudio.promhsadmin.UserFaculty.FacultyAdapter;
import com.androidcodestudio.promhsadmin.UserFaculty.FacultyPojo;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;


public class FacultyFragment extends Fragment {

    private RecyclerView csDepartment,mechanicalDepartment,physicsDepartment,ChemistryDepartment;
    private LinearLayout csNoData,mcNoData,physicsNoData,ChemistryNoData;
    private List<FacultyPojo> list1,list2,list3,list4;
    private DatabaseReference reference,dbRef;
    private FacultyAdapter teacherAdapter;

    private ShimmerFrameLayout shimmer_container;
    private LinearLayout shimmer_layout;

    private NestedScrollView scrollable;
    private FloatingActionButton fab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_faculty, container, false);

        scrollable =  view.findViewById(R.id.scrollable);
        shimmer_container =  view.findViewById(R.id.shimmer_view_container);
        shimmer_layout = view.findViewById(R.id.shimmer_layout);
        shimmer_container.startShimmer(); // If auto-start is set to false

        csNoData = view.findViewById(R.id.csNoData);
        mcNoData = view.findViewById(R.id.mcNoData);
        physicsNoData = view.findViewById(R.id.physicsNoData);
        ChemistryNoData = view.findViewById(R.id.ChemistryNoData);

        csDepartment = view.findViewById(R.id.csDepartment);
        mechanicalDepartment = view.findViewById(R.id.mechanicalDepartment);
        physicsDepartment = view.findViewById(R.id.physicsDepartment);
        ChemistryDepartment = view.findViewById(R.id.ChemistryDepartment);

        fab = view.findViewById(R.id.add_faculty);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AdminFacultyActivity.class));
            }
        });

        reference = FirebaseDatabase.getInstance().getReference().child("PromHighSchoolTeachers");
        csDepartment();
        mechanicalDepartment();
        physicsDepartment();
        return view;
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
                        FacultyPojo data = dataSnapshot.getValue(FacultyPojo.class);
                        list1.add(data);
                    }
                    csDepartment.setHasFixedSize(true);
                    csDepartment.setLayoutManager(new LinearLayoutManager(getContext()));
                    teacherAdapter = new FacultyAdapter(list1,getContext(),"Science Department");
                    csDepartment.setAdapter(teacherAdapter);

                    shimmer_container.stopShimmer();
                    shimmer_layout.setVisibility(View.GONE);
                    scrollable.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
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
                        FacultyPojo data = dataSnapshot.getValue(FacultyPojo.class);
                        list2.add(data);
                    }
                    mechanicalDepartment.setHasFixedSize(true);
                    mechanicalDepartment.setLayoutManager(new LinearLayoutManager(getContext()));
                    teacherAdapter = new FacultyAdapter(list2,getContext(),"Commerce Department");
                    mechanicalDepartment.setAdapter(teacherAdapter);
                    shimmer_container.stopShimmer();
                    shimmer_layout.setVisibility(View.GONE);
                    scrollable.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
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
                        FacultyPojo data = dataSnapshot.getValue(FacultyPojo.class);
                        list3.add(data);
                    }
                    physicsDepartment.setHasFixedSize(true);
                    physicsDepartment.setLayoutManager(new LinearLayoutManager(getContext()));
                    teacherAdapter = new FacultyAdapter(list3,getContext(),"Arts Department");
                    physicsDepartment.setAdapter(teacherAdapter);

                    shimmer_container.stopShimmer();
                    shimmer_layout.setVisibility(View.GONE);
                    scrollable.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onPause() {
        shimmer_container.stopShimmer();
        super.onPause();
    }

    @Override
    public void onResume() {
        shimmer_container.startShimmer();
        super.onResume();
    }
}