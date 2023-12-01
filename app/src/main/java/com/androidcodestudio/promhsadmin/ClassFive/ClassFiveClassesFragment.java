package com.androidcodestudio.promhsadmin.ClassFive;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidcodestudio.promhsadmin.R;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class ClassFiveClassesFragment extends Fragment {
    private RecyclerView ChapterRecyclerView;
    private DatabaseReference reference;
    private List<ClassFiveClassesPojo> list;
    private ClassFiveClassesAdapter adapter;
    private AdView mAdView;
    private ShimmerFrameLayout container;
    private LinearLayout shimmer_layout;
    private FloatingActionButton fab;
    private TextView _chapterName;

    private Dialog category_dialog;
    private CircleImageView _addImage;
    private EditText _categoryName;
    private Button _addBtn;
    String SubjectName;
    private DatabaseReference myRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_class_five_classes, container, false);
        //this code allays mobile screen light on
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //this code allays mobile screen light on

        myRef = FirebaseDatabase.getInstance().getReference();

        _chapterName = view.findViewById(R.id.chapterName);
        ChapterRecyclerView = view.findViewById(R.id.chapter_item_recycler_view);
        fab = view.findViewById(R.id.fab_chapter);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category_dialog.show();
                //startActivity(new Intent(getContext(), AdminAddChapterActivity.class));
            }
        });


        _chapterName.setText("Create "+getActivity().getIntent().getStringExtra("title")+" Chapter");


        category_dialog = new Dialog(getContext());
        category_dialog.setContentView(R.layout.add_category_dialog);
        category_dialog.getWindow().setBackgroundDrawable(getActivity().getDrawable(R.drawable.button));
        category_dialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        category_dialog.setCancelable(true);

        _categoryName = category_dialog.findViewById(R.id.edit_text);
        _addBtn = category_dialog.findViewById(R.id.add_button);

        _addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_categoryName.getText().toString().isEmpty()) {
                    _categoryName.setError("Required");
                    return;
                }
                for(ClassFiveClassesPojo pojo :list){
                    if (_categoryName.getText().toString().equals(pojo.getName())){
                        _categoryName.setError("Category Name Already Present!");
                        return;
                    }
                }
                category_dialog.dismiss();
                UploadCategoryName();
            }
        });

        reference = FirebaseDatabase.getInstance().getReference()

         // ClassFiveChapter
                .child(getActivity().getIntent().getStringExtra("chapterName"))
                .child(getActivity().getIntent().getStringExtra("title"));
        getData();
        return view;
    }

    private void getData() {
        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                list = new ArrayList<>();
                for(DataSnapshot Snapshot : snapshot.getChildren()){
                    list.add(new ClassFiveClassesPojo(Snapshot.child("name").getValue().toString(),
                            Integer.parseInt(Snapshot.child("set").getValue().toString()),
                            Snapshot.getKey()));
                }

                //adapter = new ClassFiveClassesAdapter(getActivity().getIntent().getIntExtra("sets",0),getActivity().getIntent().getStringExtra("title"));
                adapter = new ClassFiveClassesAdapter(getContext(), list, new ClassFiveClassesAdapter.DeleteListener() {
                    @Override
                    public void onDelete(String key, int position) {
                        new AlertDialog.Builder(getContext())
                                .setTitle("Delete Chapter")
                                .setMessage("Are you sure,you want to Delete This Chapter")
                                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        myRef.child("ClassFiveChapter")
                                                .child(getActivity().getIntent().getStringExtra("title"))
                                                .child(list.get(position).getKey())
                                                .removeValue()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @SuppressLint("NotifyDataSetChanged")
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            list.remove(position);
                                                            adapter.notifyDataSetChanged();
                                                        } else {
                                                            Toast.makeText(getContext(), "fail to remove", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                    }
                                }).setNegativeButton("Cancel", null)
                                .setIcon(R.drawable.ic_baseline_warning_amber_24)
                                .show();
                    }
                });
                ChapterRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                ChapterRecyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void UploadCategoryName() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", _categoryName.getText().toString());
        map.put("set", 0);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference()
                .child(getActivity().getIntent().getStringExtra("chapterName"))
                .child(getActivity().getIntent().getStringExtra("title"))
                .child("chapter" + (list.size() + 1))
                .setValue(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}