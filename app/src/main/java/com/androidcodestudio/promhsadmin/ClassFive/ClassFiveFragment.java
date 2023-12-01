package com.androidcodestudio.promhsadmin.ClassFive;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidcodestudio.promhsadmin.FirebaseDatabase.DataBaseQuerys;
import com.androidcodestudio.promhsadmin.FullImageViewActivity;
import com.androidcodestudio.promhsadmin.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static com.androidcodestudio.promhsadmin.FirebaseDatabase.DataBaseQuerys.Image;


public class ClassFiveFragment extends Fragment {

    private Window _window;

    private TextView name_tv,roll_tv,section_tv,whichClass;
    private CircleImageView imageView;

    private RecyclerView category_item_recycler_view;
    private List<ClassFivePojo> categories_pojos;
    private ClassFiveAdapter categories_adapter;

    private FloatingActionButton fab;
    private Dialog category_dialog;
    private EditText _categoryName;
    private Button _addBtn;
    private Uri image;
    private String downloadurl;
    private CircleImageView _addImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_class_five, container, false);

        //this code for status bar color change
        _window=getActivity().getWindow();
        _window.setStatusBarColor(this.getResources().getColor(R.color.backface));
        _window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        //this code for status bar color change

        //this code allays mobile screen light on
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //this code allays mobile screen light on

        fab = view.findViewById(R.id.add_subject);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category_dialog.show();
            }
        });

        //For Add Subject
        category_dialog = new Dialog(getContext());
        category_dialog.setContentView(R.layout.add_category_dialog);
        category_dialog.getWindow().setBackgroundDrawable(getActivity().getDrawable(R.drawable.button));
        category_dialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        category_dialog.setCancelable(true);

        _addImage = category_dialog.findViewById(R.id.category_pic);
        _categoryName = category_dialog.findViewById(R.id.edit_text);
        _addBtn = category_dialog.findViewById(R.id.add_button);

        _addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, 101);
            }
        });

        _addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_categoryName.getText().toString().isEmpty()) {
                    _categoryName.setError("Required");
                    return;
                }
                for(ClassFivePojo pojo :categories_pojos){
                    if (_categoryName.getText().toString().equals(pojo.getName())){
                        _categoryName.setError("Subject Name Already Present!");
                        return;
                    }

                }
                //upload
                category_dialog.dismiss();
                UploadData();
            }
        });
        //For Add Subject

        category_item_recycler_view = view.findViewById(R.id.category_item_recycler_view);
        whichClass = view.findViewById(R.id.whichClass);
        imageView = view.findViewById(R.id.studentImage);
        name_tv = view.findViewById(R.id.name);
        roll_tv = view.findViewById(R.id.roll);
        section_tv = view.findViewById(R.id.section);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), FullImageViewActivity.class);
                intent.putExtra("image",Image);
                startActivity(intent);
            }
        });

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        categories_pojos = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2, RecyclerView.VERTICAL, false);
        category_item_recycler_view.setLayoutManager(layoutManager);

        categories_adapter = new ClassFiveAdapter(getContext(), categories_pojos, new ClassFiveAdapter.DeleteListener() {
            @Override
            public void onDelete(String key, int position) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Delete Subject")
                        .setMessage("Are you sure,you want to Delete this Subject")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                myRef.child("ClassFiveCategories")
                                        .child(key)
                                        .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            myRef.child("CLASS_FIVE_SETS").child(categories_pojos.get(position).getName()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        categories_pojos.remove(position);
                                                        categories_adapter.notifyDataSetChanged();
                                                    }else{
                                                        Toast.makeText(getContext(), "fail to remove", Toast.LENGTH_SHORT).show();

                                                    }

                                                }
                                            });


                                        } else {
                                            Toast.makeText(getContext(), "fail to remove", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }).setNegativeButton("Cancel",null)
                        .setIcon(R.drawable.ic_baseline_warning_amber_24)
                        .show();
            }
        });
        category_item_recycler_view.setAdapter(categories_adapter);

        myRef.child("ClassFiveCategories").addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot Snapshot : snapshot.getChildren()){
                    categories_pojos.add(new ClassFivePojo(Snapshot.child("name").getValue().toString(),
                            Integer.parseInt(Snapshot.child("set").getValue().toString()),
                            Snapshot.child("url").getValue().toString(),
                            Snapshot.getKey()));
                }
                categories_adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101) {
            if (resultCode == RESULT_OK) {
                image = data.getData();
                _addImage.setImageURI(image);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            getActivity().finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getUid())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            Image = task.getResult().getString("Image");
                            DataBaseQuerys.Name = task.getResult().getString("Name");
                            DataBaseQuerys.roll = task.getResult().getString("Roll");
                            DataBaseQuerys.section = task.getResult().getString("Section");
                            DataBaseQuerys.whichClass = task.getResult().getString("Class");

                            Glide.with(getContext()).load(Image).into(imageView);
                            whichClass.setText("Class: "+String.format(DataBaseQuerys.whichClass));
                            name_tv.setText("Name: "+String.format(DataBaseQuerys.Name));
                            roll_tv.setText("Roll: "+String.format(DataBaseQuerys.roll));
                            section_tv.setText("Section: "+String.format(DataBaseQuerys.section));

                        } else {
                            String error = task.getException().getMessage();
                            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void UploadData() {

        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference imageReference = storageRef.child("categories").child(image.getLastPathSegment());

        UploadTask uploadTask = imageReference.putFile(image);
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw Objects.requireNonNull(task.getException());
                }
                // Continue with the task to get the download URL
                return imageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            downloadurl = task.getResult().toString();
                            UploadCategoryName();
                        } else {
                            Toast.makeText(getContext(), "problem", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Uri downloadUri = task.getResult();
            } else {
                Toast.makeText(getContext(), "problem", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void UploadCategoryName() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", _categoryName.getText().toString());
        map.put("set", 0);
        map.put("url", downloadurl);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference()
                .child("ClassFiveCategories")
                .child("category" + (categories_pojos.size() + 1))
                .setValue(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            categories_pojos.add(new ClassFivePojo(_categoryName.getText().toString(), 0, downloadurl, "category" + (categories_pojos.size() + 1)));
                            categories_adapter.notifyDataSetChanged();
                            Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }
}